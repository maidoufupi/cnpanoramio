-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getPhotoPanoramioIndex`(in swLat double,
	                                       in swLng double,
										   in neLat double,
	                                       in neLng double,
	                                       in i_level int(2),
										   in i_vendor char(50),
	                                       in i_width int(4),
											in i_height int(4),
											in i_latest boolean)
BEGIN
	DECLARE l_measure DOUBLE DEFAULT 40;
	DECLARE swLatLow DOUBLE;
	DECLARE swLngLow DOUBLE;
	DECLARE l_south DOUBLE;
	DECLARE l_west  DOUBLE;
	DECLARE l_north DOUBLE;
	DECLARE l_east  DOUBLE;

	SET l_measure = l_measure / (SELECT POW(2, i_level));	
	
	set l_south = swLat - (select swLat % l_measure);
	set l_west  = swLng - (select swLng % l_measure);
	set l_north = neLat - (select neLat % l_measure);
	set l_east  = neLng - (select neLng % l_measure);

/** */
	if i_vendor is null then
		set i_vendor = 'gps';
	end if;
	if i_latest then
		IF l_east > l_west THEN
			SELECT i.photo_id, i.photo_rating as rating, g.lat, g.lng, g.alt, g.address, '' as title
				FROM photo_latest_index as i
				inner join photo_gps as g
				  on i.photo_id = g.photo_id AND g.vendor = i_vendor
				WHERE i.level = i_level
				  AND i.south <= l_north
				  AND i.south >= l_south
				  AND i.west  <= l_east
				  AND i.west  >= l_west;
		ELSE
			SELECT i.photo_id, i.photo_rating as rating, g.lat, g.lng, g.alt, g.address, '' as title
				FROM photo_latest_index as i
				  inner join photo_gps as g
					  on i.photo_id = g.photo_id AND g.vendor = i_vendor
				WHERE i.level = i_level
				  AND i.south <= l_north
				  AND i.south >= l_south
				  AND ( ( i.west  >= l_west AND i.west  <= 180 )
					OR ( i.west >= -180 AND i.west <= l_east));
		END IF;
	else
		IF l_east > l_west THEN
			SELECT i.photo_id, i.photo_rating as rating, g.lat, g.lng, g.alt, g.address, '' as title
				FROM photo_panoramio_index as i
				inner join photo_gps as g
				  on i.photo_id = g.photo_id AND g.vendor = i_vendor
				WHERE i.level = i_level
				  AND i.south <= l_north
				  AND i.south >= l_south
				  AND i.west  <= l_east
				  AND i.west  >= l_west;
		ELSE
			SELECT i.photo_id, i.photo_rating as rating, g.lat, g.lng, g.alt, g.address, '' as title
				FROM photo_panoramio_index as i
				  inner join photo_gps as g
					  on i.photo_id = g.photo_id AND g.vendor = i_vendor
				WHERE i.level = i_level
				  AND i.south <= l_north
				  AND i.south >= l_south
				  AND ( ( i.west  >= l_west AND i.west  <= 180 )
					OR ( i.west >= -180 AND i.west <= l_east));
		END IF;
	end if;
END