-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mag 05, 2019 alle 22:16
-- Versione del server: 10.1.36-MariaDB
-- Versione PHP: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `compitowebservicesstep`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `allegati`
--

CREATE TABLE `allegati` (
  `CodAllegato` int(11) NOT NULL,
  `Descrizione` varchar(100) NOT NULL,
  `PathAllegato` varchar(200) NOT NULL,
  `CodUtente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `autorizzazioni`
--

CREATE TABLE `autorizzazioni` (
  `CodAutorizzazione` int(11) NOT NULL,
  `Categoria` enum('Studente','Genitore','PersonaleATA','Docente','Segreteria','Vicepreside','Preside','Admin') NOT NULL,
  `Livello` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `autorizzazioni`
--

INSERT INTO `autorizzazioni` (`CodAutorizzazione`, `Categoria`, `Livello`) VALUES
(1, 'Admin', 10);

-- --------------------------------------------------------

--
-- Struttura della tabella `circolari`
--

CREATE TABLE `circolari` (
  `CodCircolare` int(11) NOT NULL,
  `Titolo` varchar(20) NOT NULL,
  `Descrizione` varchar(100) NOT NULL,
  `TagIdentificativo` enum('Comunicazione','Arte','Legalità','Maturità','Iscrizioni','Consigli di classe','Pagelle') NOT NULL,
  `Tipologia` enum('Circolare','Notizia','Comunicazione') NOT NULL,
  `Rilevante` tinyint(1) NOT NULL,
  `LivelloAutorizzativo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `circolari`
--

INSERT INTO `circolari` (`CodCircolare`, `Titolo`, `Descrizione`, `TagIdentificativo`, `Tipologia`, `Rilevante`, `LivelloAutorizzativo`) VALUES
(1, 'Vacanze Pasquali', 'Circolare di auguri', 'Arte', 'Circolare', 1, 5);

-- --------------------------------------------------------

--
-- Struttura della tabella `contenere`
--

CREATE TABLE `contenere` (
  `CodEnclosing` int(11) NOT NULL,
  `CodCircolare` int(11) NOT NULL,
  `CodAllegato` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `log`
--

CREATE TABLE `log` (
  `CodLog` int(11) NOT NULL,
  `Data` date NOT NULL,
  `Ora` time NOT NULL,
  `Azione` varchar(50) NOT NULL,
  `CodUtente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `pubblicare`
--

CREATE TABLE `pubblicare` (
  `CodPubblicazione` int(11) NOT NULL,
  `Data` date NOT NULL,
  `CodUtente` int(11) NOT NULL,
  `CodCircolare` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `utenti`
--

CREATE TABLE `utenti` (
  `CodUtente` int(11) NOT NULL,
  `Nome` varchar(30) NOT NULL,
  `Cognome` varchar(30) NOT NULL,
  `Mail` varchar(50) NOT NULL,
  `Username` varchar(20) NOT NULL,
  `Password` varchar(20) NOT NULL,
  `Classe` int(1) DEFAULT NULL,
  `Sezione` char(1) DEFAULT NULL,
  `Indirizzo` varchar(10) DEFAULT NULL,
  `Materia` varchar(20) DEFAULT NULL,
  `MatricolaServizio` varchar(10) DEFAULT NULL,
  `CodAutorizzazione` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `utenti`
--

INSERT INTO `utenti` (`CodUtente`, `Nome`, `Cognome`, `Mail`, `Username`, `Password`, `Classe`, `Sezione`, `Indirizzo`, `Materia`, `MatricolaServizio`, `CodAutorizzazione`) VALUES
(1, 'Marco', 'Maspes', 'marco.maspes00@gmail.com', 'maspes_marco', '000', NULL, NULL, NULL, NULL, NULL, 1),
(2, 'Ornella', 'Perin', 'marco.sara.maspes@gmail.com', 'ornella', '000', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 'Sara', 'Maspes', 'sara00@gmail.com', 'sara', '000', NULL, NULL, NULL, NULL, NULL, NULL),
(4, 'PierPaolo', 'Maspes', 'pier@gmail.com', 'pier', '000', NULL, NULL, NULL, NULL, NULL, NULL),
(5, 'Carla', 'Locatelli', 'carla@gmail.com', 'carla', '000', NULL, NULL, NULL, NULL, NULL, NULL),
(6, 'Luigi', 'Perin', 'luigi@gmail.com', 'luigi', '000', NULL, NULL, NULL, NULL, NULL, NULL),
(7, 'Enrica', 'Frigerio', 'enrica@gmail.com', 'enrica', '000', NULL, NULL, NULL, NULL, NULL, NULL);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `allegati`
--
ALTER TABLE `allegati`
  ADD PRIMARY KEY (`CodAllegato`),
  ADD KEY `CodUtente` (`CodUtente`);

--
-- Indici per le tabelle `autorizzazioni`
--
ALTER TABLE `autorizzazioni`
  ADD PRIMARY KEY (`CodAutorizzazione`);

--
-- Indici per le tabelle `circolari`
--
ALTER TABLE `circolari`
  ADD PRIMARY KEY (`CodCircolare`);

--
-- Indici per le tabelle `contenere`
--
ALTER TABLE `contenere`
  ADD PRIMARY KEY (`CodEnclosing`),
  ADD KEY `CodCircolare` (`CodCircolare`),
  ADD KEY `CodAllegato` (`CodAllegato`);

--
-- Indici per le tabelle `log`
--
ALTER TABLE `log`
  ADD PRIMARY KEY (`CodLog`),
  ADD KEY `CodUtente` (`CodUtente`);

--
-- Indici per le tabelle `pubblicare`
--
ALTER TABLE `pubblicare`
  ADD PRIMARY KEY (`CodPubblicazione`),
  ADD KEY `CodUtente` (`CodUtente`),
  ADD KEY `CodCircolare` (`CodCircolare`);

--
-- Indici per le tabelle `utenti`
--
ALTER TABLE `utenti`
  ADD PRIMARY KEY (`CodUtente`),
  ADD KEY `CodAutorizzazione` (`CodAutorizzazione`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `allegati`
--
ALTER TABLE `allegati`
  MODIFY `CodAllegato` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `autorizzazioni`
--
ALTER TABLE `autorizzazioni`
  MODIFY `CodAutorizzazione` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT per la tabella `circolari`
--
ALTER TABLE `circolari`
  MODIFY `CodCircolare` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT per la tabella `contenere`
--
ALTER TABLE `contenere`
  MODIFY `CodEnclosing` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `log`
--
ALTER TABLE `log`
  MODIFY `CodLog` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `pubblicare`
--
ALTER TABLE `pubblicare`
  MODIFY `CodPubblicazione` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `utenti`
--
ALTER TABLE `utenti`
  MODIFY `CodUtente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `allegati`
--
ALTER TABLE `allegati`
  ADD CONSTRAINT `allegati_ibfk_1` FOREIGN KEY (`CodUtente`) REFERENCES `utenti` (`CodUtente`);

--
-- Limiti per la tabella `contenere`
--
ALTER TABLE `contenere`
  ADD CONSTRAINT `contenere_ibfk_1` FOREIGN KEY (`CodCircolare`) REFERENCES `circolari` (`CodCircolare`),
  ADD CONSTRAINT `contenere_ibfk_2` FOREIGN KEY (`CodAllegato`) REFERENCES `allegati` (`CodAllegato`);

--
-- Limiti per la tabella `log`
--
ALTER TABLE `log`
  ADD CONSTRAINT `log_ibfk_1` FOREIGN KEY (`CodUtente`) REFERENCES `utenti` (`CodUtente`);

--
-- Limiti per la tabella `pubblicare`
--
ALTER TABLE `pubblicare`
  ADD CONSTRAINT `pubblicare_ibfk_1` FOREIGN KEY (`CodUtente`) REFERENCES `utenti` (`CodUtente`),
  ADD CONSTRAINT `pubblicare_ibfk_2` FOREIGN KEY (`CodCircolare`) REFERENCES `circolari` (`CodCircolare`);

--
-- Limiti per la tabella `utenti`
--
ALTER TABLE `utenti`
  ADD CONSTRAINT `utenti_ibfk_1` FOREIGN KEY (`CodAutorizzazione`) REFERENCES `autorizzazioni` (`CodAutorizzazione`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
