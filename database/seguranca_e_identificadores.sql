USE achados_perdidos;

-- =============================================================
-- SEGURANÇA: USUÁRIOS DO MYSQL E PERMISSÕES (GRANT)
-- =============================================================
-- Por padrão, a aplicação conectava como 'root', que tem acesso
-- total ao servidor (todos os bancos, DDL, DCL, outros usuários
-- etc). Para não depender do root, criamos usuários dedicados,
-- cada um só com os privilégios que o seu papel realmente precisa
-- (princípio do menor privilégio).

-- Usuário de aplicação: é o usuário que o sistema Java (DAO.java)
-- usa para se conectar ao banco, independente de quem está logado
-- no sistema — o controle de quem é aluno, funcionário ou admin é
-- feito dentro da aplicação (tabelas Usuario/Funcionario/Administrador).
-- Por isso ele precisa de acesso de leitura/escrita a todas as
-- tabelas da aplicação, mas nenhum privilégio administrativo
-- (sem DROP, sem CREATE, sem GRANT, sem acesso a outros bancos).
DROP USER IF EXISTS 'app_achados'@'localhost';
CREATE USER 'app_achados'@'localhost' IDENTIFIED BY 'App@chados2026!';

GRANT SELECT, INSERT, UPDATE, DELETE ON achados_perdidos.* TO 'app_achados'@'localhost';

-- Usuários de demonstração por grupo (ligados à tabela grupos_usuarios
-- criada pelo Miguel), mostrando o princípio do menor privilégio na
-- prática: cada grupo só enxerga o que precisa pro seu papel.

-- Administrador: acesso total aos dados da aplicação, porque no
-- sistema o administrador também gerencia cargos de funcionário.
DROP USER IF EXISTS 'achados_admin'@'localhost';
CREATE USER 'achados_admin'@'localhost' IDENTIFIED BY 'Admin@chados2026!';
GRANT SELECT, INSERT, UPDATE, DELETE ON achados_perdidos.* TO 'achados_admin'@'localhost';

-- Funcionario: cadastra e gerencia objetos, reivindicações e
-- devoluções, mas não precisa mexer em usuários, funcionários ou
-- administradores (só consultar, pra saber quem cadastrou o quê).
DROP USER IF EXISTS 'achados_funcionario'@'localhost';
CREATE USER 'achados_funcionario'@'localhost' IDENTIFIED BY 'Func@chados2026!';
GRANT SELECT ON achados_perdidos.Usuario TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON achados_perdidos.Objeto TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON achados_perdidos.Foto TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON achados_perdidos.Pergunta_Seguranca TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT, UPDATE ON achados_perdidos.Reivindicacao TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT ON achados_perdidos.Devolucao TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT ON achados_perdidos.Retirante TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT ON achados_perdidos.Log_Auditoria TO 'achados_funcionario'@'localhost';

-- Aluno: só consulta objetos e reivindica, não cadastra objeto nem
-- mexe em devolução ou auditoria diretamente.
DROP USER IF EXISTS 'achados_aluno'@'localhost';
CREATE USER 'achados_aluno'@'localhost' IDENTIFIED BY 'Aluno@chados2026!';
GRANT SELECT ON achados_perdidos.Objeto TO 'achados_aluno'@'localhost';
GRANT SELECT, INSERT, UPDATE ON achados_perdidos.Retirante TO 'achados_aluno'@'localhost';
GRANT SELECT, INSERT ON achados_perdidos.Reivindicacao TO 'achados_aluno'@'localhost';
GRANT SELECT ON achados_perdidos.Devolucao TO 'achados_aluno'@'localhost';

FLUSH PRIVILEGES;

-- Pra conferir os privilégios de cada usuário:
-- SHOW GRANTS FOR 'app_achados'@'localhost';
-- SHOW GRANTS FOR 'achados_admin'@'localhost';
-- SHOW GRANTS FOR 'achados_funcionario'@'localhost';
-- SHOW GRANTS FOR 'achados_aluno'@'localhost';

-- =============================================================
-- FUNÇÕES PARA GERAÇÃO DE IDENTIFICADORES
-- =============================================================
DELIMITER $$

-- Calcula o próximo ID disponível de Retirante sem depender do
-- AUTO_INCREMENT — útil, por exemplo, pra prever/exibir o próximo
-- ID antes de uma inserção, sem precisar inserir primeiro.
CREATE FUNCTION fn_proximo_id_retirante()
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE proximo_id INT;
    SELECT IFNULL(MAX(ID_Retirante), 0) + 1 INTO proximo_id FROM Retirante;
    RETURN proximo_id;
END $$

-- Gera um número de protocolo legível pra devolução, combinando o
-- ano da devolução com o ID sequencial, no formato DEV-AAAA-000123.
CREATE FUNCTION fn_gerar_protocolo_devolucao(p_id_devolucao INT)
RETURNS VARCHAR(20)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE ano_devolucao INT;

    SELECT YEAR(Data_Devolucao) INTO ano_devolucao
    FROM Devolucao
    WHERE ID_Devolucao = p_id_devolucao;

    RETURN CONCAT('DEV-', ano_devolucao, '-', LPAD(p_id_devolucao, 6, '0'));
END $$

DELIMITER ;

-- Exemplos de uso:
-- SELECT fn_proximo_id_retirante();
-- SELECT fn_gerar_protocolo_devolucao(1);

-- =============================================================
-- DOCUMENTAÇÃO: USO DO AUTO_INCREMENT
-- =============================================================
-- O AUTO_INCREMENT é a estratégia padrão de geração de chave
-- primária em praticamente todas as tabelas do sistema:
--   Usuario.ID_Usuario, Funcionario.ID_Funcionario,
--   Administrador.ID_Administrador, Objeto.ID_Objeto,
--   Pergunta_Seguranca.ID_Pergunta, Retirante.ID_Retirante,
--   Reivindicacao.ID_Reivindicacao, Devolucao.ID_Devolucao,
--   Log_Auditoria.ID_Log, grupos_usuarios.id_grupo.
--
-- Vantagens: o próprio MySQL gera o valor de forma atômica a cada
-- INSERT, então duas conexões inserindo ao mesmo tempo nunca
-- recebem o mesmo ID, e a aplicação não precisa de nenhuma lógica
-- extra pra descobrir qual é o próximo valor livre.
--
-- Exceções, por design:
--   • Foto não usa AUTO_INCREMENT: sua chave primária é composta
--     (ID_Objeto, Numero_Foto) — o número da foto é controlado pela
--     aplicação, reiniciando a contagem a cada objeto.
--   • rel_usuario_grupo não usa: sua chave é composta pelas duas
--     chaves estrangeiras (id_usuario, id_grupo).
--
-- As funções fn_proximo_id_retirante e fn_gerar_protocolo_devolucao
-- NÃO substituem o AUTO_INCREMENT como gerador de chave primária
-- (duas conexões poderiam calcular o mesmo "próximo ID" antes de
-- qualquer uma inserir de fato); elas são utilitários complementares,
-- para consulta/relatório e para gerar um identificador "amigável" a
-- partir de um ID que já existe.
