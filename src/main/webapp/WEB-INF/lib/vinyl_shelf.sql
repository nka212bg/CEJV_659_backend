-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 21 май 2019 в 04:10
-- Версия на сървъра: 5.7.25-0ubuntu0.16.04.2
-- PHP Version: 7.0.33-0ubuntu0.16.04.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `vinyl_shelf`
--

-- --------------------------------------------------------

--
-- Структура на таблица `collections`
--

CREATE TABLE `collections` (
  `collection_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `collection_name` varchar(150) NOT NULL,
  `collection_genre` varchar(150) NOT NULL,
  `collection_cover` varchar(500) DEFAULT NULL,
  `collection_note` varchar(5000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Схема на данните от таблица `collections`
--

INSERT INTO `collections` (`collection_id`, `user_id`, `collection_name`, `collection_genre`, `collection_cover`, `collection_note`) VALUES
(2, 2, 'Metallica', 'Metala', 'https://cdn.shopify.com/s/files/1/1739/2771/products/vinylbundle4_1024x1024.jpg?v=1494490360', 'Turn each fe');

-- --------------------------------------------------------

--
-- Структура на таблица `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_password` varchar(50) NOT NULL,
  `user_avatar` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Схема на данните от таблица `users`
--

INSERT INTO `users` (`user_id`, `user_email`, `user_name`, `user_password`, `user_avatar`) VALUES
(2, 'nka212bg@gmail.com', 'Pesho user 2', 'qqqqqq', 'https://i.dailymail.co.uk/i/pix/2015/09/01/18/2BE1E88B00000578-3218613-image-m-5_1441127035222.jpg');

-- --------------------------------------------------------

--
-- Структура на таблица `vinyls`
--

CREATE TABLE `vinyls` (
  `vinyl_id` int(11) NOT NULL,
  `collection_id` int(11) NOT NULL,
  `vinyl_artist_name` varchar(150) NOT NULL,
  `vinyl_album_name` varchar(150) NOT NULL,
  `vinyl_album_cover` varchar(500) DEFAULT NULL,
  `vinyl_year` int(5) NOT NULL,
  `vinyl_upc_code` int(11) DEFAULT NULL,
  `vinyl_condition` int(7) NOT NULL,
  `vinyl_note` varchar(5000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Схема на данните от таблица `vinyls`
--

INSERT INTO `vinyls` (`vinyl_id`, `collection_id`, `vinyl_artist_name`, `vinyl_album_name`, `vinyl_album_cover`, `vinyl_year`, `vinyl_upc_code`, `vinyl_condition`, `vinyl_note`) VALUES
(5, 2, 'Metallica', 'Master of Puppets', 'https://images-na.ssl-images-amazon.com/images/I/71sW5Vf3KaL.jpg', 1989, 2245784, 5, 'Released on March 3, 1986, Metallica’s Master of Puppets album became an instant classic and announced the band as the most electrifying new voice in rock. Thirty years later, this six-time platinum album is considered to be the high-water mark of Metallica’s incredible career, with songs like “Battery,” “Welcome Home (Sanitarium),” and the title track, “Master of Puppets,” still a staple of their sell-out live shows. Sadly, this hugely successful period for Metallica was marred by a tragedy that shook the band to its foundation: the death of bassist Cliff Burton in a tour bus accident on September 27, 1986.'),
(6, 2, 'PINK FLOYD', 'Dark Side of the Moon', 'https://images-na.ssl-images-amazon.com/images/I/519GtlMPY2L._SL1500_.jpg', 1978, 451278, 5, 'Pink Floyd were an English rock band formed in London in 1965. They achieved international acclaim with their progressive and psychedelic music.'),
(7, 2, 'rage against the machine', 'Eponymous', 'https://i2.wp.com/www.feelnumb.com/wp-content/uploads/2011/01/album-cover-rage-against-the-machine-evil-empire.jpg', 1992, 2245784, 5, 'We have now truly delved into classic territory. Even before Killing in the Name‘s Christmas No. 1 stint this album was an absolute classic and a ferocious debut like an explosion in the music industry of the early 90’s. This album is an unstoppable machine. The raw energy, power and aggression of the ragtag group is perfectly represented in this 10-track petrol bomb of a rock record with its understated production and un-tampered-with riffs.'),
(8, 2, 'scor', 'whe', 'https://media.giphy.com/media/ZqlvCTNHpqrio/giphy.gif', 1978, 55458, 5, 'Pink Floyd w');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `collections`
--
ALTER TABLE `collections`
  ADD PRIMARY KEY (`collection_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_email` (`user_email`);

--
-- Indexes for table `vinyls`
--
ALTER TABLE `vinyls`
  ADD PRIMARY KEY (`vinyl_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `collections`
--
ALTER TABLE `collections`
  MODIFY `collection_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;
--
-- AUTO_INCREMENT for table `vinyls`
--
ALTER TABLE `vinyls`
  MODIFY `vinyl_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
