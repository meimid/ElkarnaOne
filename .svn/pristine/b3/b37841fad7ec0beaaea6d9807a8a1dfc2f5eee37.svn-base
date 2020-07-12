CREATE DATABASE  IF NOT EXISTS `credit` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `credit`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: credit
-- ------------------------------------------------------
-- Server version	5.5.19

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
-- Table structure for table `sequencetable`
--

DROP TABLE IF EXISTS `sequencetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequencetable` (
  `current_id` int(11) NOT NULL AUTO_INCREMENT,
  `current_Sequennce` int(11) DEFAULT NULL,
  PRIMARY KEY (`current_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO CREDIT.sequencetable(current_Sequennce) VALUES(1);
--
--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `numAccount` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `dateCreation` date NOT NULL,
  `devise` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  `numPersonne` varchar(255) NOT NULL,
  PRIMARY KEY (`numAccount`),
  KEY `FK_e2q06gmqyb3sjwhgx3h4sffpy` (`numPersonne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `accountsmovement`
--

DROP TABLE IF EXISTS `accountsmovement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accountsmovement` (
  `numAccountMovement` int(11) NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) NOT NULL,
  `dateCreation` date DEFAULT NULL,
  `dateOperation` date NOT NULL,
  `libelle` varchar(255) NOT NULL,
  `montantCredit` bigint(20) DEFAULT NULL,
  `montantDebit` bigint(20) DEFAULT NULL,
  `valide` tinyint(1) NOT NULL,
  `numAccount` varchar(255) NOT NULL,
  `userLogin` varchar(255) DEFAULT NULL,
  `numJournal` int(11) NOT NULL,
  `numTransaction` int(11) NOT NULL,
  PRIMARY KEY (`numAccountMovement`),
  KEY `FK_prgtjnbft60kpnu7w0uddqs8c` (`numAccount`),
  KEY `FK_pd73pg2n6ecp5q7atxqv8smsk` (`userLogin`),
  KEY `FK_n1yfea4ufgf6gxjxqlxbp41va` (`numJournal`),
  KEY `FK_dhulbum6ax5g444x1i35yapff` (`numTransaction`),
  CONSTRAINT `FK_dhulbum6ax5g444x1i35yapff` FOREIGN KEY (`numTransaction`) REFERENCES `transactiontable` (`current_id`),
  CONSTRAINT `FK_n1yfea4ufgf6gxjxqlxbp41va` FOREIGN KEY (`numJournal`) REFERENCES `journal` (`numJournal`),
  CONSTRAINT `FK_pd73pg2n6ecp5q7atxqv8smsk` FOREIGN KEY (`userLogin`) REFERENCES `user` (`userLogin`),
  CONSTRAINT `FK_prgtjnbft60kpnu7w0uddqs8c` FOREIGN KEY (`numAccount`) REFERENCES `account` (`numAccount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `journal`
--

DROP TABLE IF EXISTS `journal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `journal` (
  `numJournal` int(11) NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) NOT NULL,
  `closeAmount` bigint(20) DEFAULT NULL,
  `createUser` tinyblob,
  `dateDebut` date NOT NULL,
  `dateFin` date DEFAULT NULL,
  `datecreation` date NOT NULL,
  `openAmount` bigint(20) NOT NULL,
  `typeJournal` int(11) NOT NULL,
  `numPeriode` int(11) NOT NULL,
  `dateClose` date DEFAULT NULL,
  `numAccountPrincipal` varchar(255) NOT NULL,
  `createUser_userLogin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`numJournal`),
  KEY `FK_ai4fxw8s0ucbyx5topwnxrrg` (`numPeriode`),
  KEY `FK_ffdomdiwn3m7uh4cdbvwbf4jn` (`createUser_userLogin`),
  CONSTRAINT `FK_ffdomdiwn3m7uh4cdbvwbf4jn` FOREIGN KEY (`createUser_userLogin`) REFERENCES `user` (`userLogin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personne`
--

DROP TABLE IF EXISTS `personne`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personne` (
  `numPersonne` varchar(255) NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `birthDate` date DEFAULT NULL,
  `dateCreatio` date DEFAULT NULL,
  `nom` varchar(255) NOT NULL,
  `numTelCell` varchar(255) DEFAULT NULL,
  `numTelFixe` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`numPersonne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `periode`
--

DROP TABLE IF EXISTS `periode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `periode` (
  `numPeriod` int(11) NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) NOT NULL,
  `closeAmount` bigint(20) DEFAULT NULL,
  `dateDebut` date NOT NULL,
  `dateFin` date DEFAULT NULL,
  `libelle` varchar(255) NOT NULL,
  `openAmount` bigint(20) NOT NULL,
  `userLogin` varchar(255) NOT NULL,
  PRIMARY KEY (`numPeriod`),
  KEY `FK_gdnu1dupp2m2e21vom4ysqwf` (`userLogin`),
  CONSTRAINT `FK_gdnu1dupp2m2e21vom4ysqwf` FOREIGN KEY (`userLogin`) REFERENCES `user` (`userLogin`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transactiontable`
--

DROP TABLE IF EXISTS `transactiontable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transactiontable` (
  `current_id` int(11) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`current_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `journalmovement`
--

DROP TABLE IF EXISTS `journalmovement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `journalmovement` (
  `numJournalMovement` int(11) NOT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `dateCreation` date DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `montantCredit` bigint(20) DEFAULT NULL,
  `montantDebut` bigint(20) DEFAULT NULL,
  `numTransaction` varchar(255) DEFAULT NULL,
  `numAccount` varchar(255) NOT NULL,
  `userLogin` varchar(255) NOT NULL,
  `numJournal` int(11) NOT NULL,
  PRIMARY KEY (`numJournalMovement`),
  KEY `FK_hid7uot37002ie1pbt179mk8i` (`numAccount`),
  KEY `FK_m2ge8ht5laees16m4w4qdurc1` (`userLogin`),
  KEY `FK_hi43k8e8r7g6klymfe9irxlvl` (`numJournal`),
  CONSTRAINT `FK_hi43k8e8r7g6klymfe9irxlvl` FOREIGN KEY (`numJournal`) REFERENCES `journal` (`numJournal`),
  CONSTRAINT `FK_hid7uot37002ie1pbt179mk8i` FOREIGN KEY (`numAccount`) REFERENCES `account` (`numAccount`),
  CONSTRAINT `FK_m2ge8ht5laees16m4w4qdurc1` FOREIGN KEY (`userLogin`) REFERENCES `user` (`userLogin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sequencetableoperation`
--

DROP TABLE IF EXISTS `sequencetableoperation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequencetableoperation` (
  `current_id` int(11) NOT NULL AUTO_INCREMENT,
  `current_Sequennce` int(11) DEFAULT NULL,
  PRIMARY KEY (`current_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userLogin` varchar(255) NOT NULL,
  `personne` tinyblob,
  PRIMARY KEY (`userLogin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-12-24 11:51:10
