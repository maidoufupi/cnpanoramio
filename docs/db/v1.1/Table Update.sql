USE `ponmdb`;

alter table travel add column album_cover BIGINT(20) null;
ALTER TABLE travel ADD CONSTRAINT fk_album_cover FOREIGN KEY (album_cover) REFERENCES photo(id);
ALTER TABLE `travel` ADD COLUMN `deleted` BIT(1) NULL DEFAULT NULL AFTER `user_id`;


-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: panornew
-- ------------------------------------------------------
-- Server version	5.6.14-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `recycle`
--

DROP TABLE IF EXISTS `recycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recycle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `recy_id` bigint(20) DEFAULT NULL,
  `recy_type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK408B7293F707A0D3` (`user_id`),
  CONSTRAINT `FK408B7293F707A0D3` FOREIGN KEY (`user_id`) REFERENCES `user_settings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_recycle`
--

DROP TABLE IF EXISTS `user_recycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_recycle` (
  `user_settings_id` bigint(20) NOT NULL,
  `recycle_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_settings_id`,`recycle_id`),
  UNIQUE KEY `recycle_id` (`recycle_id`),
  KEY `FK9B7B0A1F9B92C307` (`user_settings_id`),
  KEY `FK9B7B0A1FA64AA964` (`recycle_id`),
  CONSTRAINT `FK9B7B0A1F9B92C307` FOREIGN KEY (`user_settings_id`) REFERENCES `user_settings` (`id`),
  CONSTRAINT `FK9B7B0A1FA64AA964` FOREIGN KEY (`recycle_id`) REFERENCES `recycle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `photo_gps`
--
DROP TABLE IF EXISTS `photo_photo_gps`;
DROP TABLE IF EXISTS `photo_gps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `photo_gps` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `alt` double DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `vendor` varchar(255) DEFAULT NULL,
  `photo_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCD79951D194EAB84` (`photo_id`),
  CONSTRAINT `FKCD79951D194EAB84` FOREIGN KEY (`photo_id`) REFERENCES `photo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
