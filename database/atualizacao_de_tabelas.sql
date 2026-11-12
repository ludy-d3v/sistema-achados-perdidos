
-- criando a tabela de grupos usuarios (suas funções)
CREATE TABLE grupos_usuarios (
    id_grupo INT AUTO_INCREMENT PRIMARY KEY,
    nome_grupo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(150)
)  ENGINE=INNODB;


-- criando a tabela de relação entre usuarios e grupos
CREATE TABLE rel_usuario_grupo (
    id_usuario INT NOT NULL,
    id_grupo INT NOT NULL,

    PRIMARY KEY (id_usuario, id_grupo),

    CONSTRAINT fk_ug_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES Usuario(ID_Usuario),


    CONSTRAINT fk_ug_grupo
        FOREIGN KEY (id_grupo)
        REFERENCES grupos_usuarios(id_grupo)

) ENGINE=InnoDB;


-- inserindo os grupos de usuarios (funcionario, admin e aluno)
INSERT INTO grupos_usuarios (nome_grupo, descricao)
VALUES
('Administrador', 'Gerenciamento completo do sistema'),
('Funcionario', 'Cadastro e gerenciamento de objetos'),
('Aluno', 'Consulta e reivindicação de objetos');


-- Fazendo a relação dos usuarios, dividindo-os entre os grupos de admin, funcionario e aluno

-- relação dos admins
INSERT INTO rel_usuario_grupo (id_usuario, id_grupo)
SELECT ID_Usuario, 1
FROM Administrador;

-- relação dos funcionarios
INSERT INTO rel_usuario_grupo (id_usuario, id_grupo)
SELECT ID_Usuario, 2
FROM Funcionario;

-- relacionando todos os outros como alunos
INSERT INTO rel_usuario_grupo (id_usuario, id_grupo)
SELECT u.ID_Usuario, 3
FROM Usuario u
WHERE NOT EXISTS (
    SELECT 1
    FROM Funcionario f
    WHERE f.ID_Usuario = u.ID_Usuario
)
AND NOT EXISTS (
    SELECT 1
    FROM Administrador a
    WHERE a.ID_Usuario = u.ID_Usuario
);