update panor.photo set alt = 0 where alt is null;
update panor.photo set lat = 0 where lat is null;
update panor.photo set lng = 0 where lng is null;

update panor.photo_gps set alt = 0 where alt is null;