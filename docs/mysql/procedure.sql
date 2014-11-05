-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `GetCityAddress`(in cityid int, out address varchar(50))
BEGIN
declare output int;
  select a.address into address from city as c inner join address as a 
    on c.city_id = a.city_id where c.city_id = cityid limit 1;
END