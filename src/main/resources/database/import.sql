CREATE DATABASE IF NOT EXISTS batbatcar;
USE batbatcar;

-- MySQL dump 10.13  Distrib 8.0.28, for Linux (x86_64)
--
-- Host: localhost    Database: batbatcar-test
-- ------------------------------------------------------
-- Server version	8.0.28-0ubuntu0.20.04.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `viajes`
--

DROP TABLE IF EXISTS `viajes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `viajes` (
  `codViaje` int NOT NULL AUTO_INCREMENT,
  `propietario` varchar(100) DEFAULT NULL,
  `ruta` varchar(100) DEFAULT NULL,
  `fechaSalida` datetime DEFAULT NULL,
  `duracion` int DEFAULT NULL,
  `precio` decimal(10,0) DEFAULT NULL,
  `plazasOfertadas` int DEFAULT NULL,
  `estadoViaje` enum('ABIERTO','CERRADO','CANCELADO') DEFAULT NULL,
  PRIMARY KEY (`codViaje`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `viajes`
--

LOCK TABLES `viajes` WRITE;
/*!40000 ALTER TABLE `viajes` DISABLE KEYS */;
INSERT INTO `viajes` VALUES (1,'Roberto','Alicante-Ibi','2025-08-03 19:59:45',30,24,8,'ABIERTO'),(2,'Alex','Alcoi-Cocentaina','2025-05-03 12:00:00',5,5,10,'ABIERTO'),(3,'Luis','Alcoi-Valencia','2024-10-03 20:00:00',20,40,3,'CANCELADO'),(4,'Juan','Alicante-Valencia','2023-10-01 20:00:00',22,11,5,'CERRADO'),(5,'Paco','Valencia-Alcoi','2026-10-03 20:00:00',20,40,3,'ABIERTO');
/*!40000 ALTER TABLE `viajes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservas`
--

DROP TABLE IF EXISTS `reservas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas` (
  `codigoReserva` varchar(10) NOT NULL,
  `usuario` varchar(100) DEFAULT NULL,
  `plazasSolicitadas` int DEFAULT NULL,
  `fechaRealizacion` datetime DEFAULT NULL,
  `viaje` int NOT NULL,
  PRIMARY KEY (`codigoReserva`),
  KEY `reservas_FK` (`viaje`),
  CONSTRAINT `reservas_FK` FOREIGN KEY (`viaje`) REFERENCES `viajes` (`codViaje`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas`
--

LOCK TABLES `reservas` WRITE;
/*!40000 ALTER TABLE `reservas` DISABLE KEYS */;
INSERT INTO `reservas` VALUES ('1-1','Antonio',4,'2022-04-03 23:56:06',1),('1-2','Alex',3,'2022-01-12 12:12:22',1),('2-1','Roberto',5,'2022-04-03 23:56:06',2),('2-2','Roberto',5,'2022-04-03 23:56:06',2);
/*!40000 ALTER TABLE `reservas` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-28 23:14:49
