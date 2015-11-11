use mydb;

DELIMITER //
CREATE PROCEDURE add_or_update_stock
(IN idItem INT, newQuantity INT, locationID INT)
BEGIN
	IF EXISTS (SELECT * FROM stock WHERE itemID = idItem AND idLocation = locationID) THEN
		UPDATE stock SET quantity = quantity + newQuantity WHERE itemID = idItem AND idLocation = locationID;
	ELSE 
		INSERT INTO stock (itemID, idLocation, quantity, quantityClaimed) values (idItem, locationID, newQuantity, 0);
	END IF;

END //
DELIMITER ;