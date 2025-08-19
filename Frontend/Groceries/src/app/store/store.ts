import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Grocery, GroceryAPI } from '../services/grocery';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription, timer } from 'rxjs';

@Component(
{
    selector: 'app-store',
    imports: [FormsModule],
    templateUrl: './store.html',
    styleUrl: './store.scss'
})
// this class represents the grocery "Store," which consists of all items the store offers
// the user can add any item to the list as long as it is in stock (quantity > 0)
export class Store implements OnInit
{
    items: Grocery[] = [];

    // used to display a message on success in user table manipulation
    showMessage = false;
    message = '';

    constructor(private service : GroceryAPI, private detector: ChangeDetectorRef) {};

    ngOnInit(): void
    {
        this.displayTable();;
    } 

    // updates variables to consist of database data from the store table
    // shows message indicating success in getting the store display for a few seconds
    // if there is an error sends an alert to the user
    displayTable() : void
    {
        this.service.getStore().subscribe(
        {
            next: (items) => 
            {
                this.items = items;
                this.message = 'Store items have been successfully loaded!';
                this.showMessage = true;
                timer(3000).subscribe(() => 
                { 
                    this.showMessage = false; 
                    this.detector.detectChanges();
                });
                this.detector.detectChanges();
            },
            error: (err: HttpErrorResponse) => { this.handleErrors(err.status, err.message) }         
        });
    }

    // adds to the user's grocery list one of the specified grocery item
    // if the quantity of the item is 0 it is out of stock and an alert is sent
    addToList(grocery : Grocery) : boolean
    {
        if(grocery.quantity == 0)
        {
            alert("Sorry, this item is out of stock.");
            return false;
        }
        this.service.addToList(grocery).subscribe(
        {
            next: () => 
            { 
                grocery.quantity -= 1; 
                this.message = grocery.item_name + ' has been successfully added to your list!';
                this.showMessage = true;
                timer(3000).subscribe(() => 
                { 
                    this.showMessage = false; 
                    this.detector.detectChanges();
                });
                this.detector.detectChanges();
            }
        });
        return true;
    }

    // used to handle any errors that may occur during the application, either client or server side
    handleErrors(status : number, message : string)
    {
        alert("An error has occurred with the application.\nMessage: " + message + "\nCode: " + status);
    }
}
