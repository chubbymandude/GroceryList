package application.grocerylist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import application.Grocery;

// handles logic revolving around the grocery list route of the Grocery's website
@Service
public class GroceryList 
{
	// database information
	private static final String URL = System.getenv("DB_URL");
	private static final String USERNAME = System.getenv("DB_USERNAME");
	private static final String PASSWORD = System.getenv("DB_PASSWORD");
	
	// queries
	private static final String SELECT_ALL = 
		"SELECT item_name, price, quantity, on_sale\n" + 
		"FROM groceries\n" +
		"ORDER BY id";
	private static final String DELETE_ROW = 
		"DELETE FROM groceries\n" +
		"WHERE item_name = ?";
	private static final String MOVE_ITEM_TO_STORE = 
		"UPDATE store\n" +
		"SET quantity = quantity + ?\n" +
		"WHERE item_name = ?";
	
	private Connection connection; 
	public ArrayList<String> columns;
	
	{
		try { connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); }
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	public GroceryList()
	{
		columns = new ArrayList<>();
		getColumns();
	}
	
	// obtains columns associated with the grocery table
	private void getColumns()
	{
		try
		{
			PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL);
			ResultSet resultSet = selectAll.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();	
			
			int numColumns = metaData.getColumnCount();
			for(int col = 1; col <= numColumns; col++)
			{
				columns.add(metaData.getColumnName(col));
			}
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	// used to get all groceries and their information from database
	// returned as a List of Maps (which are rows) to convert to JSON later
	public List<LinkedHashMap<String, Object>> getGroceries()
	{
		try
		{
			PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL);
			ResultSet resultSet = selectAll.executeQuery();
			List<LinkedHashMap<String, Object>> rows = new ArrayList<>();
			
			// create a JSON array out of the database contents
			while(resultSet.next()) 
			{
				LinkedHashMap<String, Object> currRow = new LinkedHashMap<String, Object>();
				for(int col = 0; col < columns.size(); col++)
				{
					currRow.put(columns.get(col), resultSet.getObject(col + 1)); 
				}
				rows.add(currRow);
			}
			return rows;
		}
		catch(SQLException e) { e.printStackTrace(); } 
		// if an error occurred while preparing statement
		return null;
	}
	
	// removes the specified grocery from the grocery list
	// updates the store to reclaim the removed items
	public void removeGrocery(Grocery grocery)
	{
		try
		{
			// perform deletion from list
			PreparedStatement purchase = connection.prepareStatement(DELETE_ROW);
			purchase.setString(1, grocery.item_name());
			purchase.execute();
			// update the store with the items placed back into the store
			PreparedStatement updateStore = connection.prepareStatement(MOVE_ITEM_TO_STORE);
			updateStore.setInt(1, grocery.quantity());
			updateStore.setString(2, grocery.item_name());
			updateStore.execute();
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	// purchases the specified grocery from the grocery list
	// same as removeGrocery above except does not reclaim items to store
	public void purchaseGrocery(Grocery grocery)
	{
		try
		{
			// perform deletion from list
			PreparedStatement purchase = connection.prepareStatement(DELETE_ROW);
			purchase.setString(1, grocery.item_name());
			purchase.execute();
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
}