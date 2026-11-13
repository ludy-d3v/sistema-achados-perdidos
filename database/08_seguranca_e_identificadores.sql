USE achados_perdidos;

-- usuario da aplicacao (substitui o root no DAO.java)
DROP USER IF EXISTS 'app_achados'@'localhost';
CREATE USER 'app_achados'@'localhost' IDENTIFIED BY 'App@chados2026!';
GRANT SELECT, INSERT, UPDATE, DELETE ON achados_perdidos.* TO 'app_achados'@'localhost';

-- administrador: acesso total
DROP USER IF EXISTS 'achados_admin'@'localhost';
CREATE USER 'achados_admin'@'localhost' IDENTIFIED BY 'Admin@chados2026!';
GRANT SELECT, INSERT, UPDATE, DELETE ON achados_perdidos.* TO 'achados_admin'@'localhost';

-- funcionario: cadastra/edita/exclui objeto, aprova/recusa reivindicacao, registra devolucao
DROP USER IF EXISTS 'achados_funcionario'@'localhost';
CREATE USER 'achados_funcionario'@'localhost' IDENTIFIED BY 'Func@chados2026!';
GRANT SELECT ON achados_perdidos.Usuario TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON achados_perdidos.Objeto TO 'achados_funcionario'@'localhost';
GRANT SELECT, UPDATE ON achados_perdidos.Reivindicacao TO 'achados_funcionario'@'localhost';
GRANT SELECT, INSERT ON achados_perdidos.Devolucao TO 'achados_funcionario'@'localhost';
GRANT SELECT ON achados_perdidos.Retirante TO 'achados_funcionario'@'localhost';
GRANT INSERT ON achados_perdidos.Log_Auditoria TO 'achados_funcionario'@'localhost';
GRANT SELECT ON achados_perdidos.consultar_reivindicacoes TO 'achados_funcionario'@'localhost';

-- aluno: consulta objeto e reivindica
DROP USER IF EXISTS 'achados_aluno'@'localhost';
CREATE USER 'achados_aluno'@'localhost' IDENTIFIED BY 'Aluno@chados2026!';
GRANT SELECT ON achados_perdidos.Objeto TO 'achados_aluno'@'localhost';
GRANT SELECT, INSERT ON achados_perdidos.Retirante TO 'achados_aluno'@'localhost';
GRANT SELECT, INSERT ON achados_perdidos.Reivindicacao TO 'achados_aluno'@'localhost';
GRANT SELECT ON achados_perdidos.Devolucao TO 'achados_aluno'@'localhost';
GRANT INSERT ON achados_perdidos.Log_Auditoria TO 'achados_aluno'@'localhost';
GRANT SELECT ON achados_perdidos.reivindicacoes_por_retirante TO 'achados_aluno'@'localhost';

FLUSH PRIVILEGES;

-- SHOW GRANTS FOR 'app_achados'@'localhost';
-- SHOW GRANTS FOR 'achados_admin'@'localhost';
-- SHOW GRANTS FOR 'achados_funcionario'@'localhost';
-- SHOW GRANTS FOR 'achados_aluno'@'localhost';

DELIMITER $$

-- protocolo legivel da devolucao, ex: DEV-2026-000001
CREATE FUNCTION fn_gerar_protocolo_devolucao(p_id_devolucao INT)
RETURNS VARCHAR(20)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE ano_devolucao INT;
    SELECT YEAR(Data_Devolucao) INTO ano_devolucao FROM Devolucao WHERE ID_Devolucao = p_id_devolucao;
    RETURN CONCAT('DEV-', ano_devolucao, '-', LPAD(p_id_devolucao, 6, '0'));
END $$

-- protocolo legivel da reivindicacao, ex: REIV-2026-000045
CREATE FUNCTION fn_gerar_protocolo_reivindicacao(p_id_reivindicacao INT)
RETURNS VARCHAR(20)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE ano_reivindicacao INT;
    SELECT YEAR(Data_Reivindicacao) INTO ano_reivindicacao FROM Reivindicacao WHERE ID_Reivindicacao = p_id_reivindicacao;
    RETURN CONCAT('REIV-', ano_reivindicacao, '-', LPAD(p_id_reivindicacao, 6, '0'));
END $$

DELIMITER ;

GRANT EXECUTE ON FUNCTION achados_perdidos.fn_gerar_protocolo_devolucao TO 'app_achados'@'localhost';
GRANT EXECUTE ON FUNCTION achados_perdidos.fn_gerar_protocolo_devolucao TO 'achados_admin'@'localhost';
GRANT EXECUTE ON FUNCTION achados_perdidos.fn_gerar_protocolo_devolucao TO 'achados_funcionario'@'localhost';
GRANT EXECUTE ON FUNCTION achados_perdidos.fn_gerar_protocolo_reivindicacao TO 'app_achados'@'localhost';
GRANT EXECUTE ON FUNCTION achados_perdidos.fn_gerar_protocolo_reivindicacao TO 'achados_admin'@'localhost';
GRANT EXECUTE ON FUNCTION achados_perdidos.fn_gerar_protocolo_reivindicacao TO 'achados_aluno'@'localhost';
GRANT EXECUTE ON PROCEDURE achados_perdidos.sp_relatorio_objetos TO 'app_achados'@'localhost';
GRANT EXECUTE ON PROCEDURE achados_perdidos.sp_relatorio_objetos TO 'achados_admin'@'localhost';
GRANT EXECUTE ON PROCEDURE achados_perdidos.sp_relatorio_objetos TO 'achados_funcionario'@'localhost';
GRANT EXECUTE ON PROCEDURE achados_perdidos.sp_relatorio_reivindicacoes TO 'app_achados'@'localhost';
GRANT EXECUTE ON PROCEDURE achados_perdidos.sp_relatorio_reivindicacoes TO 'achados_admin'@'localhost';
GRANT EXECUTE ON PROCEDURE achados_perdidos.sp_relatorio_reivindicacoes TO 'achados_funcionario'@'localhost';

FLUSH PRIVILEGES;

-- =============================================================
-- GERACAO DE IDS: POR QUE USAMOS AUTO_INCREMENT
-- =============================================================
-- Onde e usado: chaves primarias tecnicas (sem significado de negocio)
--   Usuario, Funcionario, Administrador, Objeto, Pergunta_Seguranca,
--   Retirante, Reivindicacao, Devolucao, Log_Auditoria, grupos_usuarios.
--
-- Por que:
--   1. O InnoDB gera o valor de forma atomica a cada INSERT. Varias
--      conexoes (aluno, funcionario, admin) inserindo ao mesmo tempo
--      nunca recebem o mesmo ID. Uma funcao do tipo MAX(id) + 1 teria
--      condicao de corrida: duas conexoes poderiam calcular o mesmo valor.
--   2. INT de 4 bytes e sempre crescente: o InnoDB organiza a tabela pela
--      PK (indice clusterizado), entao cada linha nova entra no fim da
--      arvore B, sem reorganizar paginas. As FKs e os JOINs ficam leves.
--   3. Esses IDs nunca sao mostrados nem digitados pelo usuario, entao
--      nao precisam carregar nenhuma regra de negocio.
--
-- Dados criticos (reivindicacao e devolucao, que comprovam posse e
-- entrega) tem regra propria de identificacao, feita pelas functions
-- fn_gerar_protocolo_reivindicacao e fn_gerar_protocolo_devolucao:
--   prefixo do tipo + ano do registro + ID com 6 digitos
--   ex: REIV-2026-000045 e DEV-2026-000001.
-- Esse protocolo e o identificador que o aluno recebe e informa no
-- balcao. Como e derivado do ID e da data, e unico, sempre igual para o
-- mesmo registro e nao precisa de coluna extra.
--
-- Onde NAO usamos AUTO_INCREMENT, de proposito:
--   - Foto: PK composta (ID_Objeto, Numero_Foto), a numeracao recomeca
--     em cada objeto (entidade fraca).
--   - rel_usuario_grupo: PK composta pelas duas FKs (id_usuario, id_grupo).
--
-- CPF, Email_Institucional e Documento_Identificacao sao UNIQUE (chaves
-- candidatas, impedem duplicidade), mas nao viraram PK: sao dados
-- pessoais (LGPD), podem ser corrigidos e sao textos maiores que um INT.
