CREATE DATABASE IF NOT EXISTS achados_perdidos;
USE achados_perdidos;

-- TABELA: USUARIO
CREATE TABLE Usuario (
    ID_Usuario INT AUTO_INCREMENT PRIMARY KEY,
    CPF VARCHAR(14) NOT NULL UNIQUE,
    Nome VARCHAR(120) NOT NULL,
    Email_Institucional VARCHAR(120) NOT NULL UNIQUE,
    Senha VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- TABELA: FUNCIONARIO
CREATE TABLE Funcionario (
    ID_Funcionario INT AUTO_INCREMENT PRIMARY KEY,
    Cargo VARCHAR(60) NOT NULL,
    Setor VARCHAR(60) NOT NULL,
    ID_Usuario INT NOT NULL UNIQUE,
    CONSTRAINT fk_funcionario_usuario FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
) ENGINE=InnoDB;

-- TABELA: ADMINISTRADOR
CREATE TABLE Administrador (
    ID_Administrador INT AUTO_INCREMENT PRIMARY KEY,
    Nivel_Acesso VARCHAR(30) NOT NULL,
    ID_Usuario INT NOT NULL UNIQUE,
    CONSTRAINT fk_administrador_usuario FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
) ENGINE=InnoDB;

-- TABELA: OBJETO
CREATE TABLE Objeto (
    ID_Objeto INT AUTO_INCREMENT PRIMARY KEY,
    Descricao_Generica VARCHAR(150) NOT NULL,
    Detalhes_Sensiveis VARCHAR(255),
    Categoria VARCHAR(50) NOT NULL,
    Data_Achado DATE NOT NULL,
    Hora_Achado TIME NOT NULL,
    Campus VARCHAR(80) NOT NULL,
    Bloco VARCHAR(50),
    Ambiente VARCHAR(100) NOT NULL,
    Status_Atual ENUM(
        'Disponivel',
        'Em analise de posse',
        'Devolvido',
        'Encaminhado para doacao'
    ) NOT NULL DEFAULT 'Disponivel',
    Data_Limite_Guarda DATE NOT NULL,
    Data_Cadastro DATE NOT NULL,
    Hora_Cadastro TIME NOT NULL,
    ID_Usuario INT NOT NULL,
    CONSTRAINT fk_objeto_usuario FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
) ENGINE=InnoDB;

-- TABELA: FOTO

CREATE TABLE Foto (
    ID_Objeto INT NOT NULL,
    Numero_Foto INT NOT NULL,
    Caminho_Foto VARCHAR(255) NOT NULL,
    Descricao VARCHAR(150),
    PRIMARY KEY (ID_Objeto, Numero_Foto),
    CONSTRAINT fk_foto_objeto FOREIGN KEY (ID_Objeto) REFERENCES Objeto(ID_Objeto)
) ENGINE=InnoDB;

-- TABELA: PERGUNTA_SEGURANCA
CREATE TABLE Pergunta_Seguranca (
    ID_Pergunta INT AUTO_INCREMENT PRIMARY KEY,
    Texto_Pergunta VARCHAR(200) NOT NULL,
    Resposta_Esperada VARCHAR(150) NOT NULL,
    ID_Objeto INT NOT NULL,
    CONSTRAINT fk_pergunta_objeto FOREIGN KEY (ID_Objeto) REFERENCES Objeto(ID_Objeto)
) ENGINE=InnoDB;

-- TABELA: RETIRANTE
CREATE TABLE Retirante (
    ID_Retirante INT AUTO_INCREMENT PRIMARY KEY,
    Nome_Completo VARCHAR(120) NOT NULL,
    Documento_Identificacao VARCHAR(20) NOT NULL UNIQUE,
    Telefone VARCHAR(20)
) ENGINE=InnoDB;

-- TABELA: REIVINDICACAO
CREATE TABLE Reivindicacao (
    ID_Reivindicacao INT AUTO_INCREMENT PRIMARY KEY,
    Data_Reivindicacao DATETIME NOT NULL,
    Status_Reivindicacao ENUM(
        'Pendente',
        'Em analise',
        'Aprovada',
        'Recusada'
    ) NOT NULL DEFAULT 'Pendente',
    Observacao VARCHAR(255),
    ID_Retirante INT NOT NULL,
    ID_Objeto INT NOT NULL,
    CONSTRAINT fk_reivindicacao_retirante FOREIGN KEY (ID_Retirante) REFERENCES Retirante(ID_Retirante),
    CONSTRAINT fk_reivindicacao_objeto FOREIGN KEY (ID_Objeto) REFERENCES Objeto(ID_Objeto)
) ENGINE=InnoDB;


-- TABELA: DEVOLUCAO

CREATE TABLE Devolucao (
    ID_Devolucao INT AUTO_INCREMENT PRIMARY KEY,
    ID_Objeto INT NOT NULL,
    ID_Retirante INT NOT NULL,
    ID_Usuario INT NOT NULL,
    ID_Reivindicacao INT NOT NULL UNIQUE,
    Data_Devolucao DATETIME NOT NULL,
    Assinatura_Digital VARCHAR(255) NOT NULL,
    CONSTRAINT fk_devolucao_objeto FOREIGN KEY (ID_Objeto) REFERENCES Objeto(ID_Objeto),
    CONSTRAINT fk_devolucao_retirante FOREIGN KEY (ID_Retirante) REFERENCES Retirante(ID_Retirante),
    CONSTRAINT fk_devolucao_usuario FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario),
    CONSTRAINT fk_devolucao_reivindicacao FOREIGN KEY (ID_Reivindicacao) REFERENCES Reivindicacao(ID_Reivindicacao)
) ENGINE=InnoDB;

-- TABELA: LOG_AUDITORIA
CREATE TABLE Log_Auditoria (
    ID_Log INT AUTO_INCREMENT PRIMARY KEY,
    Acao_Realizada VARCHAR(150) NOT NULL,
    Data_Hora_Acao DATETIME NOT NULL,
    ID_Usuario INT NOT NULL,
    CONSTRAINT fk_log_usuario FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
) ENGINE=InnoDB;