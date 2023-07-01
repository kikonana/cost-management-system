/*
SQLyog Ultimate v12.5.1 (32 bit)
MySQL - 10.4.28-MariaDB : Database - cms_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cms_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `cms_db`;

/*Table structure for table `budget` */

DROP TABLE IF EXISTS `budget`;

CREATE TABLE `budget` (
  `id_budget` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `nama_budget` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_budget`),
  KEY `userid` (`userid`),
  CONSTRAINT `budget_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `budget` */

insert  into `budget`(`id_budget`,`userid`,`nama_budget`) values 
(1,1,'makan'),
(2,1,'jalan');

/*Table structure for table `budget_category` */

DROP TABLE IF EXISTS `budget_category`;

CREATE TABLE `budget_category` (
  `id_budcat` int(11) NOT NULL AUTO_INCREMENT,
  `amount` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `id_budget` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_budcat`),
  KEY `id_budget` (`id_budget`),
  CONSTRAINT `budget_category_ibfk_1` FOREIGN KEY (`id_budget`) REFERENCES `budget` (`id_budget`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `budget_category` */

insert  into `budget_category`(`id_budcat`,`amount`,`description`,`id_budget`) values 
(1,'200000','makan malam',1),
(3,'5000.0','cireng',1);

/*Table structure for table `expense` */

DROP TABLE IF EXISTS `expense`;

CREATE TABLE `expense` (
  `id_exp` int(11) NOT NULL AUTO_INCREMENT,
  `date_exp` date DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `id_budget` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_exp`),
  KEY `id_budget` (`id_budget`),
  CONSTRAINT `expense_ibfk_1` FOREIGN KEY (`id_budget`) REFERENCES `budget` (`id_budget`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `expense` */

insert  into `expense`(`id_exp`,`date_exp`,`description`,`amount`,`id_budget`) values 
(1,'2023-06-30','cireng',200,1),
(2,'2023-06-30','jajan',200,1);

/*Table structure for table `income` */

DROP TABLE IF EXISTS `income`;

CREATE TABLE `income` (
  `id_inc` int(11) NOT NULL AUTO_INCREMENT,
  `date_inc` date DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `amount` varchar(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_inc`),
  KEY `userid` (`userid`),
  CONSTRAINT `income_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `income` */

insert  into `income`(`id_inc`,`date_inc`,`description`,`amount`,`userid`) values 
(2,'2023-06-29','gaji','3000000.0',1);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `user` */

insert  into `user`(`userid`,`username`,`password`,`created_at`) values 
(1,'admin','admin',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
