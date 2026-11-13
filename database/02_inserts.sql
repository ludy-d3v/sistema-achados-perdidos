-- =============================================================
-- Dados de exemplo para o banco achados_perdidos
-- Rode DEPOIS do 01_banco.sql e ANTES do 07_triggers.sql
-- (a trigger de reivindicação forçaria todos os status para Pendente).
-- Senha de todos os usuários: 123
--
-- @p.ucb.br = professor/funcionário  -> tela de funcionários
-- @a.ucb.br = aluno                  -> tela de alunos
-- Exceção: Juliana e Gabriel são alunos estagiários (estão na tabela
-- Funcionario), então entram pela tela de funcionários.
--
-- Logins de administrador para teste:
-- admin.sistema@p.ucb.br / 123   -> só administrador (não é funcionário,
--                                   vai direto pra tela de Gerenciar Funcionários)
-- admin.func@p.ucb.br    / 123   -> administrador E funcionário (TI), o login
--                                   pergunta qual tela abrir
-- =============================================================
USE achados_perdidos;

-- -------------------------------------------------------------
-- LIMPA AS TABELAS (e zera os IDs)
-- -------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Log_Auditoria;
TRUNCATE TABLE Devolucao;
TRUNCATE TABLE Reivindicacao;
TRUNCATE TABLE Pergunta_Seguranca;
TRUNCATE TABLE Foto;
TRUNCATE TABLE Objeto;
TRUNCATE TABLE Retirante;
TRUNCATE TABLE Administrador;
TRUNCATE TABLE Funcionario;
TRUNCATE TABLE Usuario;
SET FOREIGN_KEY_CHECKS = 1;

-- -------------------------------------------------------------
-- USUÁRIOS
-- -------------------------------------------------------------
INSERT INTO Usuario (ID_Usuario, CPF, Nome, Email_Institucional, Senha) VALUES
-- professores / funcionários (@p.ucb.br)
(1,  '111.111.111-11', 'Ana Souza',            'ana.souza@p.ucb.br',      '123'),
(2,  '222.222.222-22', 'Bruno Lima',           'bruno.lima@p.ucb.br',     '123'),
(3,  '333.333.333-33', 'Carla Mendes',         'carla.mendes@p.ucb.br',   '123'),
(4,  '444.444.444-44', 'Diego Rocha',          'diego.rocha@p.ucb.br',    '123'),
-- alunos (@a.ucb.br)
(5,  '666.666.666-66', 'Mariana Alves',        'mariana.alves@a.ucb.br',  '123'),
(6,  '777.777.777-77', 'Pedro Henrique Silva', 'pedro.silva@a.ucb.br',    '123'),
(7,  '888.888.888-88', 'Juliana Ferreira',     'juliana.ferreira@a.ucb.br','123'),  -- estagiária
(8,  '999.999.999-99', 'Rafael Gomes',         'rafael.gomes@a.ucb.br',   '123'),
(9,  '101.101.101-10', 'Larissa Nunes',        'larissa.nunes@a.ucb.br',  '123'),
(10, '121.212.121-21', 'Gabriel Teixeira',     'gabriel.teixeira@a.ucb.br','123'),  -- estagiário
-- novos alunos
(13, '131.313.131-13', 'Camila Rodrigues',     'camila.rodrigues@a.ucb.br','123'),
(14, '141.414.141-14', 'Thiago Martins',       'thiago.martins@a.ucb.br', '123'),
-- nova funcionária comum
(15, '151.515.151-15', 'Beatriz Lopes',        'beatriz.lopes@p.ucb.br',  '123'),
-- contas de administrador (teste)
(11, '111.222.333-44', 'Admin Sistema',        'admin.sistema@p.ucb.br',  '123'),  -- só admin
(12, '222.333.444-55', 'Rogerio Castro',       'admin.func@p.ucb.br',    '123');  -- admin + funcionário

-- Funcionários: professores/funcionários + os 2 alunos estagiários + a nova funcionária + o admin que também é funcionário
INSERT INTO Funcionario (ID_Funcionario, Cargo, Setor, ID_Usuario) VALUES
(1, 'Porteira',               'Portaria',                1),
(2, 'Bibliotecário',          'Biblioteca',              2),
(3, 'Professora',             'Engenharia de Software',  3),
(4, 'Técnico de laboratório', 'Laboratórios',            4),
(5, 'Estagiária',             'Secretaria',              7),
(6, 'Estagiário',             'Biblioteca',              10),
(7, 'Coordenadora',           'Secretaria Acadêmica',    15),
(8, 'Administrador de Sistemas', 'TI',                   12);

-- Administradores: Admin Sistema (só admin) e Rogerio Castro (admin + funcionário).
INSERT INTO Administrador (ID_Administrador, Nivel_Acesso, ID_Usuario) VALUES
(1, 'Total', 11),
(2, 'Total', 12);

-- Alunos 5, 6, 8 e 9 não têm cargo: não aparecem em Funcionario nem em Administrador.

-- -------------------------------------------------------------
-- RETIRANTES
-- O sistema liga o aluno ao retirante pelo CPF
-- (Documento_Identificacao = CPF do Usuario).
-- -------------------------------------------------------------
INSERT INTO Retirante (ID_Retirante, Nome_Completo, Documento_Identificacao, Telefone) VALUES
(1, 'Mariana Alves',        '666.666.666-66',  '(61) 98111-0001'),
(2, 'Pedro Henrique Silva', '777.777.777-77',  '(61) 98111-0002'),
(3, 'Rafael Gomes',         '999.999.999-99',  '(61) 98111-0003'),
(4, 'Larissa Nunes',        '101.101.101-10',  NULL),
(5, 'Roberto Dias',         'RG 1.234.567 DF', '(61) 98111-0005'),  -- visitante, não é usuário
(6, 'Camila Rodrigues',     '131.313.131-13',  '(61) 98111-0006'),
(7, 'Thiago Martins',       '141.414.141-14',  NULL);

-- -------------------------------------------------------------
-- OBJETOS (cadastrados só por funcionários/estagiários)
-- prazo de guarda = data do achado + 90 dias
-- -------------------------------------------------------------
INSERT INTO Objeto (ID_Objeto, Descricao_Generica, Detalhes_Sensiveis, Categoria, Data_Achado, Hora_Achado,
                    Campus, Bloco, Ambiente, Status_Atual, Data_Limite_Guarda, Data_Cadastro, Hora_Cadastro, ID_Usuario) VALUES
(1,  'Garrafa térmica azul de 500 ml', 'Adesivo da NASA na tampa, nome "Ana" embaixo', 'Utensílios',
     '2026-10-30', '14:30', 'Taguatinga-DF', 'Bloco M', 'Sala M-205', 'Disponivel',
     '2027-01-28', '2026-10-30', '15:00', 1),
(2,  'Celular preto com capinha transparente', 'Papel de parede com foto de cachorro, tela trincada no canto', 'Eletrônicos',
     '2026-10-20', '09:15', 'Taguatinga-DF', 'Bloco K', 'Biblioteca - 2º andar', 'Devolvido',
     '2027-01-18', '2026-10-20', '09:40', 2),
(3,  'Carteira de couro marrom', 'Contém carteirinha de estudante e R$ 20,00', 'Documentos',
     '2026-10-25', '18:40', 'Taguatinga-DF', NULL, 'Estacionamento principal', 'Em analise de posse',
     '2027-01-23', '2026-10-25', '19:00', 1),
(4,  'Moletom cinza com capuz', 'Tamanho M, chaveiro preso no zíper', 'Roupas',
     '2026-07-10', '11:00', 'Asa sul-DF', 'Bloco A', 'Quadra poliesportiva', 'Encaminhado para doacao',
     '2026-10-08', '2026-07-10', '11:30', 3),
(5,  'Fone de ouvido sem fio branco', 'Estojo com risco na lateral e iniciais "P.H."', 'Eletrônicos',
     '2026-10-28', '16:05', 'Taguatinga-DF', 'Bloco G', 'Laboratório de Informática G-12', 'Devolvido',
     '2027-01-26', '2026-10-28', '16:20', 4),
(6,  'Chaveiro com 3 chaves', 'Pingente de Torre Eiffel, uma chave de cadeado', 'Chaves',
     '2026-10-29', '07:50', 'Taguatinga-DF', 'Bloco C', 'Cantina', 'Disponivel',
     '2027-01-27', '2026-10-29', '08:10', 7),
(7,  'Calculadora científica', 'Modelo Casio fx-82, nome raspado na tampa', 'Material escolar',
     '2026-10-15', '10:20', 'Asa sul-DF', 'Bloco B', 'Sala B-101', 'Disponivel',
     '2027-01-13', '2026-10-15', '10:45', 3),
(8,  'Óculos de grau com armação preta', 'Estojo vermelho da ótica Visão Total', 'Acessórios',
     '2026-10-22', '13:10', 'Taguatinga-DF', 'Bloco K', 'Biblioteca - sala de estudos', 'Em analise de posse',
     '2027-01-20', '2026-10-22', '13:30', 10),
(9,  'Mochila preta', 'Contém caderno de Cálculo I e carregador de notebook', 'Bolsas e mochilas',
     '2026-10-27', '21:30', 'Taguatinga-DF', 'Bloco M', 'Auditório', 'Disponivel',
     '2027-01-25', '2026-10-27', '21:45', 1),
(10, 'Guarda-chuva verde', NULL, 'Acessórios',
     '2026-06-30', '17:00', 'Asa sul-DF', NULL, 'Recepção', 'Encaminhado para doacao',
     '2026-09-28', '2026-06-30', '17:15', 3),
(11, 'Caderno universitário capa dura', 'Rabiscos de fórmulas de física na capa', 'Material escolar',
     '2026-08-05', '08:40', 'Asa Norte-DF', 'Bloco D', 'Sala D-310', 'Devolvido',
     '2026-11-03', '2026-08-05', '09:00', 15),
(12, 'Relógio de pulso prateado', 'Pulseira com elo quebrado, mostrador riscado', 'Acessórios',
     '2026-09-12', '12:15', 'Asa Norte-DF', 'Bloco A', 'Quadra poliesportiva', 'Disponivel',
     '2026-12-11', '2026-09-12', '12:40', 4),
(13, 'Livro de Cálculo II usado', 'Nome "Thiago" escrito na primeira página', 'Livros',
     '2026-10-18', '17:20', 'Taguatinga-DF', 'Bloco M', 'Auditório', 'Disponivel',
     '2027-01-16', '2026-10-18', '17:45', 1);

-- -------------------------------------------------------------
-- FOTOS (todo objeto tem pelo menos uma; numeradas por objeto)
-- -------------------------------------------------------------
INSERT INTO Foto (ID_Objeto, Numero_Foto, Caminho_Foto, Descricao) VALUES
(1,  1, 'fotos/garrafa_1.jpg',     'Vista frontal'),
(1,  2, 'fotos/garrafa_2.jpg',     'Tampa com adesivo'),
(2,  1, 'fotos/celular_1.jpg',     'Capinha transparente'),
(3,  1, 'fotos/carteira_1.jpg',    'Carteira fechada'),
(4,  1, 'fotos/moletom_1.jpg',     NULL),
(5,  1, 'fotos/fone_1.jpg',        'Estojo fechado'),
(5,  2, 'fotos/fone_2.jpg',        'Detalhe das iniciais'),
(6,  1, 'fotos/chaves_1.jpg',      'Chaveiro completo'),
(7,  1, 'fotos/calculadora_1.jpg', NULL),
(8,  1, 'fotos/oculos_1.jpg',      'Óculos dentro do estojo'),
(9,  1, 'fotos/mochila_1.jpg',     'Mochila fechada'),
(10, 1, 'fotos/guardachuva_1.jpg', NULL),
(11, 1, 'fotos/caderno_1.jpg',     'Capa com rabiscos'),
(12, 1, 'fotos/relogio_1.jpg',     'Mostrador riscado'),
(13, 1, 'fotos/livro_calculo_1.jpg', NULL);

-- -------------------------------------------------------------
-- PERGUNTAS DE SEGURANÇA (pelo menos uma por objeto)
-- -------------------------------------------------------------
INSERT INTO Pergunta_Seguranca (ID_Pergunta, Texto_Pergunta, Resposta_Esperada, ID_Objeto) VALUES
(1,  'Qual adesivo tem na tampa?',                'NASA',         1),
(2,  'Que nome está escrito embaixo da garrafa?', 'Ana',          1),
(3,  'Qual é a foto do papel de parede?',         'Cachorro',     2),
(4,  'Quanto dinheiro havia na carteira?',        '20 reais',     3),
(5,  'Qual é o tamanho do moletom?',              'M',            4),
(6,  'Quais iniciais estão no estojo?',           'P.H.',         5),
(7,  'Qual é o pingente do chaveiro?',            'Torre Eiffel', 6),
(8,  'Qual é o modelo da calculadora?',           'fx-82',        7),
(9,  'De qual ótica é o estojo?',                 'Visão Total',  8),
(10, 'Qual caderno estava dentro da mochila?',    'Cálculo I',    9),
(11, 'Qual é a cor do guarda-chuva?',             'Verde',        10),
(12, 'Qual matéria está rabiscada na capa?',      'Física',       11),
(13, 'Como está a pulseira do relógio?',          'Quebrada',     12),
(14, 'Que nome está escrito no livro?',           'Thiago',       13);

-- -------------------------------------------------------------
-- REIVINDICAÇÕES (um objeto pode ter várias)
-- -------------------------------------------------------------
INSERT INTO Reivindicacao (ID_Reivindicacao, Data_Reivindicacao, Status_Reivindicacao, Observacao, ID_Retirante, ID_Objeto) VALUES
(1, '2026-10-21 10:00:00', 'Aprovada',   'Acertou a pergunta de segurança',        1, 2),
(2, '2026-10-21 15:30:00', 'Recusada',   'Não soube descrever o papel de parede',  3, 2),
(3, '2026-10-29 08:30:00', 'Aprovada',   'Iniciais conferem com o documento',      2, 5),
(4, '2026-10-26 09:00:00', 'Em analise', 'Aguardando conferência da carteirinha',  4, 3),
(5, '2026-10-23 14:00:00', 'Em analise', NULL,                                     5, 8),
(6, '2026-10-31 11:20:00', 'Pendente',   'Disse que perdeu na cantina',            1, 6),
(7, '2026-10-31 16:45:00', 'Pendente',   NULL,                                     3, 9),
(8, '2026-08-06 09:00:00', 'Aprovada',   'Reconheceu os rabiscos de física',       7, 11),
(9, '2026-10-19 08:00:00', 'Pendente',   NULL,                                     6, 13),
(10,'2026-09-13 10:00:00', 'Recusada',   'Não soube explicar a pulseira quebrada', 6, 12);

-- -------------------------------------------------------------
-- DEVOLUÇÕES (só a partir de reivindicação aprovada: 1 e 3)
-- -------------------------------------------------------------
INSERT INTO Devolucao (ID_Devolucao, ID_Objeto, ID_Retirante, ID_Usuario, ID_Reivindicacao, Data_Devolucao, Assinatura_Digital) VALUES
(1, 2, 1, 2, 1, '2026-10-21 10:15:00', 'assinaturas/devolucao_celular_mariana.png'),
(2, 5, 2, 4, 3, '2026-10-29 08:45:00', 'assinaturas/devolucao_fone_pedro.png'),
(3, 11, 7, 15, 8, '2026-08-06 09:15:00', 'assinaturas/devolucao_caderno_thiago.png');

-- -------------------------------------------------------------
-- LOG DE AUDITORIA
-- -------------------------------------------------------------
INSERT INTO Log_Auditoria (ID_Log, Acao_Realizada, Data_Hora_Acao, ID_Usuario) VALUES
(1,  'Cadastro de objeto: Guarda-chuva verde',             '2026-06-30 17:15:00', 3),
(2,  'Cadastro de objeto: Moletom cinza com capuz',        '2026-07-10 11:30:00', 3),
(3,  'Cadastro de objeto: Celular preto',                  '2026-10-20 09:40:00', 2),
(4,  'Reivindicação aprovada: Celular preto',              '2026-10-21 10:05:00', 2),
(5,  'Devolução registrada: Celular preto',                '2026-10-21 10:15:00', 2),
(6,  'Reivindicação recusada: Celular preto',              '2026-10-21 15:35:00', 2),
(7,  'Cadastro de objeto: Óculos de grau',                 '2026-10-22 13:30:00', 10),
(8,  'Cadastro de objeto: Fone de ouvido sem fio',         '2026-10-28 16:20:00', 4),
(9,  'Devolução registrada: Fone de ouvido sem fio',       '2026-10-29 08:45:00', 4),
(10, 'Cadastro de objeto: Chaveiro com 3 chaves',          '2026-10-29 08:10:00', 7),
(11, 'Cadastro de objeto: Garrafa térmica azul',           '2026-10-30 15:00:00', 1),
(12, 'Cadastro de objeto: Caderno universitário capa dura','2026-08-05 09:00:00', 15),
(13, 'Reivindicação aprovada: Caderno universitário',      '2026-08-06 09:05:00', 15),
(14, 'Devolução registrada: Caderno universitário',        '2026-08-06 09:15:00', 15),
(15, 'Cadastro de objeto: Relógio de pulso prateado',       '2026-09-12 12:40:00', 4),
(16, 'Reivindicação recusada: Relógio de pulso prateado',   '2026-09-13 10:05:00', 4),
(17, 'Cadastro de objeto: Livro de Cálculo II usado',       '2026-10-18 17:45:00', 1);
