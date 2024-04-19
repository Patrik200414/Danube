--categories
INSERT INTO category(id, name) VALUES(1, 'Clothing');
INSERT INTO category(id, name) VALUES(2, 'Home and kitchen');
INSERT INTO category(id, name) VALUES(3, 'Electronics');
INSERT INTO category(id, name) VALUES(4, 'Personal care');


--subcategories
--Clothing
INSERT INTO subcategory(id, name, category_id) VALUES(1, 'Shirt', 1);
INSERT INTO subcategory(id, name, category_id) VALUES(2, 'Pants', 1);
INSERT INTO subcategory(id, name, category_id) VALUES(3, 'Jeans', 1);
INSERT INTO subcategory(id, name, category_id) VALUES(4, 'Coat', 1);
INSERT INTO subcategory(id, name, category_id) VALUES(5, 'Jumper', 1);

--Home and kitchen
INSERT INTO subcategory(id, name, category_id) VALUES(6, 'Kitchen furniture', 2);
INSERT INTO subcategory(id, name, category_id) VALUES(7, 'Sink', 2);
INSERT INTO subcategory(id, name, category_id) VALUES(8, 'Tap', 2);
INSERT INTO subcategory(id, name, category_id) VALUES(9, 'Kitchen appliances', 2);
INSERT INTO subcategory(id, name, category_id) VALUES(10, 'Kitchen textile', 2);

--Electronics
INSERT INTO subcategory(id, name, category_id) VALUES(11, 'Camera', 3);
INSERT INTO subcategory(id, name, category_id) VALUES(12, 'Television', 3);
INSERT INTO subcategory(id, name, category_id) VALUES(13, 'Laptop', 3);
INSERT INTO subcategory(id, name, category_id) VALUES(14, 'Mobile phone', 3);
INSERT INTO subcategory(id, name, category_id) VALUES(15, 'Tablet', 3);
INSERT INTO subcategory(id, name, category_id) VALUES(16, 'PC', 3);

--Personal Care
INSERT INTO subcategory(id, name, category_id) VALUES(17, 'Shower gel', 4);
INSERT INTO subcategory(id, name, category_id) VALUES(18, 'Liquid shampoo', 4);
INSERT INTO subcategory(id, name, category_id) VALUES(19, 'Deodorant', 4);
INSERT INTO subcategory(id, name, category_id) VALUES(20, 'Sunscreen', 4);

--Details
INSERT INTO detail(id, name) VALUES(1, 'Size');
INSERT INTO detail(id, name) VALUES(2, 'Color');
INSERT INTO detail(id, name) VALUES(3, 'Material');
INSERT INTO detail(id, name) VALUES(4, 'Style');
INSERT INTO detail(id, name) VALUES(5, 'Fit');


--Subcategory_detail
--Shirt
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (1, 1, 1);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 2, 1);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 3, 1);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (4, 4, 1);

--Pants
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (4, 5, 2);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 6, 2);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 7, 2);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (1, 8, 2);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (5, 9, 2);

--Jeans
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (4, 10, 3);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 11, 3);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 12, 3);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (1, 13, 3);
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (5, 14, 3);
/*
--Coat
INSERT INTO detail(id, name, subcategory_id) VALUES(15, 'Style', 4);
INSERT INTO detail(id, name, subcategory_id) VALUES(16, 'Color', 4);
INSERT INTO detail(id, name, subcategory_id) VALUES(17, 'Material', 4);
INSERT INTO detail(id, name, subcategory_id) VALUES(18, 'Size', 4);
INSERT INTO detail(id, name, subcategory_id) VALUES(19, 'Fit', 4);
--Jumpers
INSERT INTO detail(id, name, subcategory_id) VALUES(20, 'Style', 5);
INSERT INTO detail(id, name, subcategory_id) VALUES(21, 'Color', 5);
INSERT INTO detail(id, name, subcategory_id) VALUES(22, 'Material', 5);
INSERT INTO detail(id, name, subcategory_id) VALUES(23, 'Size', 5);
INSERT INTO detail(id, name, subcategory_id) VALUES(24, 'Fit', 5);


--Home and kitchen
--Kitchen furniture
INSERT INTO detail(id, name, subcategory_id) VALUES(25, 'Type', 6);
INSERT INTO detail(id, name, subcategory_id) VALUES(26, 'Style', 6);
INSERT INTO detail(id, name, subcategory_id) VALUES(27, 'Material', 6);
INSERT INTO detail(id, name, subcategory_id) VALUES(28, 'Color', 6);
INSERT INTO detail(id, name, subcategory_id) VALUES(29, 'Width', 6);
INSERT INTO detail(id, name, subcategory_id) VALUES(30, 'Depth', 6);
INSERT INTO detail(id, name, subcategory_id) VALUES(31, 'Height', 6);
--Sink
INSERT INTO detail(id, name, subcategory_id) VALUES(32, 'Type', 7);
INSERT INTO detail(id, name, subcategory_id) VALUES(33, 'Style', 7);
INSERT INTO detail(id, name, subcategory_id) VALUES(34, 'Material', 7);
INSERT INTO detail(id, name, subcategory_id) VALUES(35, 'Color', 7);
INSERT INTO detail(id, name, subcategory_id) VALUES(36, 'Width', 7);
INSERT INTO detail(id, name, subcategory_id) VALUES(37, 'Depth', 7);
INSERT INTO detail(id, name, subcategory_id) VALUES(38, 'Height', 7);
--Tap
INSERT INTO detail(id, name, subcategory_id) VALUES(39, 'Type', 8);
INSERT INTO detail(id, name, subcategory_id) VALUES(40, 'Style', 8);
INSERT INTO detail(id, name, subcategory_id) VALUES(41, 'Finish', 8);
INSERT INTO detail(id, name, subcategory_id) VALUES(42, 'Height', 8);
INSERT INTO detail(id, name, subcategory_id) VALUES(43, 'Reach', 8);
--Kitchen appliances
INSERT INTO detail(id, name, subcategory_id) VALUES(44, 'Type', 9);
INSERT INTO detail(id, name, subcategory_id) VALUES(45, 'Model', 9);
INSERT INTO detail(id, name, subcategory_id) VALUES(46, 'Color', 9);
INSERT INTO detail(id, name, subcategory_id) VALUES(47, 'Capacity', 9);
INSERT INTO detail(id, name, subcategory_id) VALUES(48, 'Width', 9);
INSERT INTO detail(id, name, subcategory_id) VALUES(49, 'Depth', 9);
INSERT INTO detail(id, name, subcategory_id) VALUES(50, 'Height', 9);
--Kitchen textile
INSERT INTO detail(id, name, subcategory_id) VALUES(51, 'Type', 10);
INSERT INTO detail(id, name, subcategory_id) VALUES(51, 'Style', 10);
INSERT INTO detail(id, name, subcategory_id) VALUES(53, 'Material', 10);
INSERT INTO detail(id, name, subcategory_id) VALUES(54, 'Color', 10);
INSERT INTO detail(id, name, subcategory_id) VALUES(55, 'Height', 10);
INSERT INTO detail(id, name, subcategory_id) VALUES(56, 'Width', 10);
 */