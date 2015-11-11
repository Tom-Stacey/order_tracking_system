use mydb;

DELIMITER //
CREATE PROCEDURE update_location_volume_stored
(IN volumeToStore INT, locationID INT)
BEGIN
	UPDATE stock SET quantity = quantity + newQuantity WHERE itemID = idItem AND idLocation = locationID;
END //
DELIMITER ;