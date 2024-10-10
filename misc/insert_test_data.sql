-- Einfügen der Testdaten für die Datenbank

-- Schritt 1: Einfügen der `places`
INSERT or ignore INTO `places` (id, name) VALUES
(1, 'Computermuseum 1. OG'),
(2, 'C12');

-- Schritt 2

-- A (optional): Löschen der bisherigen `locations`
delete from `locations`;

-- B Einfügen der `locations`
INSERT or ignore INTO `locations` (id, name, important, places_id) VALUES
(1, 'raum71', 0, 2),
(2, 'büro chef', 1, 2),
(3, 'raum 70', 0, 2),
(4, 'raum 69', 0, 2),
(5, 'wc herren informatik', 0, 2),
(6, 'raum 61', 0, 1),
(7, 'wendeltreppe informatik', 0, 2),
(8, 'fb maschinenwesen 1', 1, 2),
(9, 'raum 62', 0, 2),
(10, 'raum 63', 0, 2),
(11, 'raum 64', 0, 2),
(12, 'raum 65', 0, 2),
(13, 'treppenhaus fb medien', 0, 2),
(14, 'fahrstuhl fb medien', 0, 2),
(15, 'fb medien 1', 1, 2),
(16, 'wc herren fb medien', 0, 2),
(17, 'wc damen fb medien', 0, 2),
(18, 'fb medien 2', 1, 2),
(19, 'raum 14', 0, 2),
(20, 'wasserautomat', 0, 2),
(21, 'kaffeeautomat', 0, 2),
(22, 'snackautomat', 0, 2),
(23, 'fb maschinenwesen 2', 1, 2),
(24, 'lastenfahrstuhl', 0, 2);

-- Schritt 3: Generieren von `texts`

INSERT or ignore INTO `texts` (id, title, text, detailed, locations_id) VALUES
(1, 'Detaillierte Beschreibung von raum71', 'Hier steht eine ausführliche Beschreibung von raum71.', 1, 1),
(2, 'Kurze Info zu raum71', 'raum71 in Kürze.', 0, 1),
(3, 'Detaillierte Beschreibung von büro chef', 'Hier steht eine ausführliche Beschreibung von büro chef.', 1, 2),
(4, 'Kurze Info zu büro chef', 'büro chef in Kürze.', 0, 2),
(5, 'Detaillierte Beschreibung von raum 70', 'Hier steht eine ausführliche Beschreibung von raum 70.', 1, 3),
(6, 'Kurze Info zu raum 70', 'raum 70 in Kürze.', 0, 3),
(7, 'Detaillierte Beschreibung von raum 69', 'Hier steht eine ausführliche Beschreibung von raum 69.', 1, 4),
(8, 'Kurze Info zu raum 69', 'raum 69 in Kürze.', 0, 4),
(9, 'Detaillierte Beschreibung von wc herren informatik', 'Hier steht eine ausführliche Beschreibung von wc herren informatik.', 1, 5),
(10, 'Kurze Info zu wc herren informatik', 'wc herren informatik in Kürze.', 0, 5),
(11, 'Detaillierte Beschreibung von raum 61', 'Hier steht eine ausführliche Beschreibung von raum 61.', 1, 6),
(12, 'Kurze Info zu raum 61', 'raum 61 in Kürze.', 0, 6),
(13, 'Detaillierte Beschreibung von wendeltreppe informatik', 'Hier steht eine ausführliche Beschreibung von wendeltreppe informatik.', 1, 7),
(14, 'Kurze Info zu wendeltreppe informatik', 'wendeltreppe informatik in Kürze.', 0, 7),
(15, 'Detaillierte Beschreibung von fb maschinenwesen 1', 'Hier steht eine ausführliche Beschreibung von fb maschinenwesen 1.', 1, 8),
(16, 'Kurze Info zu fb maschinenwesen 1', 'fb maschinenwesen 1 in Kürze.', 0, 8),
(17, 'Detaillierte Beschreibung von raum 62', 'Hier steht eine ausführliche Beschreibung von raum 62.', 1, 9),
(18, 'Kurze Info zu raum 62', 'raum 62 in Kürze.', 0, 9),
(19, 'Detaillierte Beschreibung von raum 63', 'Hier steht eine ausführliche Beschreibung von raum 63.', 1, 10),
(20, 'Kurze Info zu raum 63', 'raum 63 in Kürze.', 0, 10),
(21, 'Detaillierte Beschreibung von raum 64', 'Hier steht eine ausführliche Beschreibung von raum 64.', 1, 11),
(22, 'Kurze Info zu raum 64', 'raum 64 in Kürze.', 0, 11),
(23, 'Detaillierte Beschreibung von raum 65', 'Hier steht eine ausführliche Beschreibung von raum 65.', 1, 12),
(24, 'Kurze Info zu raum 65', 'raum 65 in Kürze.', 0, 12),
(25, 'Detaillierte Beschreibung von treppenhaus fb medien', 'Hier steht eine ausführliche Beschreibung von treppenhaus fb medien.', 1, 13),
(26, 'Kurze Info zu treppenhaus fb medien', 'treppenhaus fb medien in Kürze.', 0, 13),
(27, 'Detaillierte Beschreibung von fahrstuhl fb medien', 'Hier steht eine ausführliche Beschreibung von fahrstuhl fb medien.', 1, 14),
(28, 'Kurze Info zu fahrstuhl fb medien', 'fahrstuhl fb medien in Kürze.', 0, 14),
(29, 'Detaillierte Beschreibung von fb medien 1', 'Hier steht eine ausführliche Beschreibung von fb medien 1.', 1, 15),
(30, 'Kurze Info zu fb medien 1', 'fb medien 1 in Kürze.', 0, 15),
(31, 'Detaillierte Beschreibung von wc herren fb medien', 'Hier steht eine ausführliche Beschreibung von wc herren fb medien.', 1, 16),
(32, 'Kurze Info zu wc herren fb medien', 'wc herren fb medien in Kürze.', 0, 16),
(33, 'Detaillierte Beschreibung von wc damen fb medien', 'Hier steht eine ausführliche Beschreibung von wc damen fb medien.', 1, 17),
(34, 'Kurze Info zu wc damen fb medien', 'wc damen fb medien in Kürze.', 0, 17),
(35, 'Detaillierte Beschreibung von fb medien 2', 'Hier steht eine ausführliche Beschreibung von fb medien 2.', 1, 18),
(36, 'Kurze Info zu fb medien 2', 'fb medien 2 in Kürze.', 0, 18),
(37, 'Detaillierte Beschreibung von raum 14', 'Hier steht eine ausführliche Beschreibung von raum 14.', 1, 19),
(38, 'Kurze Info zu raum 14', 'raum 14 in Kürze.', 0, 19),
(39, 'Detaillierte Beschreibung von wasserautomat', 'Hier steht eine ausführliche Beschreibung von wasserautomat.', 1, 20),
(40, 'Kurze Info zu wasserautomat', 'wasserautomat in Kürze.', 0, 20),
(41, 'Detaillierte Beschreibung von kaffeeautomat', 'Hier steht eine ausführliche Beschreibung von kaffeeautomat.', 1, 21),
(42, 'Kurze Info zu kaffeeautomat', 'kaffeeautomat in Kürze.', 0, 21),
(43, 'Detaillierte Beschreibung von snackautomat', 'Hier steht eine ausführliche Beschreibung von snackautomat.', 1, 22),
(44, 'Kurze Info zu snackautomat', 'snackautomat in Kürze.', 0, 22),
(45, 'Detaillierte Beschreibung von fb maschinenwesen 2', 'Hier steht eine ausführliche Beschreibung von fb maschinenwesen 2.', 1, 23),
(46, 'Kurze Info zu fb maschinenwesen 2', 'fb maschinenwesen 2 in Kürze.', 0, 23),
(47, 'Detaillierte Beschreibung von lastenfahrstuhl', 'Hier steht eine ausführliche Beschreibung von lastenfahrstuhl.', 1, 24),
(48, 'Kurze Info zu lastenfahrstuhl', 'lastenfahrstuhl in Kürze.', 0, 24);

-- Schritt 4: Einfügen der `transfers`
-- Einfügen der langen Tour
INSERT or ignore INTO `transfers` (id, title, location_from, location_to) VALUES
(1, 'long_tour_station_01', 1, 2),
(2, 'long_tour_station_02', 2, 3),
(3, 'long_tour_station_03', 3, 4),
(4, 'long_tour_station_04', 4, 5),
(5, 'long_tour_station_05', 5, 6),
(6, 'long_tour_station_06', 6, 7),
(7, 'long_tour_station_07', 7, 8),
(8, 'long_tour_station_08', 8, 9),
(9, 'long_tour_station_09', 9, 10),
(10, 'long_tour_station_10', 10, 11),
(11, 'long_tour_station_11', 11, 12),
(12, 'long_tour_station_12', 12, 13),
(13, 'long_tour_station_13', 13, 14),
(14, 'long_tour_station_14', 14, 15),
(15, 'long_tour_station_15', 15, 16),
(16, 'long_tour_station_16', 16, 17),
(17, 'long_tour_station_17', 17, 18),
(18, 'long_tour_station_18', 18, 19),
(19, 'long_tour_station_19', 19, 20),
(20, 'long_tour_station_20', 20, 21),
(21, 'long_tour_station_21', 21, 22),
(22, 'long_tour_station_22', 22, 23),
(23, 'long_tour_station_23', 23, 24);

-- Einfügen der kurzen Tour
INSERT or ignore INTO `transfers` (id, title, location_from, location_to) VALUES
(24, 'short_tour_station_01', 1, 3),
(25, 'short_tour_station_02', 3, 5),
(26, 'short_tour_station_03', 5, 7),
(27, 'short_tour_station_04', 7, 9),
(28, 'short_tour_station_05', 9, 11),
(29, 'short_tour_station_06', 11, 13),
(30, 'short_tour_station_07', 13, 15),
(31, 'short_tour_station_08', 15, 17),
(32, 'short_tour_station_09', 17, 19),
(33, 'short_tour_station_10', 19, 21),
(34, 'short_tour_station_11', 21, 23);

