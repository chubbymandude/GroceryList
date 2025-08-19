package com.groceries.Grocery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import application.Grocery;
import application.GroceryApplication;
import application.grocerylist.GroceryList;
import application.store.Store;

@SpringBootTest(classes = GroceryApplication.class)
class GroceryApplicationTests 
{
	
	// test that the GroceryList object gets the right column names
	@Test
	void testColumnsGet()
	{
		GroceryList list = new GroceryList();
		ArrayList<String> columns = list.columns;
		
		assertEquals("item_name", columns.get(0));
		assertEquals("price", columns.get(1));
		assertEquals("quantity", columns.get(2));
		assertEquals("on_sale", columns.get(3));
	}
	
	@Test
	void testStoreUpdate()
	{
		Store store = new Store();
		Grocery grocery = new Grocery("Apples", Float.valueOf("0.78"), 4, false);
		store.addItem(grocery); // increment by 1
	}
}
