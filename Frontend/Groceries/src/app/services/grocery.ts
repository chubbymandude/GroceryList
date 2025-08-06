import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable(
{
    providedIn: 'root'
})
// used to obtain data from Java backend in a global component
export class Grocery 
{
    // support text-only post request
    headers = new HttpHeaders({ 'Content-Type': 'text/plain' });
    
    constructor(private http: HttpClient) {}
    
    // obtains header for the table from /get-all
    getHeader() : Observable<string[]>
    {
        return this.http.get<string[]>('http://localhost:8080/groceries-header');
    }

    // obtains all groceries and attributes in the form of a 2D array
    getGroceries() : Observable<string[][]>
    {
        return this.http.get<string[][]>('http://localhost:8080/get-all');
    }

    // obtains # of rows in database for tracking
    getNumGroceries() : Observable<number>
    {
        return this.http.get<number>('http://localhost:8080/get-num-rows');
    }

    // sends name of grocery item to be added to database
    addGrocery(itemName : string) : Observable<string>
    {
        return this.http.post('http://localhost:8080/add-grocery', itemName, 
        {
            headers: this.headers,
            responseType: 'text',
        });
    }

    // sends name of grocery item to be deleted from database
    deleteGrocery(itemName : string) : Observable<string>
    {
        return this.http.post('http://localhost:8080/delete-grocery', itemName, 
        {
            headers: this.headers,
            responseType: 'text',
        });
    }

    // updates the entire row of this grocery item when the update button is clicked
    updateGrocery(row : string[]) : Observable<string>
    {
        return this.http.post('http://localhost:8080/update-grocery', row, 
        {
            headers: this.headers, 
            responseType: 'text',
        });
    }
}
