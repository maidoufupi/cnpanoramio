USE `panornew`;
--
-- Table structure for table `user_tag`
--

DROP TABLE IF EXISTS `user_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_tag` (
  `user_settings_id` bigint(20) NOT NULL,
  `tags_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_settings_id`,`tags_id`),
  KEY `FKF022FD26BC9B5425` (`tags_id`),
  KEY `FKF022FD269B92C307` (`user_settings_id`),
  CONSTRAINT `FKF022FD269B92C307` FOREIGN KEY (`user_settings_id`) REFERENCES `user_settings` (`id`),
  CONSTRAINT `FKF022FD26BC9B5425` FOREIGN KEY (`tags_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_tag`
--

/*!40000 ALTER TABLE `user_tag` DISABLE KEYS */;
REPLACE INTO `user_tag` (`user_settings_id`, `tags_id`) VALUES (3,1),(3,2),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8),(4,9),(6,9),(12,9),(12,10),(4,11),(4,12),(4,13);
/*!40000 ALTER TABLE `user_tag` ENABLE KEYS */;

update tag set user_id = (select user_settings_id from user_tag where tags_id = tag.id limit 1);

DROP TABLE IF EXISTS `user_tag`;

-- update create date
update travel set create_date = '2014-09-21 00:00:00' where create_date = '0000-00-00 00:00:00';
update travel set modify_date = '2014-09-21 00:00:00' where modify_date = '0000-00-00 00:00:00';
update travel_spot set create_date = '2014-09-21 00:00:00' where create_date = '0000-00-00 00:00:00';
update travel_spot set modify_date = '2014-09-21 00:00:00' where modify_date = '0000-00-00 00:00:00';
update photo set create_date = '2014-09-21 00:00:00' where create_date = '0000-00-00 00:00:00';
update photo set modify_date = '2014-09-21 00:00:00' where modify_date = '0000-00-00 00:00:00';
update comment set create_date = '2014-09-21 00:00:00' where create_date = '0000-00-00 00:00:00';
update comment set modify_date = '2014-09-21 00:00:00' where modify_date = '0000-00-00 00:00:00';
update message_queue set create_date = '2014-09-21 00:00:00' where create_date = '0000-00-00 00:00:00';
update message_queue set modify_date = '2014-09-21 00:00:00' where modify_date = '0000-00-00 00:00:00';
update message set create_date = '2014-09-21 00:00:00' where create_date = '0000-00-00 00:00:00';
update message set modify_date = '2014-09-21 00:00:00' where modify_date = '0000-00-00 00:00:00';
update circle set create_date = '2014-09-21 00:00:00' where create_date = '0000-00-00 00:00:00';
update circle set modify_date = '2014-09-21 00:00:00' where modify_date = '0000-00-00 00:00:00';
update recycle set create_date = '2014-09-21 00:00:00' where create_date = '0000-00-00 00:00:00';
update recycle set modify_date = '2014-09-21 00:00:00' where modify_date = '0000-00-00 00:00:00';

update travel set deleted = 0 where deleted is null;
update travel_spot set deleted = 0 where deleted is null;
update photo set deleted = 0 where deleted is null;
update comment set deleted = 0 where deleted is null;
update message_queue set deleted = 0 where deleted is null;
update message set deleted = 0 where deleted is null;
update circle set deleted = 0 where deleted is null;
update recycle set deleted = 0 where deleted is null;

-- update comment
update comment set type = 'photo';

-- update travel
delete from travel where title = '';