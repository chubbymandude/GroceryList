package application.grocerylist;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import application.Grocery;

@CrossOrigin(origins = "http://localhost:4200") // allow Angular frontend to access this controller
@RestController
public class GroceryController 
{
	@Autowired
	private GroceryList groceries;
	
	@GetMapping("/list/display")
	public List<LinkedHashMap<String, Object>> getGroceries()
	{
		return groceries.getGroceries();
	}
	
	@DeleteMapping("/list/remove")
	public ResponseEntity<Grocery> removeItem(@RequestBody Grocery grocery)
	{
		groceries.removeGrocery(grocery);
		return ResponseEntity.ok(grocery);
	}
	
	@DeleteMapping("/list/purchase")
	public ResponseEntity<Grocery> purchaseItem(@RequestBody Grocery grocery)
	{
		groceries.purchaseGrocery(grocery);
		return ResponseEntity.ok(grocery);
	}
}