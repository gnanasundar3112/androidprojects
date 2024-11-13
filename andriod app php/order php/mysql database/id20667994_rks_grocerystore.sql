-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 02, 2023 at 06:11 AM
-- Server version: 10.5.20-MariaDB
-- PHP Version: 7.3.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id20667994_rks_grocerystore`
--

-- --------------------------------------------------------

--
-- Table structure for table `acyear`
--

CREATE TABLE `acyear` (
  `year_id` varchar(6) NOT NULL DEFAULT '',
  `year_name` varchar(100) NOT NULL DEFAULT '',
  `year_mark` varchar(1) NOT NULL DEFAULT '',
  `status` int(11) NOT NULL DEFAULT 1,
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `acyear`
--

INSERT INTO `acyear` (`year_id`, `year_name`, `year_mark`, `status`, `crea_date`, `crea_time`, `modi_date`, `modi_time`) VALUES
('202223', 'ACCOUNTING YEAR 2022-23', 'Z', 1, '0000-00-00', '00:00:00', '0000-00-00', '00:00:00'),
('202324', 'ACCOUNTING YEAR 2023-24', 'A', 1, '0000-00-00', '00:00:00', '0000-00-00', '00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE `address` (
  `acyear` varchar(6) NOT NULL,
  `firm_id` varchar(1) NOT NULL DEFAULT '1',
  `order_no` varchar(10) NOT NULL,
  `order_date` date NOT NULL DEFAULT '0000-00-00',
  `add_name` varchar(40) NOT NULL,
  `add_mobile` varchar(40) NOT NULL,
  `add_address` varchar(200) NOT NULL,
  `add_state` varchar(40) NOT NULL,
  `add_city` varchar(40) NOT NULL,
  `add_pincode` varchar(10) NOT NULL,
  `cust_phone` varchar(10) NOT NULL,
  `deli_type` varchar(40) NOT NULL,
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`acyear`, `firm_id`, `order_no`, `order_date`, `add_name`, `add_mobile`, `add_address`, `add_state`, `add_city`, `add_pincode`, `cust_phone`, `deli_type`, `crea_date`, `crea_time`, `modi_date`, `modi_time`) VALUES
('202324', '1', 'AR00000001', '2023-08-01', 'Gnana Sundar', '8438910200', 'main road, agarakattu', 'TAMIL NADU', 'SURANDAI', '627852', '8438910200', 'Cash on delivery', '2023-08-01', '07:26:37', '2023-08-01', '07:26:37'),
('202324', '1', 'AR00000002', '2023-08-01', 'Gnana Sundar', '8438910200', 'agarakattu', 'KERALA', 'THIRUVANANTHAPURAM', '627811', '8438910200', 'Cash on delivery', '2023-08-01', '07:27:49', '2023-08-01', '07:27:49'),
('202324', '1', 'AR00000003', '2023-08-01', 'Gnana Sundar', '8438910200', 'main', 'TAMIL NADU', 'TENKASI', '627852', '8438910200', 'Cash on delivery', '2023-08-01', '10:19:15', '2023-08-01', '10:19:15');

-- --------------------------------------------------------

--
-- Table structure for table `cartorder`
--

CREATE TABLE `cartorder` (
  `acyear` varchar(6) NOT NULL,
  `firm_id` varchar(1) NOT NULL DEFAULT '1',
  `ref_date` date NOT NULL DEFAULT '0000-00-00',
  `cust_phone` varchar(10) NOT NULL,
  `prod_id` varchar(7) NOT NULL,
  `size_id` varchar(5) NOT NULL,
  `rate` decimal(15,2) NOT NULL,
  `qty` decimal(15,3) NOT NULL DEFAULT 0.000,
  `amount` decimal(15,2) NOT NULL DEFAULT 0.00,
  `cgstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `sgstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `igstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `cgstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `sgstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `igstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `deli_charg` decimal(11,2) NOT NULL DEFAULT 0.00,
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `cartorder`
--

INSERT INTO `cartorder` (`acyear`, `firm_id`, `ref_date`, `cust_phone`, `prod_id`, `size_id`, `rate`, `qty`, `amount`, `cgstper`, `sgstper`, `igstper`, `cgstamt`, `sgstamt`, `igstamt`, `deli_charg`, `crea_date`, `crea_time`, `modi_date`, `modi_time`) VALUES
('202324', '1', '2023-08-01', '', '0300108', '00003', 120.00, 2.000, 60.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, '2023-08-01', '07:57:24', '2023-08-01', '07:59:32'),
('202324', '1', '2023-08-01', '', '0300109', '00001', 30.00, 3.000, 5.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, '2023-08-01', '07:57:41', '2023-08-01', '07:57:41'),
('202324', '1', '2023-08-02', '8438910200', '0300108', '00001', 120.00, 1.000, 6.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, '2023-08-02', '10:19:59', '2023-08-02', '10:20:00'),
('202324', '1', '2023-08-02', '8438910200', '0300109', '00006', 30.00, 10.000, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, '2023-08-02', '11:28:46', '2023-08-02', '11:28:46');

-- --------------------------------------------------------

--
-- Table structure for table `cartprice`
--

CREATE TABLE `cartprice` (
  `id` int(11) NOT NULL,
  `item_total` varchar(40) NOT NULL,
  `delivery` varchar(40) NOT NULL,
  `tax` varchar(10) NOT NULL,
  `total_price` varchar(40) NOT NULL,
  `cust_phone` varchar(10) NOT NULL,
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00',
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `cartprice`
--

INSERT INTO `cartprice` (`id`, `item_total`, `delivery`, `tax`, `total_price`, `cust_phone`, `modi_date`, `modi_time`, `crea_date`, `crea_time`) VALUES
(283, '727.5', '40.0', '0.0', '767.5', '1234561234', '0000-00-00', '00:00:00', '0000-00-00', '00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `catemaster`
--

CREATE TABLE `catemaster` (
  `id` int(10) NOT NULL,
  `cate_id` varchar(2) NOT NULL DEFAULT '',
  `cate_name` varchar(100) NOT NULL,
  `tamil_name` varchar(100) NOT NULL,
  `image` varchar(100) NOT NULL,
  `crea_user` varchar(40) NOT NULL,
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `crea_stat` varchar(40) NOT NULL,
  `modi_user` varchar(40) NOT NULL,
  `modi_date` date NOT NULL DEFAULT current_timestamp(),
  `modi_time` time NOT NULL DEFAULT current_timestamp(),
  `modi_stat` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `catemaster`
--

INSERT INTO `catemaster` (`id`, `cate_id`, `cate_name`, `tamil_name`, `image`, `crea_user`, `crea_date`, `crea_time`, `crea_stat`, `modi_user`, `modi_date`, `modi_time`, `modi_stat`) VALUES
(95, '01', 'Fruits', 'பழங்கள்', 'uploadImage/fruits.jpg', '', '0000-00-00', '00:00:00', '', '', '2023-07-15', '12:29:36', ''),
(97, '02', 'Snacks', 'சிற்றுண்டி', 'uploadImage/snacks.jpg', '', '0000-00-00', '00:00:00', '', '', '2023-07-15', '12:29:36', ''),
(96, '03', 'Vegitable', 'காய்கறி', 'uploadImage/veg_img.jpg', '', '0000-00-00', '00:00:00', '', '', '2023-07-15', '12:29:36', '');

-- --------------------------------------------------------

--
-- Table structure for table `citymaster`
--

CREATE TABLE `citymaster` (
  `city_id` varchar(2) NOT NULL,
  `city_name` varchar(40) NOT NULL,
  `short_name` varchar(10) NOT NULL,
  `state_name` varchar(40) NOT NULL,
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time DEFAULT '00:00:00',
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `citymaster`
--

INSERT INTO `citymaster` (`city_id`, `city_name`, `short_name`, `state_name`, `crea_date`, `crea_time`, `modi_date`, `modi_time`) VALUES
('01', 'TENKASI', 'TK', 'TAMIL NADU', '0000-00-00', '00:00:00', '0000-00-00', '00:00:00'),
('02', 'SURANDAI', 'SI', 'TAMIL NADU', '0000-00-00', '00:00:00', '0000-00-00', '00:00:00'),
('03', 'THIRUVANANTHAPURAM', 'TM', 'KERALA', '0000-00-00', '00:00:00', '0000-00-00', '00:00:00'),
('04', 'KOCHI', 'KI', 'KERALA', '0000-00-00', '00:00:00', '0000-00-00', '00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `firmmaster`
--

CREATE TABLE `firmmaster` (
  `firm_id` varchar(1) NOT NULL DEFAULT '',
  `firm_name` varchar(50) DEFAULT '',
  `firm_sub` varchar(30) DEFAULT '',
  `firm_short` varchar(10) DEFAULT '',
  `firm_add1` varchar(50) DEFAULT '',
  `firm_add2` varchar(50) DEFAULT '',
  `firm_add3` varchar(50) DEFAULT '',
  `firm_place` varchar(30) DEFAULT '',
  `firm_pin` int(6) DEFAULT 0,
  `firm_type` int(1) DEFAULT 0,
  `firm_tin` varchar(15) DEFAULT '',
  `firm_cst` varchar(25) DEFAULT '',
  `firm_cin` varchar(25) DEFAULT '',
  `firm_fax` varchar(15) DEFAULT '',
  `firm_gstin` varchar(15) DEFAULT '',
  `firm_mark` varchar(1) DEFAULT '',
  `firm_taluk` varchar(40) DEFAULT '',
  `firm_dist` varchar(40) DEFAULT '',
  `firm_juris` varchar(40) DEFAULT '',
  `state_id` varchar(2) DEFAULT '',
  `state_name` varchar(30) DEFAULT '',
  `stcode` varchar(2) DEFAULT '',
  `phone1` varchar(20) DEFAULT '',
  `phone2` varchar(20) DEFAULT '',
  `mobile` varchar(20) DEFAULT '',
  `firm_stat` varchar(30) DEFAULT '',
  `firm_pan` varchar(30) DEFAULT '',
  `firm_email` varchar(40) DEFAULT '',
  `firm_bank` varchar(40) DEFAULT '',
  `firm_acno` varchar(20) DEFAULT '',
  `firm_ifsc` varchar(15) DEFAULT '',
  `tddb_name` varchar(20) DEFAULT '',
  `ac_num` varchar(8) DEFAULT '',
  `suffix` varchar(2) DEFAULT '',
  `firm_img` varchar(100) DEFAULT '',
  `active` int(1) DEFAULT 0,
  `crea_user` varchar(40) DEFAULT '',
  `crea_date` date DEFAULT '0000-00-00',
  `crea_time` varchar(8) DEFAULT '00:00:00',
  `crea_stat` varchar(3) DEFAULT '',
  `modi_user` varchar(40) DEFAULT '',
  `modi_date` date DEFAULT '0000-00-00',
  `modi_time` varchar(8) DEFAULT '00:00:00',
  `modi_stat` varchar(3) DEFAULT '',
  `edt_lock` int(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `firmmaster`
--

INSERT INTO `firmmaster` (`firm_id`, `firm_name`, `firm_sub`, `firm_short`, `firm_add1`, `firm_add2`, `firm_add3`, `firm_place`, `firm_pin`, `firm_type`, `firm_tin`, `firm_cst`, `firm_cin`, `firm_fax`, `firm_gstin`, `firm_mark`, `firm_taluk`, `firm_dist`, `firm_juris`, `state_id`, `state_name`, `stcode`, `phone1`, `phone2`, `mobile`, `firm_stat`, `firm_pan`, `firm_email`, `firm_bank`, `firm_acno`, `firm_ifsc`, `tddb_name`, `ac_num`, `suffix`, `firm_img`, `active`, `crea_user`, `crea_date`, `crea_time`, `crea_stat`, `modi_user`, `modi_date`, `modi_time`, `modi_stat`, `edt_lock`) VALUES
('1', 'R.K.S ', '', 'RKS', 'No.10,11, Daily Market ', '', '', 'Surandai', 627859, 1, '', '', '', '', '', 'R', 'Tenaksi(T.K)', 'Tenkasi(D.T)', 'Tenkasi', '33', 'TAMIL NADU', '33', '8778423873', '7904769286', '', 'TAMIL NADU', '', '', '', '', '', 'rks', '', 'R', 'D:\\vinsoft\\rks\\media\\grafix\\rks.png', 1, '', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `otp`
--

CREATE TABLE `otp` (
  `cust_phone` varchar(10) NOT NULL,
  `otp` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pincode_master`
--

CREATE TABLE `pincode_master` (
  `pincode_id` int(10) NOT NULL,
  `pincode` int(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `pincode_master`
--

INSERT INTO `pincode_master` (`pincode_id`, `pincode`) VALUES
(3, 627852),
(4, 627811),
(5, 627812);

-- --------------------------------------------------------

--
-- Table structure for table `productmaster`
--

CREATE TABLE `productmaster` (
  `Id` int(100) NOT NULL,
  `prod_id` varchar(7) NOT NULL DEFAULT '',
  `prod_name` varchar(100) NOT NULL,
  `tamil_name` varchar(100) NOT NULL,
  `rate` int(10) NOT NULL DEFAULT 0,
  `image` varchar(100) NOT NULL,
  `cate_id` varchar(2) NOT NULL DEFAULT '',
  `stock_type` int(1) NOT NULL DEFAULT 1,
  `crea_user` varchar(40) NOT NULL DEFAULT '',
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `crea_stat` varchar(2) NOT NULL DEFAULT '',
  `modi_user` varchar(40) NOT NULL DEFAULT '',
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00',
  `modi_stat` varchar(10) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `productmaster`
--

INSERT INTO `productmaster` (`Id`, `prod_id`, `prod_name`, `tamil_name`, `rate`, `image`, `cate_id`, `stock_type`, `crea_user`, `crea_date`, `crea_time`, `crea_stat`, `modi_user`, `modi_date`, `modi_time`, `modi_stat`) VALUES
(108, '0300108', 'Apple', 'ஆப்பிள்', 120, 'uploadImage/apple.jpg', '01', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(109, '0300109', 'Banana', 'வாழைப்பழம்', 30, 'uploadImage/banana.jpg', '01', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(93, '0300093', 'Beens', 'பீன்ஸ்', 25, 'uploadImage/beens.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(92, '0300092', 'Beetroot', 'பீட்ரூட்', 35, 'uploadImage/beetroot.png', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(94, '0300094', 'Bottle gourd', 'சுரைக்காய்', 16, 'uploadImage/bottle gourd.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(102, '0300102', 'Broccoli', 'காலிபிளவர்', 80, 'uploadImage/broccoli.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(87, '0300087', 'cabbage', 'முட்டைக்கோஸ்', 50, 'uploadImage/cabbage.jpeg', '03', 2, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(116, '0300116', 'Candy', 'மிட்டாய்', 600, 'uploadImage/candy.jpg', '02', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(86, '0300086', 'Cantalope', 'பாகற்காய்', 50, 'uploadImage/cantalope.jpeg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(95, '0300095', 'Capcicum', 'கேப்சிகம்', 38, 'uploadImage/capcicum.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(96, '0300096', 'Carrot', 'கேரட்', 60, 'uploadImage/carrot.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(90, '0300090', 'Cauliflower', 'காலிஃபிளவர்', 30, 'uploadImage/cauliflower.jpeg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(89, '0300089', 'Chow_chow', 'சவ் சவ்', 35, 'uploadImage/chow_chow.jpeg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(88, '0300088', 'Eggplant', 'கத்திரிக்காய்', 0, 'uploadImage/eggplant.jpeg', '03', 2, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(110, '0300110', 'Graps_green', 'கிராப்ஸ்', 150, 'uploadImage/graps_green.jpeg', '01', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(111, '0300111', 'Graps_red', 'கிராப்ஸ்', 200, 'uploadImage/graps_red.jpg', '01', 2, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(97, '0300097', 'Green chilly', 'பச்சை மிளகாய்', 15, 'uploadImage/green chilly.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(98, '0300098', 'Karunai kizhangu', 'கருணை கிழங்கு', 48, 'uploadImage/karunai kizhangu.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(85, '0300085', 'Onion', 'வெங்காயம்', 50, 'uploadImage/onion.jpeg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(115, '0300115', 'Orange', 'ஆரஞ்சு', 130, 'uploadImage/orange.jpg', '01', 2, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(117, '0300117', 'Popcorn', 'பாப்கார்ன்', 50, 'uploadImage/popcorn.jpg', '02', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(84, '0300084', 'Potato', 'உருளைக்கிழங்கு', 40, 'uploadImage/potato.jpeg', '03', 2, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(103, '0300103', 'Pumpkin', 'பூசணி', 120, 'uploadImage/pumpkin.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(99, '0300099', 'Red chilly', 'சிவப்பு மிளகாய்', 30, 'uploadImage/red chilly.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(100, '0300100', 'Senai kizhangu', 'சேனை கிழங்கு', 16, 'uploadImage/senai kizhangu.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', ''),
(101, '0300101', 'Snake guaed', 'புடலங்காய்', 38, 'uploadImage/snake guaed.jpg', '03', 1, '\'\'', '0000-00-00', '00:00:00', '', '', '0000-00-00', '00:00:00', '');

-- --------------------------------------------------------

--
-- Table structure for table `productsize`
--

CREATE TABLE `productsize` (
  `eff_date` date NOT NULL DEFAULT '0000-00-00',
  `prod_id` varchar(7) NOT NULL DEFAULT '''''',
  `size_id` varchar(5) NOT NULL DEFAULT '''''',
  `crea_user` varchar(40) NOT NULL DEFAULT '''''',
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `crea_stat` varchar(10) NOT NULL DEFAULT '''''',
  `modi_user` varchar(40) NOT NULL DEFAULT '''''',
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00',
  `modi_stat` varchar(10) NOT NULL DEFAULT ''''''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `productsize`
--

INSERT INTO `productsize` (`eff_date`, `prod_id`, `size_id`, `crea_user`, `crea_date`, `crea_time`, `crea_stat`, `modi_user`, `modi_date`, `modi_time`, `modi_stat`) VALUES
('2023-07-28', '0300108', '00001', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:57:48', '\'\''),
('2023-07-28', '0300108', '00002', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:57:48', '\'\''),
('2023-07-28', '0300108', '00003', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:57:48', '\'\''),
('2023-07-28', '0300108', '00004', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:57:48', '\'\''),
('2023-07-28', '0300108', '00005', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:55:56', '\'\''),
('2023-07-28', '0300108', '00006', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:55:56', '\'\''),
('2023-07-28', '0300108', '00007', '\'\'', '2023-07-29', '00:00:00', '\'\'', '\'\'', '2023-07-29', '07:04:41', '\'\''),
('2023-07-28', '0300108', '00008', '\'\'', '2023-07-29', '00:00:00', '\'\'', '\'\'', '2023-07-29', '07:04:41', '\'\''),
('2023-07-28', '0300109', '00001', '', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:55:56', '\'\''),
('2023-07-28', '0300109', '00002', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:55:56', '\'\''),
('2023-07-28', '0300109', '00003', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:55:56', '\'\''),
('2023-07-28', '0300109', '00004', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:55:56', '\'\''),
('2023-07-28', '0300109', '00005', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:55:56', '\'\''),
('2023-07-28', '0300109', '00006', '\'\'', '0000-00-00', '00:00:00', '\'\'', '\'\'', '2023-07-28', '15:55:56', '\'\'');

-- --------------------------------------------------------

--
-- Table structure for table `register`
--

CREATE TABLE `register` (
  `cust_id` int(10) NOT NULL,
  `cust_name` varchar(100) NOT NULL,
  `cust_email` varchar(100) NOT NULL,
  `cust_phone` bigint(10) NOT NULL,
  `cust_pass` varchar(100) NOT NULL,
  `image` varchar(100) NOT NULL,
  `otp` bigint(6) NOT NULL,
  `otp_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `otp_limit` bigint(5) NOT NULL DEFAULT 30
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `register`
--

INSERT INTO `register` (`cust_id`, `cust_name`, `cust_email`, `cust_phone`, `cust_pass`, `image`, `otp`, `otp_time`, `otp_limit`) VALUES
(45, 'Gnana Sundar', 'sundar3112000@gmail.com', 8438910200, '123', '', 254013, '2023-08-01 11:15:01', 30),
(47, 'Gnana Sundar', 'vijithalapathyvijay@gmail.com', 1234561234, '123', '', 219741, '2023-08-01 07:09:03', 30);

-- --------------------------------------------------------

--
-- Table structure for table `salescancel`
--

CREATE TABLE `salescancel` (
  `acyear` varchar(6) NOT NULL,
  `firm_id` varchar(1) NOT NULL DEFAULT '1',
  `order_no` varchar(10) NOT NULL,
  `order_date` date NOT NULL DEFAULT '0000-00-00',
  `cust_phone` varchar(10) NOT NULL,
  `prod_id` varchar(7) NOT NULL DEFAULT '',
  `size_id` varchar(5) NOT NULL DEFAULT '',
  `rate` decimal(11,2) NOT NULL DEFAULT 0.00,
  `qty` decimal(12,3) NOT NULL DEFAULT 0.000,
  `amount` decimal(15,2) NOT NULL DEFAULT 0.00,
  `cgstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `sgstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `igstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `cgstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `sgstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `igstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `deli_charg` decimal(11,2) NOT NULL DEFAULT 0.00,
  `status` varchar(20) NOT NULL,
  `reason` varchar(60) NOT NULL,
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `salescancel`
--

INSERT INTO `salescancel` (`acyear`, `firm_id`, `order_no`, `order_date`, `cust_phone`, `prod_id`, `size_id`, `rate`, `qty`, `amount`, `cgstper`, `sgstper`, `igstper`, `cgstamt`, `sgstamt`, `igstamt`, `deli_charg`, `status`, `reason`, `crea_date`, `crea_time`, `modi_date`, `modi_time`) VALUES
('202324', '1', 'AR00000001', '2023-08-01', '8438910200', '0300093', '0001', 25.00, 10.000, 250.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 'Cancel', 'I want to change the delivery address', '2023-08-01', '07:26:37', '2023-08-01', '07:26:37'),
('202324', '1', 'AR00000001', '2023-08-01', '8438910200', '0300108', '00001', 120.00, 5.000, 30.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 'Cancel', 'I want to change the delivery address', '2023-08-01', '07:26:37', '2023-08-01', '07:26:37'),
('202324', '1', 'AR00000002', '2023-08-01', '8438910200', '0300090', '0001', 30.00, 5.000, 150.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 'Cancel', 'I want to change the contact number', '2023-08-01', '07:27:49', '2023-08-01', '07:27:49'),
('202324', '1', 'AR00000002', '2023-08-01', '8438910200', '0300092', '0001', 35.00, 5.000, 175.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 'Cancel', 'I want to change the contact number', '2023-08-01', '07:27:49', '2023-08-01', '07:27:49'),
('202324', '1', 'AR00000001', '2023-08-01', '8438910200', '0300109', '00001', 30.00, 5.000, 8.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 'Cancel', 'I want to change the delivery address', '2023-08-01', '07:26:37', '2023-08-01', '07:26:37'),
('202324', '1', 'AR00000002', '2023-08-01', '8438910200', '0300086', '0001', 50.00, 6.000, 300.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 'Cancel', 'I want to change the contact number', '2023-08-01', '07:27:49', '2023-08-01', '07:27:49');

-- --------------------------------------------------------

--
-- Table structure for table `salesorder`
--

CREATE TABLE `salesorder` (
  `acyear` varchar(6) NOT NULL,
  `firm_id` varchar(1) NOT NULL DEFAULT '1',
  `order_no` varchar(10) NOT NULL,
  `order_date` date NOT NULL DEFAULT '0000-00-00',
  `cust_phone` varchar(10) NOT NULL,
  `prod_id` varchar(7) NOT NULL DEFAULT '',
  `size_id` varchar(5) NOT NULL DEFAULT '',
  `rate` decimal(11,2) NOT NULL DEFAULT 0.00,
  `qty` decimal(12,3) NOT NULL DEFAULT 0.000,
  `amount` decimal(15,2) NOT NULL DEFAULT 0.00,
  `cgstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `sgstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `igstper` decimal(6,2) NOT NULL DEFAULT 0.00,
  `cgstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `sgstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `igstamt` decimal(11,2) NOT NULL DEFAULT 0.00,
  `deli_charg` decimal(11,2) NOT NULL DEFAULT 0.00,
  `status` varchar(20) NOT NULL,
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `modi_date` date NOT NULL DEFAULT '0000-00-00',
  `modi_time` time NOT NULL DEFAULT '00:00:00'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `salesorder`
--

INSERT INTO `salesorder` (`acyear`, `firm_id`, `order_no`, `order_date`, `cust_phone`, `prod_id`, `size_id`, `rate`, `qty`, `amount`, `cgstper`, `sgstper`, `igstper`, `cgstamt`, `sgstamt`, `igstamt`, `deli_charg`, `status`, `crea_date`, `crea_time`, `modi_date`, `modi_time`) VALUES
('202324', '1', 'AR00000003', '2023-08-01', '8438910200', '0300117', '0001', 50.00, 4.000, 200.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 'Ordered', '2023-08-01', '10:19:15', '2023-08-01', '10:19:15'),
('202324', '1', 'AR00000003', '2023-08-01', '8438910200', '0300116', '0001', 600.00, 11.000, 6600.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 'Ordered', '2023-08-01', '10:19:15', '2023-08-01', '10:19:15');

-- --------------------------------------------------------

--
-- Table structure for table `sizemaster`
--

CREATE TABLE `sizemaster` (
  `size_id` varchar(5) NOT NULL DEFAULT '',
  `size_name` varchar(50) NOT NULL DEFAULT '',
  `short_name` varchar(10) NOT NULL DEFAULT '',
  `factor` decimal(10,3) NOT NULL DEFAULT 0.000,
  `active` int(1) NOT NULL DEFAULT 0,
  `crea_user` varchar(40) NOT NULL DEFAULT '',
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `crea_stat` varchar(10) NOT NULL DEFAULT '',
  `modi_user` varchar(40) NOT NULL DEFAULT '',
  `modi_date` date NOT NULL DEFAULT current_timestamp(),
  `modi_time` time NOT NULL DEFAULT current_timestamp(),
  `modi_stat` varchar(10) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `sizemaster`
--

INSERT INTO `sizemaster` (`size_id`, `size_name`, `short_name`, `factor`, `active`, `crea_user`, `crea_date`, `crea_time`, `crea_stat`, `modi_user`, `modi_date`, `modi_time`, `modi_stat`) VALUES
('00001', '50grm', '', 0.050, 0, '', '2023-07-28', '00:00:00', '', '', '2023-07-27', '11:27:33', ''),
('00002', '100grm', '', 0.100, 0, '', '2023-07-28', '00:00:00', '', '', '2023-07-27', '11:27:33', ''),
('00003', '250grm', '', 0.250, 0, '', '2023-07-28', '00:00:00', '', '', '2023-07-28', '15:48:20', ''),
('00004', '500grm', '', 0.500, 0, '', '2023-07-28', '00:00:00', '', '', '2023-07-28', '15:48:20', ''),
('00005', '750grm', '', 0.750, 0, '', '2023-07-28', '00:00:00', '', '', '2023-07-28', '15:48:20', ''),
('00006', '1KG', '', 1.000, 0, '', '0000-00-00', '00:00:00', '', '', '2023-07-28', '16:00:03', ''),
('00007', '5KG', '', 5.000, 0, '', '2023-07-29', '00:00:00', '', '', '2023-07-29', '07:03:19', ''),
('00008', '10KG', '', 10.000, 0, '', '2023-07-29', '00:00:00', '', '', '2023-07-29', '07:03:19', '');

-- --------------------------------------------------------

--
-- Table structure for table `statemaster`
--

CREATE TABLE `statemaster` (
  `state_id` varchar(2) NOT NULL,
  `state_name` varchar(40) NOT NULL,
  `short_name` varchar(10) NOT NULL,
  `stcode` varchar(40) NOT NULL,
  `crea_date` date NOT NULL DEFAULT '0000-00-00',
  `crea_time` time NOT NULL DEFAULT '00:00:00',
  `modi_date` date NOT NULL DEFAULT current_timestamp(),
  `modi_time` time NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `statemaster`
--

INSERT INTO `statemaster` (`state_id`, `state_name`, `short_name`, `stcode`, `crea_date`, `crea_time`, `modi_date`, `modi_time`) VALUES
('01', 'JAMMU & KASHMIR', 'JK', '01', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('02', 'HIMACHAL PRADESH', 'HP', '02', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('03', 'PUNJAB', 'PB', '03', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('04', 'CHANDIGARH', 'CH', '04', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('05', 'UTTARAKHAND', 'UT', '05', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('06', 'HARYANA', 'HR', '06', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('07', 'DELHI', 'DL', '07', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('08', 'RAJASTHAN', 'RJ', '08', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('09', 'UTTAR PRADESH', 'UP', '09', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('10', 'BIHAR', 'BR', '10', '0000-00-00', '00:00:00', '2023-07-30', '07:01:30'),
('11', 'SIKKIM', 'SK', '11', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('12', 'ARUNACHAL PRADESH', 'AR', '12', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('13', 'NAGALAND', 'NL', '13', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('14', 'MANIPUR', 'MN', '14', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('15', 'MIZORAM', 'MZ', '15', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('16', 'TRIPURA', 'TR', '16', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('17', 'MEGHALAYA', 'ML', '17', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('18', 'ASSAM', 'AS', '18', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('19', 'WEST BENGAL', 'WB', '19', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('20', 'JHARKHAND', 'JH', '20', '0000-00-00', '00:00:00', '2023-07-30', '07:05:56'),
('21', 'ODISHA', 'OR', '21', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('22', 'LAKSHDWEEP', 'LD', '22', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('23', 'CHHATTISGARH', 'CT', '23', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('24', 'MADHYA PRADESH', 'MP', '24', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('25', 'GUJARAT', 'GJ', '25', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('26', 'DAMAN & DIU', 'DD', '26', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('27', 'DADRA & NAGAR HAVELI', 'DN', '27', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('28', 'MAHARASHTRA', 'MH', '28', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('29', 'KARNATAKA', 'KA', '29', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('30', 'GOA', 'GA', '30', '0000-00-00', '00:00:00', '2023-07-30', '07:11:31'),
('31', 'KERALA', 'KL', '31', '0000-00-00', '00:00:00', '2023-07-30', '07:14:57'),
('32', 'TAMIL NADU', 'TN', '32', '0000-00-00', '00:00:00', '2023-07-30', '07:14:57'),
('33', 'PONDICHERRY', 'PY', '33', '0000-00-00', '00:00:00', '2023-07-30', '07:14:57'),
('34', 'ANDAMAN & NICOBAR ISLANDS', 'AN', '34', '0000-00-00', '00:00:00', '2023-07-30', '07:14:57'),
('35', 'TELENGANA', 'DG', '35', '0000-00-00', '00:00:00', '2023-07-30', '07:14:57'),
('36', 'ANDHRA PRADESH', 'AP', '36', '0000-00-00', '00:00:00', '2023-07-30', '07:14:57'),
('99', 'OTHER TERRITORY', 'OT', '99', '0000-00-00', '00:00:00', '2023-07-30', '07:14:57');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `acyear`
--
ALTER TABLE `acyear`
  ADD PRIMARY KEY (`year_id`),
  ADD UNIQUE KEY `year_id` (`year_id`);

--
-- Indexes for table `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`acyear`,`firm_id`,`order_no`),
  ADD UNIQUE KEY `acyear` (`acyear`,`firm_id`,`order_no`);

--
-- Indexes for table `cartorder`
--
ALTER TABLE `cartorder`
  ADD PRIMARY KEY (`acyear`,`firm_id`,`cust_phone`,`prod_id`) USING BTREE,
  ADD UNIQUE KEY `cartorder` (`acyear`,`firm_id`,`cust_phone`,`prod_id`) USING BTREE;

--
-- Indexes for table `cartprice`
--
ALTER TABLE `cartprice`
  ADD PRIMARY KEY (`cust_phone`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `cust_phone` (`cust_phone`);

--
-- Indexes for table `catemaster`
--
ALTER TABLE `catemaster`
  ADD PRIMARY KEY (`cate_name`) USING BTREE,
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `citymaster`
--
ALTER TABLE `citymaster`
  ADD PRIMARY KEY (`city_id`,`state_name`);

--
-- Indexes for table `firmmaster`
--
ALTER TABLE `firmmaster`
  ADD PRIMARY KEY (`firm_id`);

--
-- Indexes for table `otp`
--
ALTER TABLE `otp`
  ADD PRIMARY KEY (`cust_phone`) USING BTREE;

--
-- Indexes for table `pincode_master`
--
ALTER TABLE `pincode_master`
  ADD PRIMARY KEY (`pincode`),
  ADD UNIQUE KEY `pincode_id` (`pincode_id`);

--
-- Indexes for table `productmaster`
--
ALTER TABLE `productmaster`
  ADD PRIMARY KEY (`prod_name`),
  ADD UNIQUE KEY `Id` (`Id`) USING BTREE;

--
-- Indexes for table `productsize`
--
ALTER TABLE `productsize`
  ADD PRIMARY KEY (`eff_date`,`prod_id`,`size_id`),
  ADD UNIQUE KEY `eff_date` (`eff_date`,`prod_id`,`size_id`);

--
-- Indexes for table `register`
--
ALTER TABLE `register`
  ADD PRIMARY KEY (`cust_id`,`cust_phone`) USING BTREE,
  ADD UNIQUE KEY `cust_id` (`cust_id`),
  ADD UNIQUE KEY `cust_phone` (`cust_phone`);

--
-- Indexes for table `salescancel`
--
ALTER TABLE `salescancel`
  ADD PRIMARY KEY (`acyear`,`firm_id`,`order_no`,`cust_phone`,`prod_id`) USING BTREE,
  ADD UNIQUE KEY `acyear` (`acyear`,`firm_id`,`order_no`,`cust_phone`,`prod_id`) USING BTREE;

--
-- Indexes for table `salesorder`
--
ALTER TABLE `salesorder`
  ADD PRIMARY KEY (`acyear`,`firm_id`,`order_no`,`cust_phone`,`prod_id`) USING BTREE,
  ADD UNIQUE KEY `acyear` (`acyear`,`firm_id`,`order_no`,`cust_phone`,`prod_id`) USING BTREE;

--
-- Indexes for table `sizemaster`
--
ALTER TABLE `sizemaster`
  ADD PRIMARY KEY (`size_id`),
  ADD UNIQUE KEY `size_id` (`size_id`);

--
-- Indexes for table `statemaster`
--
ALTER TABLE `statemaster`
  ADD PRIMARY KEY (`state_id`,`state_name`),
  ADD UNIQUE KEY `state_id` (`state_id`),
  ADD UNIQUE KEY `state_name` (`state_name`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cartprice`
--
ALTER TABLE `cartprice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=284;

--
-- AUTO_INCREMENT for table `catemaster`
--
ALTER TABLE `catemaster`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=98;

--
-- AUTO_INCREMENT for table `pincode_master`
--
ALTER TABLE `pincode_master`
  MODIFY `pincode_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `productmaster`
--
ALTER TABLE `productmaster`
  MODIFY `Id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT for table `register`
--
ALTER TABLE `register`
  MODIFY `cust_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
