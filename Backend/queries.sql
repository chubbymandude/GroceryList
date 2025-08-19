/* reset table if needed */
DROP TABLE store;

/* table which consists of grocery items in a store */
CREATE TABLE store
(
	id bigserial PRIMARY KEY,
	item_name text,
	price numeric(5, 2), /* grocery items cannot exceed $999.9 */
	quantity integer, 
	on_sale boolean,
	CONSTRAINT item_name_key UNIQUE (item_name) /* items can be searched by their name */
)

/* import from a local file */
COPY store (item_name, price, quantity, on_sale)
FROM '/Users/abdurrafayatif/Downloads/Right Angle Solutions/Groceries/Backend/Grocery List - Groceries.csv'
WITH (FORMAT CSV, HEADER);

/* check that table has correct items */
SELECT * FROM store ORDER BY id;

/* reset user groceries if needed */
DROP TABLE groceries;

/* create table which consists of information about a user's groceries */
CREATE TABLE groceries
(
	id bigserial PRIMARY KEY,
	item_name text,
	price numeric(5, 2), /* grocery items cannot exceed $999.9 */
	quantity integer, 
	on_sale boolean
)

/* check that user's groceries has correct items */
SELECT * FROM groceries ORDER BY id;