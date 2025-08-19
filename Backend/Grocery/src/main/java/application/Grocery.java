package application;

// represents a Grocery item from the database
public record Grocery(String item_name, Float price, Integer quantity, Boolean on_sale) { }
