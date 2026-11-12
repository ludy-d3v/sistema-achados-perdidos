-- sempre que uma nova reivindicação for cadastrada, o MySQL define automaticamente o campo
-- Status_Reivindicacao como Pendente, independentemente do valor enviado pelo Java.

DELIMITER //

CREATE TRIGGER trg_reivindicacao_pendente
BEFORE INSERT ON Reivindicacao
FOR EACH ROW
BEGIN
    SET NEW.Status_Reivindicacao = 'Pendente';
END //

DELIMITER ;

-- Atualizar automaticamente o status de um objeto para Devolvido 
-- assim que uma devolução for cadastrada no banco de dados.

DELIMITER //

CREATE TRIGGER trg_objeto_devolvido
AFTER INSERT ON Devolucao
FOR EACH ROW
BEGIN
    UPDATE Objeto
    SET Status_Atual = 'Devolvido'
    WHERE ID_Objeto = NEW.ID_Objeto;
END //

DELIMITER ;