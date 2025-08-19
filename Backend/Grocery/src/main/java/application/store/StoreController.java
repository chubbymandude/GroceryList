package application.store;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import application.Grocery;

@CrossOrigin(origins = "http://localhost:4200") // allow Angular frontend to access this controller
@RestController
public class StoreController 
{
	@Autowired
	private Store store;
	
	@GetMapping("/store/display")
	public List<LinkedHashMap<String, Object>> displayItems()
	{
		return store.getStoreItems();
	}
	
	@PutMapping("/store/add")
	public ResponseEntity<Grocery> addItem(@RequestBody Grocery grocery)
	{
		store.addItem(grocery);
		return ResponseEntity.ok(grocery);
	}
}
