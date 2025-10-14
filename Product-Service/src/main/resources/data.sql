-- CATEGORIES
INSERT INTO categories (id, name, slug, description) VALUES ('b1a9a4f0-2f6b-4d2c-9ad9-4a9d6f3e1a01', 'Smartphones', 'smartphones', 'Moderna smartphones i olika prisklasser.');
INSERT INTO categories (id, name, slug, description) VALUES ('f4b63e94-3d26-4b02-9a63-9c9ca1c87d51', 'Laptops', 'laptops', 'Bärbara datorer för arbete och studier.');
INSERT INTO categories (id, name, slug, description) VALUES ('592f87a1-b5b0-49e8-bbe2-55a1efc6f6a2', 'Hörlurar', 'hoerlurar', 'In‑ear, over‑ear och true wireless.');
INSERT INTO categories (id, name, slug, description) VALUES ('a70f3d11-1c5a-4b49-9f25-5c7f2e55c090', 'Kameror', 'kameror', 'Kompakt- och systemkameror.');

-- PRODUCTS
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('f3c6c5b5-8b6a-4fdc-a0a0-5a3e6d6e7c01', 'Galaxy Nexus', 'galaxy-nexus', 'Google/Samsung Galaxy Nexus med AMOLED‑skärm.', 1590.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b1a9a4f0-2f6b-4d2c-9ad9-4a9d6f3e1a01');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('8be34a80-0d83-4a7f-9b57-86e2a1c78e02', 'Motorola Moto E (1st gen)', 'motorola-moto-e-1st-gen', 'Prisvärd Android‑mobil i bra skick.', 890.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b1a9a4f0-2f6b-4d2c-9ad9-4a9d6f3e1a01');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('2a5f9d9d-4a3c-4d42-8a78-3c7f7b9a3e03', 'Microsoft Lumia 950', 'microsoft-lumia-950', 'Lumia 950 med 20 MP kamera och USB‑C.', 1290.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b1a9a4f0-2f6b-4d2c-9ad9-4a9d6f3e1a01');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('f1a2b3c4-d5e6-4789-9a0b-c1d2e3f4a105', 'iPhone 4 (svart)', 'iphone-4-svart', 'Klassisk iPhone 4, 3.5” Retina‑skärm.', 590.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'b1a9a4f0-2f6b-4d2c-9ad9-4a9d6f3e1a01');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('3d7a9e21-1f45-4c1a-9f2f-66b9a1b2c406', 'MacBook Pro 17" (Intel)', 'macbook-pro-17-intel', '17‑tums MacBook Pro med stor skärm.', 4990.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'f4b63e94-3d26-4b02-9a63-9c9ca1c87d51');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('a4c2e423-9e32-44a0-8c22-8e2f6e044b07', 'Lenovo IdeaPad 320', 'lenovo-ideapad-320', 'Lenovo IdeaPad 320 – enkel vardagsdator.', 2690.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'f4b63e94-3d26-4b02-9a63-9c9ca1c87d51');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('7be0289f-fb2b-4a87-9c8e-7d2fa03c1c08', 'HP Pavilion dv2000', 'hp-pavilion-dv2000', 'HP Pavilion dv2000 – äldre men fungerande.', 1490.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'f4b63e94-3d26-4b02-9a63-9c9ca1c87d51');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('0c7a3c8a-6b0b-47f6-b24e-28cce7a3e709', 'ThinkPad X31/T43 (jämförelse)', 'thinkpad-x31-t43', 'Klassiska ThinkPad‑modeller för retro‑älskare.', 990.00, 'SEK', 6, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'f4b63e94-3d26-4b02-9a63-9c9ca1c87d51');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('0f1e2d3c-4b5a-4968-8c7d-9e0f1a2b3c09', 'Sennheiser HD555', 'sennheiser-hd555', 'Öppna over‑ear‑hörlurar för musik och spel.', 690.00, 'SEK', 2250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '592f87a1-b5b0-49e8-bbe2-55a1efc6f6a2');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b110', 'Phonak PFE112 In‑Ear', 'phonak-pfe112', 'In‑ear med silikonproppar, neutral signatur.', 390.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '592f87a1-b5b0-49e8-bbe2-55a1efc6f6a2');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('b2c3d4e5-f6a7-48b9-8c0d-e1f2a3b4c111', 'Vintage C525‑hörlurar', 'vintage-c525-horlurar', '70‑talsklassiker – vita, lätta och bekväma.', 290.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '592f87a1-b5b0-49e8-bbe2-55a1efc6f6a2');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('c3d4e5f6-a7b8-49ca-9d0e-f1a2b3c4d112', 'Samsung Galaxy Buds2', 'samsung-galaxy-buds2', 'True wireless med aktiv brusreducering.', 990.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '592f87a1-b5b0-49e8-bbe2-55a1efc6f6a2');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('d4e5f6a7-b8c9-40db-ae0f-12a3b4c5d113', 'Canon EOS 7D (hus)', 'canon-eos-7d-hus', 'Stabil DSLR‑kropp med magnesiumchassi.', 3490.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'a70f3d11-1c5a-4b49-9f25-5c7f2e55c090');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('e5f6a7b8-c9d0-41ec-bf10-23a4b5c6d114', 'Nikon Coolpix L19', 'nikon-coolpix-l19', 'Kompaktkamera – enkel att använda.', 490.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'a70f3d11-1c5a-4b49-9f25-5c7f2e55c090');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('f6a7b8c9-d0e1-42fd-8011-34b5c6d7e115', 'Nikon D7000 + 18‑105', 'nikon-d7000-18-105', 'DSLR‑kit med allround‑zoom.', 2990.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'a70f3d11-1c5a-4b49-9f25-5c7f2e55c090');
INSERT INTO products (id, name, slug, description, price, currency, stock_quantity, active, version, created_at, updated_at, category_id) VALUES ('a7b8c9d0-e1f2-430e-9122-45c6d7e8f116', 'Sony DSLR‑A700 + Tamron 11‑18', 'sony-dslr-a700-tamron-11-18', 'Sony DSLR‑A700 med vidvinkelzoom.', 2690.00, 'SEK', 250, TRUE, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'a70f3d11-1c5a-4b49-9f25-5c7f2e55c090');

-- PRODUCT IMAGES (position 0)
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('f3c6c5b5-8b6a-4fdc-a0a0-5a3e6d6e7c01', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Galaxy_Nexus_smartphone.jpg', 'Galaxy_Nexus_smartphone.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('8be34a80-0d83-4a7f-9b57-86e2a1c78e02', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Motorola_Moto_E.jpg', 'Motorola_Moto_E.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('2a5f9d9d-4a3c-4d42-8a78-3c7f7b9a3e03', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Lumia950.jpg', 'Lumia950.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('f1a2b3c4-d5e6-4789-9a0b-c1d2e3f4a105', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Iphone_4G-3_black_screen.png', 'Iphone_4G-3_black_screen.png');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('3d7a9e21-1f45-4c1a-9f2f-66b9a1b2c406', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/MacBook_Pro_17.jpg', 'MacBook_Pro_17.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('a4c2e423-9e32-44a0-8c22-8e2f6e044b07', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Lenovo_IdeaPad_320.jpg', 'Lenovo_IdeaPad_320.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('7be0289f-fb2b-4a87-9c8e-7d2fa03c1c08', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/HP_Pavilion_dv2000_laptop.jpg', 'HP_Pavilion_dv2000_laptop.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('0c7a3c8a-6b0b-47f6-b24e-28cce7a3e709', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/X31_T43_laptop.png', 'X31_T43_laptop.png');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('0f1e2d3c-4b5a-4968-8c7d-9e0f1a2b3c09', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Headphones-Sennheiser-HD555.jpg', 'Headphones-Sennheiser-HD555.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b110', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Phonak_PFE112_In-Ear_Headphones_1.jpg', 'Phonak_PFE112_In-Ear_Headphones_1.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('b2c3d4e5-f6a7-48b9-8c0d-e1f2a3b4c111', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Headphones_1.jpg', 'Headphones_1.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('c3d4e5f6-a7b8-49ca-9d0e-f1a2b3c4d112', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Samsung_Galaxy_Buds2_%28Docked%29.png', 'Samsung_Galaxy_Buds2_%28Docked%29.png');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('d4e5f6a7-b8c9-40db-ae0f-12a3b4c5d113', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Canon_EOS_7D_DSLR_body_front.jpg', 'Canon_EOS_7D_DSLR_body_front.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('e5f6a7b8-c9d0-41ec-bf10-23a4b5c6d114', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Nikon_Coolpix_L19.jpg', 'Nikon_Coolpix_L19.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('f6a7b8c9-d0e1-42fd-8011-34b5c6d7e115', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/Nikon_D7000%2B18-105_Lens.jpg', 'Nikon_D7000%2B18-105_Lens.jpg');
INSERT INTO product_images (product_id, position, url, file_name) VALUES ('a7b8c9d0-e1f2-430e-9122-45c6d7e8f116', 0, 'https://commons.wikimedia.org/wiki/Special:FilePath/DSLR-A700-Tamron11-18.jpeg', 'DSLR-A700-Tamron11-18.jpeg');

-- PRODUCT ATTRIBUTES
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('f3c6c5b5-8b6a-4fdc-a0a0-5a3e6d6e7c01', 'färg', 'svart');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('f3c6c5b5-8b6a-4fdc-a0a0-5a3e6d6e7c01', 'lagring', '32 GB');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('f3c6c5b5-8b6a-4fdc-a0a0-5a3e6d6e7c01', 'skärm', '4.65"');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('8be34a80-0d83-4a7f-9b57-86e2a1c78e02', 'färg', 'svart');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('8be34a80-0d83-4a7f-9b57-86e2a1c78e02', 'lagring', '8 GB');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('2a5f9d9d-4a3c-4d42-8a78-3c7f7b9a3e03', 'färg', 'svart');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('2a5f9d9d-4a3c-4d42-8a78-3c7f7b9a3e03', 'lagring', '32 GB');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('f1a2b3c4-d5e6-4789-9a0b-c1d2e3f4a105', 'färg', 'svart');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('f1a2b3c4-d5e6-4789-9a0b-c1d2e3f4a105', 'lagring', '16 GB');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('3d7a9e21-1f45-4c1a-9f2f-66b9a1b2c406', 'ram', '8 GB');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('3d7a9e21-1f45-4c1a-9f2f-66b9a1b2c406', 'lagring', '512 GB SSD');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('3d7a9e21-1f45-4c1a-9f2f-66b9a1b2c406', 'skärm', '17"');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('a4c2e423-9e32-44a0-8c22-8e2f6e044b07', 'ram', '8 GB');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('a4c2e423-9e32-44a0-8c22-8e2f6e044b07', 'lagring', '256 GB SSD');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('a4c2e423-9e32-44a0-8c22-8e2f6e044b07', 'skärm', '15.6"');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('7be0289f-fb2b-4a87-9c8e-7d2fa03c1c08', 'ram', '4 GB');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('7be0289f-fb2b-4a87-9c8e-7d2fa03c1c08', 'lagring', '160 GB HDD');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('7be0289f-fb2b-4a87-9c8e-7d2fa03c1c08', 'skärm', '14"');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('0c7a3c8a-6b0b-47f6-b24e-28cce7a3e709', 'ram', '2 GB');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('0c7a3c8a-6b0b-47f6-b24e-28cce7a3e709', 'lagring', '80 GB HDD');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('0f1e2d3c-4b5a-4968-8c7d-9e0f1a2b3c09', 'typ', 'over‑ear');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('0f1e2d3c-4b5a-4968-8c7d-9e0f1a2b3c09', 'kabel', '3.5 mm');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b110', 'typ', 'in‑ear');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b110', 'impedans', '32 Ω');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('b2c3d4e5-f6a7-48b9-8c0d-e1f2a3b4c111', 'typ', 'over‑ear');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('b2c3d4e5-f6a7-48b9-8c0d-e1f2a3b4c111', 'vikt', '250 g');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('c3d4e5f6-a7b8-49ca-9d0e-f1a2b3c4d112', 'typ', 'true wireless');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('c3d4e5f6-a7b8-49ca-9d0e-f1a2b3c4d112', 'bt', '5.2');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('d4e5f6a7-b8c9-40db-ae0f-12a3b4c5d113', 'sensor', '18 MP');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('d4e5f6a7-b8c9-40db-ae0f-12a3b4c5d113', 'fattning', 'EF');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('e5f6a7b8-c9d0-41ec-bf10-23a4b5c6d114', 'sensor', '10 MP');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('e5f6a7b8-c9d0-41ec-bf10-23a4b5c6d114', 'zoom', '3.6×');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('f6a7b8c9-d0e1-42fd-8011-34b5c6d7e115', 'sensor', '16 MP');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('f6a7b8c9-d0e1-42fd-8011-34b5c6d7e115', 'objektiv', '18‑105 mm');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('a7b8c9d0-e1f2-430e-9122-45c6d7e8f116', 'sensor', '12 MP');
INSERT INTO product_attributes (product_id, attr_key, attr_value) VALUES ('a7b8c9d0-e1f2-430e-9122-45c6d7e8f116', 'objektiv', '11‑18 mm');
