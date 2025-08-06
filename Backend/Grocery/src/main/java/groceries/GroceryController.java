package groceries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// used to create GET and POST requests related to groceries
@CrossOrigin(origins = "http://localhost:4200") // allow Angular frontend to access this controller
@RestController
public class GroceryController 
{
	@Autowired
	private GroceryList groceries;
	
	// obtains header of database table which exists in the GroceryApplication
	@GetMapping("/groceries-header")
	public String[] getHeader()
	{
		return groceries.columns.toArray(new String[0]);
	}
	
	// sends the 2D array representing database content 
	@GetMapping("/get-all")
	public String[][] getGroceries()
	{
		return groceries.getGroceries();
	}
	
	// sends the # of groceries to ensure proper row counts
	@GetMapping("/get-num-rows")
	public int getNumGroceries()
	{
		return groceries.getNumGroceries();
	}
	
	// adds the item passed in from the frontend to the database with default attributes
	@PostMapping("/add-grocery")
	public String addGrocery(@RequestBody String itemName)
	{
		groceries.addGrocery(itemName);
		return itemName + " was successfully added!";
	}
	
	// removes the specified item from the database 
	@PostMapping("/delete-grocery")
	public String deleteGrocery(@RequestBody String itemName)
	{
		groceries.deleteGrocery(itemName);
		return itemName + " was successfully deleted from the list!";
	}
	
	// updates the specified row in the database
	@PostMapping("/update-grocery")
	public String updateGrocery(@RequestBody String row)
	{
		// convert String into an array for parsing
		String[] attributes = row.substring(1, row.length() - 1).split(",");
		// parse through attributes, remove quotations at start and end of each element
		for(int col = 0; col < attributes.length; col++)
		{
			attributes[col] = attributes[col].substring(1, attributes[col].length() - 1);
		}
		// update database
		groceries.updateGrocery(attributes);
		return attributes[0] + " has been successfully updated!";
	}
}
