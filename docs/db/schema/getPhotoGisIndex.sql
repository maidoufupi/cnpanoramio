-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getPhotoGisIndex`(in swLat double,
                                     in swLng double,
									 in neLat double,
                                     in neLng double,
                                     in zoomLevel int(11),
                                     in width int4,
								     in height int4)
BEGIN
	DECLARE done_photo INT DEFAULT false;
	declare lat1 double;
	declare lng1 double;
	declare zoomLevel1 int(11);
	declare photoId1 bigint(20);
	declare lat2 double;
	declare lng2 double;
	declare zoomLevel2 int(11);
	declare photoId2 bigint(20);
	declare not_exist INT default true;

    DECLARE cursor_photo CURSOR FOR 
		SELECT * from photo_gisindex 
			where zoom_level = zoomLevel
				and lat > swLat
				and lat < nelat
				and lng > swLng
				and lng < neLng;  
	declare cursor_pgindex CURSOR FOR select * from t_pgindex;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_photo = true;
	
	
	DROP TEMPORARY TABLE IF EXISTS t_pgindex;
	create temporary table IF NOT EXISTS t_pgindex like photo_gisindex;
	
	open cursor_photo;
	photo_loop: LOOP
		set done_photo = false;
		fetch cursor_photo into lat1, lng1, zoomLevel1, photoId1;
		if done_photo then 
			LEAVE photo_loop;
		end if;
		SET not_exist = true;
		open cursor_pgindex;
		pgindex_loop: loop
			set done_photo = false;
			fetch cursor_pgindex into lat2, lng2, zoomLevel2, photoId2;
			if done_photo then
				LEAVE pgindex_loop;
			end if;
			if ABS((lat1 - lat2)*height/(neLat - swLat)) < 34
				and ABS((lng1 - lng2)*width/(neLng - swLng)) < 34 then
				set not_exist = false;
				LEAVE pgindex_loop;
			end if;
		end loop;
		close cursor_pgindex;
		if not_exist then 
			insert into t_pgindex values(lat1, lng1, zoomLevel1, photoId1);
		end if;
	end loop;
	close cursor_photo;
	select * from t_pgindex;
END