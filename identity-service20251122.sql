-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: identity_service
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `invalidated_token`
--

DROP TABLE IF EXISTS `invalidated_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invalidated_token` (
  `id` varchar(255) NOT NULL,
  `expiration_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invalidated_token`
--

LOCK TABLES `invalidated_token` WRITE;
/*!40000 ALTER TABLE `invalidated_token` DISABLE KEYS */;
INSERT INTO `invalidated_token` VALUES ('11f1058f-3753-40ef-94b8-698e74340be9','2025-10-31 10:53:24.000000'),('138e9d26-b60d-4d36-bfda-29e46dc2a2cb','2025-10-31 10:54:40.000000'),('5827ac05-9ce8-497a-be49-874659c925d2','2025-10-30 20:20:26.000000'),('5d36795a-49e8-4336-a5ab-073ae51e97bd','2025-10-31 17:16:13.000000'),('b7b1388a-3589-475e-95fb-3f55e4cb0ac0','2025-10-30 21:01:13.000000'),('c69a0516-aefe-47e2-939b-16d838bb011a','2025-10-30 20:38:09.000000'),('cb333ed3-1787-4841-8297-ac4f0c5f7bb7','2025-10-31 17:15:42.000000');
/*!40000 ALTER TABLE `invalidated_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES ('APPROVE_POST','Approve a post'),('CREATE_DATA','Create user data'),('READ_DATA','Read user data'),('REJECT_POST','Reject a post'),('UPDATE_DATA','Update user data');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('ADMIN','Admin role'),('USER','User role');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permissions` (
  `role_name` varchar(255) NOT NULL,
  `permissions_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_name`,`permissions_name`),
  KEY `FKf5aljih4mxtdgalvr7xvngfn1` (`permissions_name`),
  CONSTRAINT `FKcppvu8fk24eqqn6q4hws7ajux` FOREIGN KEY (`role_name`) REFERENCES `role` (`name`),
  CONSTRAINT `FKf5aljih4mxtdgalvr7xvngfn1` FOREIGN KEY (`permissions_name`) REFERENCES `permission` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
INSERT INTO `role_permissions` VALUES ('ADMIN','APPROVE_POST'),('ADMIN','CREATE_DATA'),('USER','READ_DATA'),('ADMIN','REJECT_POST'),('USER','UPDATE_DATA');
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `dob` date DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `roles` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('06f88b0a-4771-47a1-a478-e99e78255e70','2000-07-22','caideo','jr','$2a$10$YXJZm0mI3uUW9HQWvWPNsOi7gIPrcSkyCfV13w6Ruf0XNRwag99fe','test3',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0w\0\0\0t\0USERx'),('0c67dee2-8441-47aa-b332-2524f1bb6f31','2000-07-22','lionel','messsiu','123456','thanglon',NULL),('0ed5ebc2-0d15-47b0-9d5a-69d50b6c1f83','2000-07-22','lionel','messsiu','123','12',NULL),('1561b2f3-c780-46cc-943d-fc01f849ced3','2000-07-22','caideo','jr','$2a$10$nQeEGYs4f7TjzTf4hohJrON7L4e0z/Gu12pJxpJbfl1rPTh1nEM.C','testspringsecurity',NULL),('2852ee29-54e4-4028-85c0-5f074de29368','2000-07-22','neymar','jr','1234asdasdadas56','con',NULL),('2ac82a7d-934c-4208-b2af-24bc918691ef','2010-07-22','caideo','jr','$2a$10$mxMYOlmDeO5dCpObfiT0IecHSOmLMrwOHMbudOn3KlkEl..vlpwka','test4',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0w\0\0\0t\0USERx'),('37521e39-bc9e-4d7a-be9a-ffc4e9ba8448','2000-05-12','nhinconcac','dybala','123','contala',NULL),('4b4b55bc-8b5b-4515-83cf-3f13d804fa0b','2000-07-22','lionel','messsiu','123','concac',NULL),('507a60fd-9166-45ff-a54f-55cbd81a87b5','2000-07-22','neymar','jr','1234asdasdadas56','conmatlon ',NULL),('62c75d5d-8848-4b76-8fea-8efe482d7c24','2000-07-22','caideo','jr','$2a$10$nwUCCLRx2GeBwvYNEC7HLOLUqvVNIxpsJk4rGS4cVhUVjJoje0boi','test1',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0w\0\0\0t\0USERx'),('645378fb-6dd9-46a9-ba3b-4abba2500a46','2000-07-22','caideo','jr','$2a$10$sLbh0Yc/G2KxL1NAY2AZzOecIbOBeioD0JyZSW59kUmnwpMLxefG2','test2',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0w\0\0\0t\0USERx'),('726e5e6c-2d37-4f0b-805d-4e2495a802b6','2000-07-22','lionel','messsiu','123','okcool',NULL),('79161462-a2f0-4581-8e03-b8e6b9cc0c8d','2000-05-04','cu','loz','$2a$10$639WPfgFldfxN31PSF/mGOvO.Mt.kfM2Pr.Rtwo4wX/P6.QEKfTtK','test5',NULL),('7c3bb3a6-0465-409b-8ae4-109f70ea1643','2000-07-22','lionel','messsiu','123','conlon',NULL),('7fb294c8-5cb5-4ddc-b707-12504bfba89d','2004-09-11','daniel','phan','123','whatthehell',NULL),('a238ecbe-809f-4021-b42c-6965b538fdc5','2000-07-22','caideo','jr','$2a$10$F5rBhZK9cUD.MO52Ix0gheGzUIKVmnHkBBxjgVPGdvgpppV7HgDXq','testmahoabcrypt',NULL),('b3a963a5-b642-4819-bc6a-86c67c33543e','2000-07-22','caideo','jr','$2a$10$.cSO21REy45px6s54cwqjezHs7ZaU3zMZOsKmv2doYkidfJktYcQW','marrio',NULL),('c3fa49bb-8ae6-4156-a74f-51b009683a61','2000-07-22','neymarkkk','jr','121413212','w1313231',NULL),('c969b9f8-3ea9-4c87-90f2-f8e8c2191b5a','1990-09-02','Admin','Admin','$2a$10$L4rPNOcH9oIle3sM5PjrXOb4qCrNdRBuFvu8yrKb42YwfqmOX5phK','admin',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0w\0\0\0t\0ADMINx'),('d91b19dc-3b26-4cc2-9046-8a761ff44dcb','2000-07-22','neymar','jr','123456','conmatlon',NULL),('db3686e7-9319-4a16-a8f6-fcdf50b7f104','2000-07-22','caideo','jr','$2a$10$vLXx3JIr22HwpigGN7PYlukEkp5QwsSB5.IoH2rDN1gG/hEVBgSAO','caideogithe',NULL),('f0ae3dbf-ab7d-4652-a6f2-f0fd6603377f','2000-07-22','caideo','jr','$2a$10$KBT24vjf6bAAA9TwgudM9.ph8CiunMn0PxBwTyxKymyLzinEM3S1O','testUserRole',_binary '¨\Ì\0sr\0java.util.ArrayListxÅ\“ô\«aù\0I\0sizexp\0\0\0w\0\0\0t\0USERx');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` varchar(255) NOT NULL,
  `roles_name` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`,`roles_name`),
  KEY `FK6pmbiap985ue1c0qjic44pxlc` (`roles_name`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK6pmbiap985ue1c0qjic44pxlc` FOREIGN KEY (`roles_name`) REFERENCES `role` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES ('c969b9f8-3ea9-4c87-90f2-f8e8c2191b5a','ADMIN');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-22  8:32:52
