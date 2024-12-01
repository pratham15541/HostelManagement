-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 01, 2024 at 12:20 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hostelmanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `complain`
--

CREATE TABLE `complain` (
  `Sr` int(50) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `complain` varchar(100) NOT NULL,
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0,
  `createdAt` datetime NOT NULL DEFAULT current_timestamp(),
  `updatedAt` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `complain`
--

INSERT INTO `complain` (`Sr`, `userId`, `complain`, `isDeleted`, `createdAt`, `updatedAt`) VALUES
(1, '1', 'de', 0, '2024-12-01 15:14:44', '2024-12-01 15:14:44'),
(2, '1', '2121', 0, '2024-12-01 15:14:54', '2024-12-01 15:14:54'),
(3, '1', '1', 0, '2024-12-01 15:21:35', '2024-12-01 15:21:35');

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `Sr` int(11) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `feedback` varchar(100) NOT NULL,
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0,
  `createdAt` datetime NOT NULL DEFAULT current_timestamp(),
  `updatedAt` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`Sr`, `userId`, `feedback`, `isDeleted`, `createdAt`, `updatedAt`) VALUES
(1, '1', '21', 0, '2024-12-01 15:27:31', '2024-12-01 15:27:31');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `Sr` int(11) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `roomId` varchar(50) NOT NULL,
  `bookCondition` tinyint(4) NOT NULL DEFAULT 0,
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0,
  `createdAt` datetime NOT NULL DEFAULT current_timestamp(),
  `updatedAt` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`Sr`, `userId`, `roomId`, `bookCondition`, `isDeleted`, `createdAt`, `updatedAt`) VALUES
(1, '1', '001', 1, 0, '2024-12-01 15:45:18', '2024-12-01 15:46:12'),
(2, '2', '002', 1, 0, '2024-12-01 15:45:18', '2024-12-01 15:46:36'),
(3, '', '003', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(4, '', '004', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(5, '', '005', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(6, '', '006', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(7, '', '007', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(8, '1', '008', 1, 0, '2024-12-01 15:45:18', '2024-12-01 15:46:14'),
(9, '', '009', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(10, '2', '101', 1, 0, '2024-12-01 15:45:18', '2024-12-01 15:46:38'),
(11, '', '102', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(12, '', '103', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(13, '', '104', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(14, '', '105', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18'),
(15, '', '106', 0, 0, '2024-12-01 15:45:18', '2024-12-01 15:45:18');

-- --------------------------------------------------------

--
-- Table structure for table `staffs`
--

CREATE TABLE `staffs` (
  `Sr` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `designation` varchar(50) NOT NULL,
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0,
  `createdAt` datetime NOT NULL DEFAULT current_timestamp(),
  `updatedAt` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `staffs`
--

INSERT INTO `staffs` (`Sr`, `name`, `designation`, `isDeleted`, `createdAt`, `updatedAt`) VALUES
(1, 'demo', 'software engineer', 0, '2024-12-01 14:57:12', '2024-12-01 14:57:12'),
(2, 'demo1', 'electrical engineer', 0, '2024-12-01 14:57:12', '2024-12-01 14:57:12'),
(3, 'demo3', 'electrical assistant', 0, '2024-12-01 14:57:12', '2024-12-01 14:57:12'),
(4, 'demo4', 'machine engineer', 0, '2024-12-01 14:57:12', '2024-12-01 14:57:12'),
(5, 'demo5', 'electrical engineer', 0, '2024-12-01 14:57:12', '2024-12-01 14:57:12'),
(6, 'demo6', 'electrical engineer', 0, '2024-12-01 14:57:12', '2024-12-01 14:57:12'),
(7, 'demo7', 'electrical engineer', 0, '2024-12-01 14:57:12', '2024-12-01 14:57:12'),
(8, 'de3', 'de2', 0, '2024-12-01 16:33:40', '2024-12-01 16:33:40'),
(9, '2121', '21', 0, '2024-12-01 16:39:31', '2024-12-01 16:39:31');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `Sr` int(11) NOT NULL,
  `role` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `mobile_number` int(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `isDeleted` tinyint(4) NOT NULL DEFAULT 0,
  `createdAt` datetime NOT NULL DEFAULT current_timestamp(),
  `updatedAt` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`Sr`, `role`, `name`, `email`, `password`, `mobile_number`, `address`, `isDeleted`, `createdAt`, `updatedAt`) VALUES
(1, 'admin', 'de', 'de', 'de', 123, 'deq', 0, '2024-12-01 14:56:17', '2024-12-01 15:02:04'),
(2, 'user', 'de1', 'de1', 'de1', 12, 'de1', 0, '2024-12-01 14:56:17', '2024-12-01 14:56:17'),
(3, 'user', 'de2', 'de2', 'de2', 1212, 'de2', 0, '2024-12-01 14:56:17', '2024-12-01 14:56:17'),
(4, 'user', '12', '12', '12', 12, '12', 0, '2024-12-01 14:56:17', '2024-12-01 14:56:17'),
(5, 'user', 'de3', 'de3', 'de3', 1221, 'de3', 0, '2024-12-01 16:31:39', '2024-12-01 16:31:39'),
(6, '', '2', '2', '2', 12211, '21', 0, '2024-12-01 16:39:47', '2024-12-01 16:39:47'),
(7, '', '2121', '21', '1212', 21212, '1212', 0, '2024-12-01 16:43:25', '2024-12-01 16:43:25'),
(9, '', '2121', '32343', '211', 323, '23', 0, '2024-12-01 16:48:41', '2024-12-01 16:48:41');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `complain`
--
ALTER TABLE `complain`
  ADD PRIMARY KEY (`Sr`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`Sr`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`Sr`);

--
-- Indexes for table `staffs`
--
ALTER TABLE `staffs`
  ADD PRIMARY KEY (`Sr`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Sr`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `complain`
--
ALTER TABLE `complain`
  MODIFY `Sr` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `Sr` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `Sr` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `staffs`
--
ALTER TABLE `staffs`
  MODIFY `Sr` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `Sr` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
