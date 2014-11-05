USE `ponmdb`;

update photo set alt = 0 where alt is null;
update photo set lat = 0 where lat is null;
update photo set lng = 0 where lng is null;

update photo_gps set alt = 0 where alt is null;

update photo set is360 = 0 where is360 is null;