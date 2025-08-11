package com.groceries.Grocery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import groceries.GroceryApplication;
import groceries.GroceryList;

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
	
	// tests the output of the JSON string as a result of the getGroceries method
	@Test
	void testJSONString() throws JsonProcessingException
	{
		GroceryList list = new GroceryList();
		List<LinkedHashMap<String, Object>> map = list.getGroceries();
		System.out.println(new ObjectMapper().writeValueAsString(map));
	}	
}
