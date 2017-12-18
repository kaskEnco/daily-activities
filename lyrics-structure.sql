-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: lyrics
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `l_language`
--

DROP TABLE IF EXISTS `l_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `l_language` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `lang_name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `lang_name_UNIQUE` (`lang_name`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `l_lyrics`
--

DROP TABLE IF EXISTS `l_lyrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `l_lyrics` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `lyric_title` varchar(256) NOT NULL,
  `lyric_content` longtext NOT NULL,
  `movie_id` int(4) NOT NULL,
  `writer_name` varchar(256) NOT NULL,
  `lyric_views` int(10) DEFAULT '0',
  `creation_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updation_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `movie_id` (`movie_id`),
  CONSTRAINT `l_lyrics_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `l_movie` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `l_movie`
--

DROP TABLE IF EXISTS `l_movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `l_movie` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `lang_id` int(4) NOT NULL,
  `movie_year_id` int(4) NOT NULL,
  `movie_name` varchar(256) NOT NULL,
  `movie_release_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `creation_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updation_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `lang_id` (`lang_id`),
  KEY `movie_year` (`movie_year_id`),
  KEY `id` (`id`),
  CONSTRAINT `l_movie_ibfk_1` FOREIGN KEY (`lang_id`) REFERENCES `l_language` (`id`),
  CONSTRAINT `l_movie_ibfk_2` FOREIGN KEY (`movie_year_id`) REFERENCES `l_year` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `l_year`
--

DROP TABLE IF EXISTS `l_year`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `l_year` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `lyric_year` year(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `lyric_year_UNIQUE` (`lyric_year`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-18 18:27:42
