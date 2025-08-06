package com.groceries.Grocery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import groceries.GroceryApplication;
import groceries.GroceryList;

@SpringBootTest(classes = GroceryApplication.class)
class GroceryApplicationTests 
{
	// test that the GroceryList object gets the right # of columns 
	@Test
	@Order(3)
	void testNumColumns() 
	{
		GroceryList list = new GroceryList();
		assertEquals(8, list.getNumGroceries());
	}
	
	// test that the GroceryList object gets the right column names
	@Test
	@Order(4)
	void testColumnsGet()
	{
		GroceryList list = new GroceryList();
		ArrayList<String> columns = list.columns;
		
		assertEquals("item_name", columns.get(0));
		assertEquals("price", columns.get(1));
		assertEquals("quantity", columns.get(2));
		assertEquals("on_sale", columns.get(3));
	}
	
	// test that groceries from database were obtained by checking first and last rows
	@Test
	@Order(5)
	void obtainGroceries()
	{
		GroceryList list = new GroceryList();
		String[][] groceries = list.getGroceries();
		
		assertEquals("Apples", groceries[0][0]);
		assertEquals("0.78", groceries[0][1]);
		assertEquals("4", groceries[0][2]);
		assertEquals("FALSE", groceries[0][3]);
	}
	
	// tests that a row got successfully added to the groceries list
	@Test
	@Order(1)
	void insertTest()
	{
		GroceryList list = new GroceryList();
		list.addGrocery("Meatballs");
		String[][] groceries = list.getGroceries();
		
		assertEquals("Meatballs", groceries[8][0]);
		
		list.deleteGrocery("Meatballs");
	}
	
	// tests that row gets updated successfully
	@Test
	@Order(2)
	void updateTest()
	{
		String[] row = {"Meatballs", "1.01", "4", "TRUE"};
		
		GroceryList list = new GroceryList();
		list.addGrocery("Meatballs");
		
		list.updateGrocery(row);
		
		String[][] groceries = list.getGroceries();
		assertEquals("Meatballs", groceries[8][0]);
		assertEquals("1.01", groceries[8][1]);
		assertEquals("4", groceries[8][2]);
		assertEquals("TRUE", groceries[8][3]);
		
		list.deleteGrocery("Meatballs");
	}
}
