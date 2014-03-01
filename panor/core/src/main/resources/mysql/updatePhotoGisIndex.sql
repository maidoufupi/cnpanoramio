-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`db775f7wf1lr50gp`@`%` PROCEDURE `updatePhotoGisIndex`()
BEGIN
  DECLARE n INT Default 0 ;
/** update photo rating */
  update photo as p set rating = (select count(*) from comment as c
        where c.photo_id = p.id);

/** cal map level photos */  
	delete from photo_gisindex;
	insert into photo_gisindex 
		select DISTINCT lat, lng, 1, id 
			from photo
			where (lat <> 0 or lng <> 0) group by lat, lng order by rating desc;
  
  update_level_loop: LOOP
	SET n = n + 1;
	insert into photo_gisindex 
		select lat, lng, (n + 1), photo_id 
			from photo_gisindex
			where zoom_level = n;
         IF n = 7 THEN
            LEAVE update_level_loop;
         END IF;
   END LOOP update_level_loop;
END