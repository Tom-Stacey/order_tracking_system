use mydb;

DELIMITER //
CREATE PROCEDURE update_location_volume_stored
(IN volumeToStore INT, locationID INT)
BEGIN
	UPDATE location SET locationLtrVolumeUsed = locationLtrVolumeUsed + volumeToStore WHERE idLocation = locationID;
END //
DELIMITER ;