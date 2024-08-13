-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Creato il: Ago 13, 2024 alle 19:26
-- Versione del server: 5.7.39
-- Versione PHP: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `acs`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `access_rule`
--

CREATE TABLE `access_rule` (
  `id` bigint(20) NOT NULL,
  `start_date` datetime(6),
  `end_date` datetime(6),
  `notes` varchar(255) DEFAULT NULL,
  `employee_id` bigint(20) DEFAULT NULL,
  `room_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='The Employee entity.';

--
-- Dump dei dati per la tabella `access_rule`
--

INSERT INTO `access_rule` (`id`, `start_date`, `end_date`, `notes`, `employee_id`, `room_id`) VALUES
(1505, '2024-08-11 18:24:00.000000', '2025-08-11 18:24:00.000000', NULL, 1500, 1500),
(1506, '2024-08-11 18:31:00.000000', '2024-08-11 18:31:00.000000', NULL, 1500, 1501);

-- --------------------------------------------------------

--
-- Struttura della tabella `employee`
--

CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL COMMENT 'The firstname attribute.',
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `employee`
--

INSERT INTO `employee` (`id`, `first_name`, `last_name`, `email`) VALUES
(1500, 'Ludovico', 'Iovino', 'ludovico.iovino@gssi.it');

-- --------------------------------------------------------

--
-- Struttura della tabella `room`
--

CREATE TABLE `room` (
  `id` bigint(20) NOT NULL,
  `room_name` varchar(255) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `room`
--

INSERT INTO `room` (`id`, `room_name`, `uid`) VALUES
(1500, 'Ufficio IOVINO', '29d3d3'),
(1501, 'ufficio2', 'ddedede');

-- --------------------------------------------------------

--
-- Struttura della tabella `tag`
--

CREATE TABLE `tag` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `employee_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `tag`
--

INSERT INTO `tag` (`id`, `code`, `employee_id`) VALUES
(1500, 'cd7c4d9', 1500);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `access_rule`
--
ALTER TABLE `access_rule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_access_rule__employee_id` (`employee_id`),
  ADD KEY `fk_access_rule__room_id` (`room_id`);

--
-- Indici per le tabelle `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `tag`
--
ALTER TABLE `tag`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_tag__employee_id` (`employee_id`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `access_rule`
--
ALTER TABLE `access_rule`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1507;

--
-- AUTO_INCREMENT per la tabella `employee`
--
ALTER TABLE `employee`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1501;

--
-- AUTO_INCREMENT per la tabella `room`
--
ALTER TABLE `room`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1502;

--
-- AUTO_INCREMENT per la tabella `tag`
--
ALTER TABLE `tag`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1504;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `access_rule`
--
ALTER TABLE `access_rule`
  ADD CONSTRAINT `fk_access_rule__employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  ADD CONSTRAINT `fk_access_rule__room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`);

--
-- Limiti per la tabella `tag`
--
ALTER TABLE `tag`
  ADD CONSTRAINT `fk_tag__employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
