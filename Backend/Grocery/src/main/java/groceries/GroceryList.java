package groceries;

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

// class connects to database via JDBC to obtain grocery list and associated items
@Service
public class GroceryList 
{
	// an inner class that represents components of the JSON body for an individual Grocery item
	public record Grocery(String item_name, Float price, Integer quantity, Boolean on_sale) { }
	
	// database information
	private static final String URL = System.getenv("DB_URL");
	private static final String USERNAME = System.getenv("DB_USERNAME");
	private static final String PASSWORD = System.getenv("DB_PASSWORD");
	
	// queries
	private static final String SELECT_ALL = 
		"SELECT item_name, price, quantity, on_sale\n" + 
		"FROM groceries\n" +
		"ORDER BY id";
	private static final String INSERT_ROW = 
		"INSERT INTO groceries (item_name, price, quantity, on_sale)\n" +
		"VALUES (?, 0.0, 0, false);";
	private static final String DELETE_ROW = "DELETE FROM groceries\n" + "WHERE item_name = ?";
	private static final String UPDATE_ROW = 
		"UPDATE groceries\n" +
		"SET price = ?, quantity = ?, on_sale = ?\n" +
		"WHERE item_name = ?";
	;
	
	private Connection connection; // develop connection to database
	public ArrayList<String> columns; // names of each column 
	
	// initiailization block used to develop connection for this user
	{
		try
		{
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	// initializes groceries and the # of groceries for this user
	public GroceryList()
	{
		columns = new ArrayList<>();
		getColumns();
	}
	
	// obtains columns associated with the grocery table, returns true if successful
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
	
	// adds row with specified grocery into the database (with default values for the other data)
	public void addGrocery(String grocery)
	{
		try(PreparedStatement insert = connection.prepareStatement(INSERT_ROW);)
		{
			insert.setString(1, grocery);
			insert.execute();
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	// deletes row consisting of specified grocery item
	public void deleteGrocery(String grocery)
	{ 
		try(PreparedStatement delete = connection.prepareStatement(DELETE_ROW);)
		{
			delete.setString(1, grocery);
			delete.execute();
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	// updates an item in the grocery 
	public void updateGrocery(Grocery groceryData)
	{
		try(PreparedStatement update = connection.prepareStatement(UPDATE_ROW))
		{
			// set based on data type (only place requiring hardcoding)
			update.setFloat(1, groceryData.price());
			update.setInt(2, groceryData.quantity());
			update.setBoolean(3, groceryData.on_sale());
			update.setString(4, groceryData.item_name());
			update.execute();
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
}