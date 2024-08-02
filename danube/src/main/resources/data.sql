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
-- Insert details
INSERT INTO detail(id, name) VALUES(1, 'Size');
INSERT INTO detail(id, name) VALUES(2, 'Color');
INSERT INTO detail(id, name) VALUES(3, 'Material');
INSERT INTO detail(id, name) VALUES(4, 'Style');
INSERT INTO detail(id, name) VALUES(5, 'Fit');
INSERT INTO detail(id, name) VALUES(6, 'Dimensions');
INSERT INTO detail(id, name) VALUES(7, 'Weight');
INSERT INTO detail(id, name) VALUES(8, 'Brand');
INSERT INTO detail(id, name) VALUES(9, 'Capacity');
INSERT INTO detail(id, name) VALUES(10, 'Power');
INSERT INTO detail(id, name) VALUES(11, 'Resolution');
INSERT INTO detail(id, name) VALUES(12, 'Operating System');
INSERT INTO detail(id, name) VALUES(13, 'Battery Life');
INSERT INTO detail(id, name) VALUES(14, 'Screen Size');
INSERT INTO detail(id, name) VALUES(15, 'Fragrance');
INSERT INTO detail(id, name) VALUES(16, 'SPF');
INSERT INTO detail(id, name) VALUES(17, 'Voltage');
INSERT INTO detail(id, name) VALUES(18, 'Energy Rating');
INSERT INTO detail(id, name) VALUES(19, 'Waterproof');
INSERT INTO detail(id, name) VALUES(20, 'Warranty');
INSERT INTO detail(id, name) VALUES(21, 'Connectivity');
INSERT INTO detail(id, name) VALUES(22, 'Processor');
INSERT INTO detail(id, name) VALUES(23, 'RAM');
INSERT INTO detail(id, name) VALUES(24, 'Storage');
INSERT INTO detail(id, name) VALUES(25, 'Refresh Rate');
INSERT INTO detail(id, name) VALUES(26, 'Cooking Time');
INSERT INTO detail(id, name) VALUES(27, 'Temperature Control');
INSERT INTO detail(id, name) VALUES(28, 'Safety Features');
INSERT INTO detail(id, name) VALUES(29, 'Compatibility');
INSERT INTO detail(id, name) VALUES(30, 'Noise Level');

-- Subcategory details
-- Shirt
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (1, 1, 1); -- Size
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 2, 1); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 3, 1); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (4, 4, 1); -- Style
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (5, 5, 1); -- Fit

-- Pants
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (1, 8, 2); -- Size
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 6, 2); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 7, 2); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (4, 5, 2); -- Style
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (5, 9, 2); -- Fit

-- Jeans
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (1, 13, 3); -- Size
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 11, 3); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 12, 3); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (4, 10, 3); -- Style
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (5, 14, 3); -- Fit

-- Coat
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (1, 15, 4); -- Size
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 16, 4); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 17, 4); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (4, 18, 4); -- Style
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (5, 19, 4); -- Fit

-- Kitchen furniture
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 25, 6); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 26, 6); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (6, 27, 6); -- Dimensions

-- Sink
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 28, 7); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 29, 7); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (6, 30, 7); -- Dimensions

-- Tap
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 31, 8); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 32, 8); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (19, 33, 8); -- Waterproof

-- Kitchen appliances
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 34, 9); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 35, 9); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (9, 36, 9); -- Capacity
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (10, 37, 9); -- Power
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (20, 38, 9); -- Warranty

-- Kitchen textile
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 39, 10); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 40, 10); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (19, 41, 10); -- Waterproof

-- Camera
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 42, 11); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 43, 11); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (11, 44, 11); -- Resolution
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (7, 45, 11); -- Weight
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (20, 46, 11); -- Warranty

-- Television
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 47, 12); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 48, 12); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (11, 49, 12); -- Resolution
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (14, 50, 12); -- Screen Size
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (25, 51, 12); -- Refresh Rate

-- Laptop
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 52, 13); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 53, 13); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (12, 54, 13); -- Operating System
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (13, 55, 13); -- Battery Life
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (24, 56, 13); -- Storage

-- Mobile phone
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 57, 14); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 58, 14); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (12, 59, 14); -- Operating System
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (13, 60, 14); -- Battery Life
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (14, 61, 14); -- Screen Size

-- Tablet
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 62, 15); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 63, 15); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (12, 64, 15); -- Operating System
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (13, 65, 15); -- Battery Life
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (14, 66, 15); -- Screen Size

-- PC
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 67, 16); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 68, 16); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (22, 69, 16); -- Processor
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (23, 70, 16); -- RAM
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (24, 71, 16); -- Storage

-- Shower gel
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 72, 17); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 73, 17); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (15, 74, 17); -- Fragrance
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (28, 75, 17); -- Safety Features

-- Liquid shampoo
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 76, 18); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 77, 18); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (15, 78, 18); -- Fragrance
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (28, 79, 18); -- Safety Features

-- Deodorant
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 80, 19); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 81, 19); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (15, 82, 19); -- Fragrance
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (19, 83, 19); -- Waterproof

-- Sunscreen
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (3, 84, 20); -- Material
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (2, 85, 20); -- Color
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (15, 86, 20); -- Fragrance
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (16, 87, 20); -- SPF
INSERT INTO subcategory_detail(detail_id, id, subcategory_id) VALUES (28, 88, 20); -- Safety Features