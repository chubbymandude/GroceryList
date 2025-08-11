package groceries;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import groceries.GroceryList.Grocery;

// used to create GET and POST requests related to groceries
@CrossOrigin(origins = "http://localhost:4200") // allow Angular frontend to access this controller
@RestController
public class GroceryController 
{
	@Autowired
	private GroceryList groceries;
	
	// sends the JSON string representing database content 
	@GetMapping("/get-all")
	public List<LinkedHashMap<String, Object>> getGroceries()
	{
		return groceries.getGroceries();
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
	public ResponseEntity<Grocery> updateGrocery(@RequestBody Grocery grocery)
	{
		groceries.updateGrocery(grocery);
		return ResponseEntity.ok(grocery);
	}
}
