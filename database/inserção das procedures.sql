-- Recebe as datas inicial e final.
-- Verifica se ambas foram informadas ou se ambas estão vazias.
-- Retorna a quantidade total de objetos.
-- Agrupa os objetos por categoria.
-- Agrupa os objetos por campus.
-- Agrupa os objetos por mês, em ordem cronológica.

USE achados_perdidos;

DELIMITER //

CREATE PROCEDURE sp_relatorio_objetos(
    IN p_data_inicial DATE,
    IN p_data_final DATE
)
BEGIN

    -- Verifica se as duas datas foram informadas corretamente
    IF (p_data_inicial IS NULL AND p_data_final IS NOT NULL)
       OR (p_data_inicial IS NOT NULL AND p_data_final IS NULL) THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Informe as duas datas ou deixe ambas vazias.';

    END IF;

    -- 1. Total de objetos
    SELECT COUNT(*) AS total
    FROM Objeto
    WHERE
        (p_data_inicial IS NULL OR Data_Cadastro >= p_data_inicial)
        AND
        (p_data_final IS NULL OR Data_Cadastro <= p_data_final);

    -- 2. Objetos por categoria
    SELECT Categoria, COUNT(*) AS total
    FROM Objeto
    WHERE
        (p_data_inicial IS NULL OR Data_Cadastro >= p_data_inicial)
        AND
        (p_data_final IS NULL OR Data_Cadastro <= p_data_final)
    GROUP BY Categoria
    ORDER BY Categoria;

    -- 3. Objetos por campus
    SELECT Campus, COUNT(*) AS total
    FROM Objeto
    WHERE
        (p_data_inicial IS NULL OR Data_Cadastro >= p_data_inicial)
        AND
        (p_data_final IS NULL OR Data_Cadastro <= p_data_final)
    GROUP BY Campus
    ORDER BY Campus;

    -- 4. Objetos por mês
    SELECT
        DATE_FORMAT(Data_Cadastro, '%m/%Y') AS mes,
        DATE_FORMAT(Data_Cadastro, '%Y-%m') AS ordem,
        COUNT(*) AS total
    FROM Objeto
    WHERE
        (p_data_inicial IS NULL OR Data_Cadastro >= p_data_inicial)
        AND
        (p_data_final IS NULL OR Data_Cadastro <= p_data_final)
    GROUP BY ordem, mes
    ORDER BY ordem;

END //

DELIMITER ;

-- Recebe as datas inicial e final.
-- Verifica se ambas foram informadas ou se ambas estão vazias.
-- Retorna o total de reivindicações solicitadas.
-- Retorna os totais de reivindicações aprovadas, recusadas e pendentes.

DELIMITER //

CREATE PROCEDURE sp_relatorio_reivindicacoes(
    IN p_data_inicial DATE,
    IN p_data_final DATE
)
BEGIN

    -- Verifica se as duas datas foram informadas corretamente
    IF (p_data_inicial IS NULL AND p_data_final IS NOT NULL)
       OR (p_data_inicial IS NOT NULL AND p_data_final IS NULL) THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Informe as duas datas ou deixe ambas vazias.';

    END IF;

    -- Relatório geral de reivindicações
    SELECT
        COUNT(*) AS solicitadas,

        SUM(CASE
            WHEN Status_Reivindicacao = 'Aprovada' THEN 1
            ELSE 0
        END) AS aprovadas,

        SUM(CASE
            WHEN Status_Reivindicacao = 'Recusada' THEN 1
            ELSE 0
        END) AS recusadas,

        SUM(CASE
            WHEN Status_Reivindicacao = 'Pendente' THEN 1
            ELSE 0
        END) AS pendentes

    FROM Reivindicacao

    WHERE
        (p_data_inicial IS NULL OR DATE(Data_Reivindicacao) >= p_data_inicial)
        AND
        (p_data_final IS NULL OR DATE(Data_Reivindicacao) <= p_data_final);

END //

DELIMITER ;