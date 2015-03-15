-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: Set 04, 2014 alle 15:34
-- Versione del server: 5.6.12-log
-- Versione PHP: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `our_products`
--
CREATE DATABASE IF NOT EXISTS `our_products` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `our_products`;

-- --------------------------------------------------------

--
-- Struttura della tabella `e_our_product`
--

CREATE TABLE IF NOT EXISTS `e_our_product` (
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `e_our_product`
--

INSERT INTO `e_our_product` (`name`) VALUES
('Acqua'),
('Arrosticini'),
('Birra'),
('Bistecca'),
('Bruschetta al Tartufo'),
('Bruschetta con Pomodoro'),
('Bruschetta Sale e Olio'),
('Caffè'),
('Castagne'),
('Coca Cola'),
('Cotiche e Fagioli'),
('Fagioli'),
('Fagioli e Salsiccia'),
('Fanta'),
('Frutta'),
('Gelato'),
('Panino'),
('Pannocchia'),
('Pasta'),
('Patate al Forno'),
('Patatine Fritte'),
('Pizza'),
('Porchetta'),
('Sagne e Fagioli'),
('Salsicce'),
('Sprite'),
('Torta'),
('Varietà di Dolci'),
('Vino (Bicchiere)'),
('Vino (Bottiglia)');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
