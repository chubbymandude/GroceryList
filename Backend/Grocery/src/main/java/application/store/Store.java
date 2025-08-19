package application.store;

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

// handles logic revolving around the store route of the Grocery's website
@Service
public class Store 
{
	// database information
	private static final String URL = System.getenv("DB_URL");
	private static final String USERNAME = System.getenv("DB_USERNAME");
	private static final String PASSWORD = System.getenv("DB_PASSWORD");
	
	// queries
	private static final String SELECT_ALL =
		"SELECT item_name, price, quantity, on_sale\n" + 
		"FROM store\n" +
		"ORDER BY id";
	private static final String ITEM_EXISTS = 
		"SELECT COUNT(*) as count\n" +
		"FROM groceries\n" + 
		"WHERE item_name = ?";
	private static final String UPDATE_ROW_IN_STORE = 
		"UPDATE store\n" + 
		"SET quantity = quantity - 1\n" + 
		"WHERE item_name = ?";
	private static final String INSERT_ROW_IN_LIST = 
		"INSERT INTO groceries (item_name, price, quantity, on_sale)\n" +
		"VALUES (?, ?, 1, ?);";
	private static final String UPDATE_ROW_IN_LIST = 
		"UPDATE groceries\n" +
		"SET quantity = quantity + 1\n" +
		"WHERE item_name = ?";
	
	private Connection connection; 
	public ArrayList<String> columns; 
	
	{
		try { connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); }
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	public Store()
	{
		columns = new ArrayList<>();
		getColumns();
	}
	
	// obtains columns associated with the store table
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
	
	// used to get all store items and their information from database
	// returned as a List of Maps (which are rows) to convert to JSON later
	public List<LinkedHashMap<String, Object>> getStoreItems()
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
	
	// updates the quantity of grocery in the list
	// if the grocery doesn't exist in the list add it to the list with a quantity of 1
	// if the item does exist in the list simply re-increment it
	public void addItem(Grocery grocery)
	{
		try
		{
			// update the quantity from the store
			PreparedStatement updateStore = connection.prepareStatement(UPDATE_ROW_IN_STORE);
			updateStore.setString(1, grocery.item_name());
			updateStore.execute();
			// update quantity in the list accordingly depending on if it exists or not 
			PreparedStatement exists = connection.prepareStatement(ITEM_EXISTS);
			exists.setString(1, grocery.item_name());
			ResultSet resultSet = exists.executeQuery();
			resultSet.next();
			if(resultSet.getInt("count") != 0)
			{
				PreparedStatement updateRow = connection.prepareStatement(UPDATE_ROW_IN_LIST);
				updateRow.setString(1, grocery.item_name());			
				updateRow.execute();
			}
			else
			{
				PreparedStatement insertRow = connection.prepareStatement(INSERT_ROW_IN_LIST);
				insertRow.setString(1, grocery.item_name());
				insertRow.setFloat(2, grocery.price());
				insertRow.setBoolean(3, grocery.on_sale());
				insertRow.execute();
			}
		}
		catch(SQLException e) { e.printStackTrace(); }
	}
}