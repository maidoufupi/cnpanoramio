-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `updatePhotoPanoramioIndex`()
BEGIN
	DECLARE default_measure DOUBLE DEFAULT 20;
	DECLARE l_measure DOUBLE;
	DECLARE l_level int(11);
	DECLARE l_photo_id bigint(20);
	DECLARE l_rating int(11);
	DECLARE l_photo_rating int(11);
	DECLARE l_lat DOUBLE;
	DECLARE l_lng DOUBLE;
	DECLARE l_south DOUBLE;
	DECLARE l_west DOUBLE;
	DECLARE l_panor_id bigint(20);
	DECLARE l_panor_rating int(11);
	DECLARE done_photo INT DEFAULT false;
	DECLARE cursor_photo CURSOR FOR 
		SELECT id, rating, lat, lng from photo;

	DECLARE cursor_panor CURSOR FOR 
		SELECT south, west, photo_id, rating, photo_rating
			from photo_panoramio_index
			where level = l_level;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_photo = true;
	
/** update photo rating */
    update photo as p set rating = (select count(*) from comment as c
        where c.photo_id = p.id);
	update photo_panoramio_index set rating = 0, photo_rating = 0;

/** 更新最底层photo */
	set l_level = 19;
	SET l_measure = default_measure / (SELECT POW(2, l_level));
	
	open cursor_photo;
	photo_loop: LOOP
		fetch cursor_photo into l_photo_id, l_photo_rating, l_lat, l_lng;
		if done_photo then 
			LEAVE photo_loop;
		end if;
		set l_south = l_lat - (select l_lat % l_measure);
		set l_west  = l_lng - (select l_lng % l_measure);
		set l_panor_id = 0;
		set l_panor_rating = 0;
		select photo_id, photo_rating into l_panor_id, l_panor_rating
			from photo_panoramio_index
			where level = l_level
			  AND south = l_south
			  AND west  = l_west;
		if(l_panor_id = 0) then 
			INSERT INTO photo_panoramio_index (`level`,
				`south`,
				`west`,
				`photo_id`,
				`rating`,
				`photo_rating`)
				VALUES
				( l_level,
				l_south,
				l_west,
				l_photo_id,
				l_rating,
				l_photo_rating);
		elseif( l_photo_rating > l_panor_rating) then 
			UPDATE photo_panoramio_index
				SET
				photo_id = l_photo_id,
				rating = (rating + l_photo_rating),
				photo_rating = l_photo_rating
				WHERE level = l_level 
                  AND south = l_south 
				  AND west  = l_west;
		else
			UPDATE photo_panoramio_index
				SET
				rating = (rating + l_photo_rating)
				WHERE level = l_level 
                  AND south = l_south 
				  AND west  = l_west;
		end if;
	end LOOP;
	close cursor_photo;

/** 更新各level的photo */
	while l_level > 1 do
		set done_photo = false;
		SET l_measure = default_measure / (SELECT POW(2, l_level - 1));
		open cursor_panor;
		panor_loop: LOOP
			fetch cursor_panor into l_lat, l_lng, l_photo_id, l_rating, l_photo_rating;
			if done_photo then 
				LEAVE panor_loop;
			end if;
			set l_south = l_lat - (select l_lat % l_measure);
			set l_west  = l_lng - (select l_lng % l_measure);
			set l_panor_id = 0;
			set l_panor_rating = 0;
			select photo_id, photo_rating into l_panor_id, l_panor_rating
				from photo_panoramio_index
				where level = (l_level - 1)
				  AND south = l_south
				  AND west  = l_west;
			if(l_panor_id = 0) then 
				INSERT INTO photo_panoramio_index (`level`,
					`south`,
					`west`,
					`photo_id`,
					`rating`,
					`photo_rating`)
					VALUES
					( l_level - 1,
					l_south,
					l_west,
					l_photo_id,
					l_rating,
					l_photo_rating);
			elseif( l_photo_rating > l_panor_rating) then
				UPDATE photo_panoramio_index
					SET
					photo_id = l_photo_id,
					rating = (rating + l_photo_rating),
					photo_rating = l_photo_rating
					WHERE level = (l_level - 1) 
					  AND south = l_south 
					  AND west  = l_west;
			else
				UPDATE photo_panoramio_index
					SET
					rating = (rating + l_photo_rating)
					WHERE level = (l_level - 1) 
					  AND south = l_south 
					  AND west  = l_west;
			end if;
		end loop;
		close cursor_panor;
		set l_level = l_level - 1;
	End while ;	
END