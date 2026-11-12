use achados_perdidos;

-- Criação da view "reivindicacoes_por_retirante" para ajudar na consulta, basicamente não precisa-se mais utilizar inner join na consulta
DROP VIEW IF EXISTS reivindicacoes_por_retirante;
CREATE VIEW reivindicacoes_por_retirante AS
SELECT
    r.ID_Reivindicacao, r.ID_Retirante,
    r.Data_Reivindicacao,r.Status_Reivindicacao,
    o.Descricao_Generica, o.Categoria, o.Campus
FROM Reivindicacao r
INNER JOIN Objeto o
    ON r.ID_Objeto = o.ID_Objeto;
    
    
    
  -- Criação da view "consultar_reivindicacoes" com o mesmo proposito da outra
    DROP VIEW IF EXISTS consultar_reivindicacoes;
    CREATE VIEW consultar_reivindicacoes AS
SELECT
    r.ID_Reivindicacao,r.Data_Reivindicacao,r.Status_Reivindicacao,
    r.Observacao,r.ID_Objeto,r.ID_Retirante,
    ret.Nome_Completo,ret.Documento_Identificacao,
    o.Descricao_Generica,o.Categoria, o.Campus,o.Bloco,o.Ambiente
FROM Reivindicacao r
INNER JOIN Retirante ret
    ON r.ID_Retirante = ret.ID_Retirante
INNER JOIN Objeto o
    ON r.ID_Objeto = o.ID_Objeto;

    
    -- Vale ressaltar que as views criadas possuem os mesmos nomes dos metodos do netbeans
    -- Entretanto a view "consultar_reivindicacoes" também é utilizada pelo método "buscarIdReivindicacao", pois é melhor para pesquisar