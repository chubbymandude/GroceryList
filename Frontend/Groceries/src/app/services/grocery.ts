import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

// consists of the JSON body for a grocery item and its attributes
export interface Grocery
{
    item_name: string,
    price: number,
    quantity: number, 
    on_sale: boolean,
}
@Injectable(
{
    providedIn: 'root'
})
// used to obtain data from Java backend in a global component
export class GroceryAPI 
{
    constructor(private http: HttpClient) {}

    // obtains all groceries and attributes in the form of a 2D array
    getGroceries() : Observable<Grocery[]>
    {
        return this.http.get<Grocery[]>('http://localhost:8080/get-all');
    }

    // sends name of grocery item to be added to database
    addGrocery(itemName : string) : Observable<Object>
    {
        return this.http.post('http://localhost:8080/add-grocery', itemName);
    }

    // sends name of grocery item to be deleted from database
    deleteGrocery(itemName : string) : Observable<Object>
    {
        return this.http.post('http://localhost:8080/delete-grocery', itemName);
    }

    // updates the entire row of this grocery item when the update button is clicked
    updateGrocery(row : Grocery) : Observable<Grocery>
    {
        return this.http.post<Grocery>('http://localhost:8080/update-grocery', row);
    }
}
