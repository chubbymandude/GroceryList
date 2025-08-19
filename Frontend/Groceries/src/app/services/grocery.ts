import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { setThrowInvalidWriteToSignalError } from '@angular/core/primitives/signals';
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
    constructor(private http: HttpClient) {};

    getStore() : Observable<Grocery[]>
    {
        return this.http.get<Grocery[]>('http://localhost:8080/store/display');
    }

    addToList(grocery: Grocery) : Observable<Grocery>
    {
        return this.http.put<Grocery>('http://localhost:8080/store/add', grocery);
    }

    getList() : Observable<Grocery[]>
    {
        return this.http.get<Grocery[]>('http://localhost:8080/list/display');
    }

    removeGrocery(grocery: Grocery) : Observable<Grocery>
    {
        return this.http.delete<Grocery>('http://localhost:8080/list/remove', { body: grocery })
    }

    purchaseGrocery(grocery : Grocery) : Observable<Grocery>
    {
        return this.http.delete<Grocery>('http://localhost:8080/list/purchase', { body: grocery });
    }
}
