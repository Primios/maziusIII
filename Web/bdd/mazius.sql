-- phpMyAdmin SQL Dump
-- version 5.1.4
-- https://www.phpmyadmin.net/
--
-- Host: mysql-serveurmazius.alwaysdata.net
-- Generation Time: Mar 23, 2023 at 08:20 PM
-- Server version: 10.6.11-MariaDB
-- PHP Version: 7.4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `serveurmazius_bdd`
--

-- --------------------------------------------------------

--
-- Table structure for table `mazius`
--

CREATE TABLE `mazius` (
  `id` bigint(64) NOT NULL,
  `name` varchar(255) NOT NULL,
  `level` varchar(255) NOT NULL,
  `date` date DEFAULT NULL,
  `step` int(11) DEFAULT NULL,
  `seed` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mazius`
--

INSERT INTO `mazius` (`id`, `name`, `level`, `date`, `step`, `seed`) VALUES
(30, 'valentin', 'Easy', '2023-03-23', 124, 123456),
(31, 'resgre', 'Easy', '2023-03-23', 50, 276463665),
(32, 'mlkjhghf', 'Easy', '2023-03-23', 98, 98456156),
(33, 'primios', 'Easy', '2023-03-23', 100, 879564213),
(34, 'dbzuai', 'Easy', '2023-03-23', 89, 123456),
(35, 'cthardles', 'Hard', '2023-03-23', 566, 538325492),
(36, 'cthardlesd', 'Hard', '2023-03-23', 550, 538325492),
(38, 'ccsvcomment', 'Easy', '2023-03-23', 108, 80085),
(39, 'ooooooooooo', 'Easy', '2023-03-23', 110, 258878683),
(40, 'dzjaiuhodaz', 'Easy', '2023-03-23', 74, 894561),
(41, 'lolo', 'Normal', '2023-03-23', 317, 123456),
(42, 'dzajkp', 'Easy', '2023-03-23', 137, 89754),
(44, 'noggg', 'Normal', '2023-03-23', 325, 130000000),
(45, 'kinadim', 'Easy', '2023-03-23', 143, 690288230),
(46, 'nadinadou', 'Easy', '2023-03-23', 43, 95);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mazius`
--
ALTER TABLE `mazius`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mazius`
--
ALTER TABLE `mazius`
  MODIFY `id` bigint(64) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
