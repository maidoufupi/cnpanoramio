-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getUserFavPanoramio`(in i_swLat double,
                                     in i_swLng double,
									 in i_neLat double,
                                     in i_neLng double,
                                     in i_level int(2),
									 in i_vendor char(50),
                                     in i_width int(4),
								     in i_height int(4),
									 in i_userId BIGINT(20))
BEGIN
	DECLARE done_photo INT DEFAULT FALSE;
	declare not_exist INT default true;
	DECLARE l_swLng double;
	DECLARE l_neLng double;

	DECLARE l_id BIGINT(20);
	DECLARE l_lat double;
	DECLARE l_lng double;
	DECLARE l_alt double;
	DECLARE l_rating INT(11);
	DECLARE l_title VARCHAR(255);
	DECLARE l_address VARCHAR(255);
	
	DECLARE l_lat_temp double;
	DECLARE l_lng_temp double;
	DECLARE l_rating_temp INT(11);

	DECLARE cursor_photo CURSOR FOR 
		SELECT p.id, p.lat, p.lng, p.alt, p.rating, p.title, p.address 
			from photo as p inner join favorite as f
			on p.id = f.photo_id
			where p.lat > i_swLat
				and p.lat < i_nelat
				and p.lng > l_swLng
				and p.lng < l_neLng
				and f.user_id = i_userId;  
/******一下逻辑和getUserPhotoPanoramio逻辑一样，只不过是photo表取数不同**********************************************************/
	declare cursor_upIndex CURSOR FOR select lat, lng, rating from t_userPhotoIndex;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_photo = true;
	DROP TEMPORARY TABLE IF EXISTS t_userPhotoIndex;
	create temporary table IF NOT EXISTS t_userPhotoIndex like photo_panoramio;
	
	if i_swLng < i_neLng then
		set l_swLng = i_swLng;
		set l_neLng = i_neLng;
/*******重复逻辑*****************************************************/
	open cursor_photo;
	photo_loop: LOOP
		set done_photo = false;
		fetch cursor_photo into l_id, l_lat, l_lng, l_alt, l_rating, l_title, l_address;
		if done_photo then 
			LEAVE photo_loop;
		end if;
		SET not_exist = true;
		open cursor_upIndex;
		upIndex_loop: loop
			set done_photo = false;
			fetch cursor_upIndex into l_lat_temp, l_lng_temp, l_rating_temp;
			if done_photo then
				LEAVE upIndex_loop;
			end if;
			if ABS((l_lat - l_lat_temp)*i_height/(i_neLat - i_swLat)) < 34
				and ABS((l_lng - l_lng_temp)*i_width/(i_neLng - i_swLng)) < 34 then
				set not_exist = false;
				LEAVE upIndex_loop;
			end if;
		end loop;
		close cursor_upIndex;
		if not_exist then 
			insert into t_userPhotoIndex(`photo_id`, `lat`, `lng`, `alt`, `rating`, `title`, `address`) values(l_id, l_lat, l_lng, l_alt, l_rating, l_title, l_address);
		end if;
	end loop;
	close cursor_photo;
/****************************************************************/		
	else
		set l_swLng = i_swLng;
		set l_neLng = 180;
/*******重复逻辑*****************************************************/
	open cursor_photo;
	photo_loop: LOOP
		set done_photo = false;
		fetch cursor_photo into l_id, l_lat, l_lng, l_alt, l_rating, l_title, l_address;
		if done_photo then 
			LEAVE photo_loop;
		end if;
		SET not_exist = true;
		open cursor_upIndex;
		upIndex_loop: loop
			set done_photo = false;
			fetch cursor_upIndex into l_lat_temp, l_lng_temp, l_rating_temp;
			if done_photo then
				LEAVE upIndex_loop;
			end if;
			if ABS((l_lat - l_lat_temp)*i_height/(i_neLat - i_swLat)) < 34
				and ABS((l_lng - l_lng_temp)*i_width/(i_neLng - i_swLng)) < 34 then
				set not_exist = false;
				LEAVE upIndex_loop;
			end if;
		end loop;
		close cursor_upIndex;
		if not_exist then 
			insert into t_userPhotoIndex(`photo_id`, `lat`, `lng`, `alt`, `rating`, `title`, `address`) values(l_id, l_lat, l_lng, l_alt, l_rating, l_title, l_address);
		end if;
	end loop;
	close cursor_photo;
/****************************************************************/
		set l_swLng = -180;
		set l_neLng = i_neLng;
/*******重复逻辑*****************************************************/
	open cursor_photo;
	photo_loop: LOOP
		set done_photo = false;
		fetch cursor_photo into l_id, l_lat, l_lng, l_alt, l_rating, l_title, l_address;
		if done_photo then 
			LEAVE photo_loop;
		end if;
		SET not_exist = true;
		open cursor_upIndex;
		upIndex_loop: loop
			set done_photo = false;
			fetch cursor_upIndex into l_lat_temp, l_lng_temp, l_rating_temp;
			if done_photo then
				LEAVE upIndex_loop;
			end if;
			if ABS((l_lat - l_lat_temp)*i_height/(i_neLat - i_swLat)) < 34
				and ABS((l_lng - l_lng_temp)*i_width/(i_neLng - i_swLng)) < 34 then
				set not_exist = false;
				LEAVE upIndex_loop;
			end if;
		end loop;
		close cursor_upIndex;
		if not_exist then 
			insert into t_userPhotoIndex(`photo_id`, `lat`, `lng`, `alt`, `rating`, `title`, `address`) values(l_id, l_lat, l_lng, l_alt, l_rating, l_title, l_address);
		end if;
	end loop;
	close cursor_photo;
/****************************************************************/
	end if;
	
	select * from t_userPhotoIndex;
END