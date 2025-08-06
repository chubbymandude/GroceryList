package groceries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import org.springframework.stereotype.Service;

// class connects to database via JDBC to obtain grocery list and associated items
@Service
public class GroceryList 
{
	// database information
	private static final String URL = System.getenv("DB_URL");
	private static final String USERNAME = System.getenv("DB_USERNAME");
	private static final String PASSWORD = System.getenv("DB_PASSWORD");
	
	// queries
	private static final String SELECT_ALL = "SELECT * FROM groceries;";
	private static final String ROW_COUNT = "SELECT COUNT(1) FROM groceries;";
	private static final String INSERT_ROW = 
		"INSERT INTO groceries (item_name, price, quantity, on_sale)\n" +
		"VALUES (?, 0.0, 0, false);";
	private static final String DELETE_ROW = "DELETE FROM groceries\n" + "WHERE item_name = ?;";
	private static final String UPDATE_ROW = 
		"UPDATE groceries\n" +
		"SET price = ?, quantity = ?, on_sale = ?\n" +
		"WHERE item_name = ?";
	;
	
	private Connection connection; // develop connection to database
	public ArrayList<String> columns; // names of each column 
	private int numGroceries; // use a query to determine this
	
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
		numGroceries = getNumGroceries();
		columns = new ArrayList<>();
		getColumns();
	}
	
	// obtains # of rows in the groceries table AKA the # of groceries there are
	public int getNumGroceries() 
	{
		try
		{
			PreparedStatement getCount = connection.prepareStatement(ROW_COUNT);
			ResultSet resultSet = getCount.executeQuery();
			
			if(resultSet.next())
			{
				return resultSet.getInt("count");
			}
		}
		catch(SQLException e) { e.printStackTrace(); }
		// if an error occurred
		return 0;
	}
	
	// obtains columns associated with the grocery table, returns true if successful
	public boolean getColumns()
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
			
			return true;
		}
		catch(SQLException e) { e.printStackTrace(); }
		// if an error occurred while preparing statement
		return false;
	}
	
	// used to get all groceries and their information from database	
	// returns false if the database was not able to be contacted
	public String[][] getGroceries()
	{
		try
		{
			PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL);
			ResultSet resultSet = selectAll.executeQuery();
			String[][] groceries = new String[numGroceries][columns.size()];
			
			// loop through each item and obtain results accordingly
			int row = 0;
			while(resultSet.next())
			{
				for(int col = 0; col < columns.size(); col++)
				{
					groceries[row][col] = resultSet.getString(col + 1);
					// check boolean and convert it
					if(groceries[row][col].equals("t"))
					{
						groceries[row][col] = "TRUE";
					}
					if(groceries[row][col].equals("f"))
					{
						groceries[row][col] = "FALSE";
					}
				}
				row++;
			}
			
			// success
			return groceries;
		}
		catch(SQLException e) { e.printStackTrace(); } 
		// if an error occurred while preparing statement
		return null;
	}
	
	// adds row with specified grocery into the database (with default values for the other data)
	public void addGrocery(String grocery)
	{
		try
		{
			PreparedStatement insert = connection.prepareStatement(INSERT_ROW);
			insert.setString(1, grocery);
			insert.execute();
			numGroceries++;
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	// deletes row consisting of specified grocery item
	public void deleteGrocery(String grocery)
	{
		try
		{
			PreparedStatement delete = connection.prepareStatement(DELETE_ROW);
			delete.setString(1, grocery);
			delete.execute();
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	// updates an item in the grocery 
	public void updateGrocery(String[] groceryData)
	{
		try
		{
			PreparedStatement update = connection.prepareStatement(UPDATE_ROW);
			// set based on data type (only place requiring hardcoding)
			update.setFloat(1, Float.valueOf(groceryData[1]));
			update.setInt(2, Integer.valueOf(groceryData[2]));
			update.setBoolean(3,  Boolean.valueOf(groceryData[3]));
			update.setString(4, groceryData[0]);
			update.execute();
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
}