use achados_perdidos;

CREATE INDEX idx_objeto_categoria_data -- Esse indice foi criado para nos auxiliar á filtrar  a busca por objetos na pagina de consultar objetos e procurar objetos
ON Objeto (Categoria, Data_Achado); -- Ele pode ser utilizado tanto para buscar pelos 2 indices (categoria / data_achado) quanto apenas por categoria

CREATE INDEX idx_objeto_data_achado -- Esse foi criado para buscar apenas a "data_achado" porque primeiro não serviria
ON Objeto (Data_Achado); -- Pois o MySQL consegue aproveitar a primeira coluna de um índice composto para fazer uma busca isolada, mas o segundo não


-- Comando para analisar a busca
EXPLAIN 
SELECT ID_Objeto, Descricao_Generica, Categoria, Campus, Status_Atual, Data_Achado FROM Objeto
WHERE Categoria = 'Eletrônicos'
AND Data_Achado >= '2026-10-01'
AND Data_Achado <= '2026-10-31'
ORDER BY Data_Achado DESC;