-- Add version column for all tables allowing optimistic locking
ALTER TABLE actor     ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE category  ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE city      ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE country   ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE customer  ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE film      ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE inventory ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE p_address ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE payment   ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE rental    ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE staff     ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE store     ADD version int DEFAULT 1 NOT NULL;

-- Fix sequence for all sequences avoid duplicate keys
SELECT setval('actor_actor_id_seq', 
			  (SELECT max(actor_id) FROM actor));
SELECT setval('category_category_id_seq', 
              (SELECT max(category_id) FROM category));
SELECT setval('city_city_id_seq', 
              (SELECT max(city_id) FROM city));
SELECT setval('country_country_id_seq', 
              (SELECT max(country_id) FROM country));
SELECT setval('customer_customer_id_seq', 
              (SELECT max(customer_id) FROM customer));
SELECT setval('film_film_id_seq', 
              (SELECT max(film_id) FROM film));
SELECT setval('inventory_inventory_id_seq', 
              (SELECT max(inventory_id) FROM inventory));
SELECT setval('p_address_p_address_id_seq', 
              (SELECT max(p_address_id) FROM p_address));
SELECT setval('payment_payment_id_seq', 
              (SELECT max(payment_id) FROM payment));
SELECT setval('staff_staff_id_seq', 
              (SELECT max(staff_id) FROM staff));
SELECT setval('store_store_id_seq', 
              (SELECT max(store_id) FROM store));
