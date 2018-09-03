-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: test_db
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `branch` (
  `branchNumber` int(11) NOT NULL AUTO_INCREMENT,
  `location` text NOT NULL,
  `numOfEmployees` int(11) NOT NULL,
  `phone` text NOT NULL,
  PRIMARY KEY (`branchNumber`),
  UNIQUE KEY `branch_branchNumber_uindex` (`branchNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES (1,'Tel Aviv',10,'03-5063264'),(2,'Holon',7,'03-5024596'),(3,'Tel Aviv ',12,'03-5359583');
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_details`
--

DROP TABLE IF EXISTS `cart_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cart_details` (
  `cartID` int(11) NOT NULL,
  `branch_Number` int(11) NOT NULL,
  `employeeNum` int(11) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`cartID`),
  KEY `cartFK_idx` (`cartID`),
  KEY `cart_details_branch_branchNumber_fk` (`branch_Number`),
  KEY `cart_details_employee_employeeCode_fk` (`employeeNum`),
  CONSTRAINT `cart_details_branch_branchNumber_fk` FOREIGN KEY (`branch_Number`) REFERENCES `branch` (`branchNumber`),
  CONSTRAINT `cart_details_employee_employeeCode_fk` FOREIGN KEY (`employeeNum`) REFERENCES `employee` (`employeeCode`),
  CONSTRAINT `cart_details_shoppingcart_cartCode_fk` FOREIGN KEY (`cartID`) REFERENCES `shoppingcart` (`cartCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_details`
--

LOCK TABLES `cart_details` WRITE;
/*!40000 ALTER TABLE `cart_details` DISABLE KEYS */;
INSERT INTO `cart_details` VALUES (1,3,4,'2018-06-12'),(2,2,5,'2018-02-23'),(2469,2,1,'2018-08-27');
/*!40000 ALTER TABLE `cart_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `clientNumber` int(11) NOT NULL AUTO_INCREMENT,
  `clientID` int(11) NOT NULL,
  `fullName` text NOT NULL,
  `phone` text NOT NULL,
  `ClientType` text NOT NULL,
  `discountRate` double NOT NULL,
  PRIMARY KEY (`clientNumber`),
  UNIQUE KEY `clients_clientNumber_uindex` (`clientNumber`),
  UNIQUE KEY `clients_clientNumber_uindex_2` (`clientNumber`),
  UNIQUE KEY `clients_clientID_uindex` (`clientID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,237465209,'Joseph Stalin','050-44444444','RETURNCLIENT',10),(2,843749573,'Donald Tramp','053-55555555','VIPCLIENT',40),(3,857362434,'Netta Barzilai','052-33333333','NEWCLIENT',0),(4,646293744,'Barack Obama','053-2459394','NEWCLIENT',0),(5,145468832,'Missy Elliot','052-2592694','NEWCLIENT',0),(6,522222222,'baba abba','3421422435','NEWCLIENT',0);
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `employeeCode` int(11) NOT NULL AUTO_INCREMENT,
  `fullName` text NOT NULL,
  `password` text,
  `ID` int(11) NOT NULL,
  `phoneNum` text NOT NULL,
  `accountNumber` int(11) NOT NULL,
  `branch` int(11) NOT NULL,
  `profession` text NOT NULL,
  PRIMARY KEY (`employeeCode`),
  UNIQUE KEY `employee_employeeCode_uindex` (`employeeCode`),
  UNIQUE KEY `employee_ID_uindex` (`ID`),
  UNIQUE KEY `employee_accountNumber_uindex` (`accountNumber`),
  KEY `employee_branch_branchNumber_fk` (`branch`),
  CONSTRAINT `employee_branch_branchNumber_fk` FOREIGN KEY (`branch`) REFERENCES `branch` (`branchNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'Guy Cohen','C4318372F98F4C46ED3A32C16EE4D7A76C832886D887631C0294B3314F34EDF1',123456789,'054-2352444',567832,2,'MANAGER'),(2,'Roy Bar','4AC58E2797F24A85C85B94E51299F7FA0851F1B1F02FCAAED3C75F297E3E5F19',987666642,'053-5454336',325566,3,'MANAGER'),(3,'Steve Jobs','B2E277207179D53195ECF4BC79A3D3DFD5F55629E2AE1982784F35D8B868FC0D',235325325,'055-5235255',398898,2,'SELLER'),(4,'Elon Mask','331927BF1A855269DF75774BC538E91029DC20E3849F15D1A3B51353A17CCB72',298909988,'054-3243245',886999,3,'CASHIER'),(5,'Bill Gates','DB09DE66076E2AFA228BFACFF37F659DE0D312DB3E27B6AA1DF33F5C9911AB2A',989577357,'054-3253528',908536,2,'CASHIER'),(6,'Roy Zimun','FE3160BC8704B7C7F97AAE0277223E5D05820C681726A1632D0740FA6C3F87A0',876658134,'050-4324321',314531,1,'MANAGER');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inventory` (
  `inventory_code` int(11) NOT NULL,
  `productCode` int(11) NOT NULL,
  `numberOfItems` int(11) NOT NULL,
  PRIMARY KEY (`inventory_code`,`productCode`),
  KEY `inventory_product_productCode_fk` (`productCode`),
  CONSTRAINT `inventory_branch_branchNumber_fk` FOREIGN KEY (`inventory_code`) REFERENCES `branch` (`branchNumber`),
  CONSTRAINT `inventory_product_productCode_fk` FOREIGN KEY (`productCode`) REFERENCES `product` (`productCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,1,11),(1,2,14),(1,3,3),(1,4,5),(1,8,10),(2,1,1),(2,2,5),(2,4,4),(2,5,3),(2,6,4),(2,7,3);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `productCode` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`productCode`),
  UNIQUE KEY `product_productCode_uindex` (`productCode`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Graphic T-Shirt',30),(2,'Jeans',150),(3,'Converse Shoes',250),(4,'Socks',15),(5,'Underwear',20),(6,'Luis Vitton Dress',550),(7,'Gucci Sunglasses',450.5),(8,'Button up shirt',120);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_history`
--

DROP TABLE IF EXISTS `shopping_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_history` (
  `clientNumber` int(11) NOT NULL,
  `cartID` int(11) NOT NULL,
  PRIMARY KEY (`cartID`),
  UNIQUE KEY `shopping_history_clientNumber_uindex` (`clientNumber`),
  CONSTRAINT `shopping_history_clients_clientNumber_fk` FOREIGN KEY (`clientNumber`) REFERENCES `clients` (`clientNumber`),
  CONSTRAINT `shopping_history_shopping_cart_cartCode_fk` FOREIGN KEY (`cartID`) REFERENCES `shoppingcart` (`cartCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_history`
--

LOCK TABLES `shopping_history` WRITE;
/*!40000 ALTER TABLE `shopping_history` DISABLE KEYS */;
INSERT INTO `shopping_history` VALUES (1,1),(2,2),(3,2469);
/*!40000 ALTER TABLE `shopping_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shoppingcart`
--

DROP TABLE IF EXISTS `shoppingcart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shoppingcart` (
  `cartCode` int(11) NOT NULL,
  `product_code` int(11) NOT NULL,
  `numOfItems` int(11) NOT NULL,
  PRIMARY KEY (`cartCode`,`product_code`),
  KEY `shoppingcart_product_productCode_fk` (`product_code`),
  KEY `shoppingcart_cartdetails_cartID_FK_idx` (`cartCode`),
  CONSTRAINT `shoppingcart_product_productCode_fk` FOREIGN KEY (`product_code`) REFERENCES `product` (`productCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shoppingcart`
--

LOCK TABLES `shoppingcart` WRITE;
/*!40000 ALTER TABLE `shoppingcart` DISABLE KEYS */;
INSERT INTO `shoppingcart` VALUES (1,3,5),(1,4,2),(1,7,3),(2,1,1),(2,8,3),(2469,5,2),(2469,6,2);
/*!40000 ALTER TABLE `shoppingcart` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-28 12:44:18
