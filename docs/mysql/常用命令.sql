SHOW FULL PROCESSLIST;

/** call procedure */
SET @address='';
CALL `sakila`.`GetCityAddress`(1, @address);
select @address;

/** create event */
use cddl;
DROP EVENT IF EXISTS e_statistics_daily;
CREATE EVENT e_statistics_daily
ON SCHEDULE EVERY 1 Day
STARTS '2013-10-18 16:45:00'
on completion preserve
DO CALL Get_Info_Every_Day();