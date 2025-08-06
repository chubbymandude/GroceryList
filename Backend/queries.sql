/* reset table contents if needed */
DROP TABLE groceries;

/* create table which consists of information about a user's groceries */
CREATE TABLE groceries
(
	item_name text PRIMARY KEY,
	price numeric(5, 2), /* grocery items cannot exceed $999.9 */
	quantity integer,
	on_sale boolean,
	CONSTRAINT item_name_key UNIQUE (item_name) /* items can be searched by their name */
);

/* import from a local file */
COPY groceries
FROM '...'
WITH (FORMAT CSV, HEADER);

/* check that table has correct items */
SELECT * FROM groceries;

/* add index to the table to item_name so queries are efficient */
CREATE INDEX item_index ON groceries(item_name);

/* check if index is being used */
EXPLAIN SELECT item_name
FROM groceries
WHERE item_name = 'Apples';
