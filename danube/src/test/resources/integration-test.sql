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