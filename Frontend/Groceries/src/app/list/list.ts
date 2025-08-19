import { ChangeDetectorRef, Component } from '@angular/core';
import { Grocery, GroceryAPI } from '../services/grocery';
import { timer } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component(
{
    selector: 'app-list',
    imports: [],
    templateUrl: './list.html',
    styleUrl: './list.scss'
})
// this class represents the user's grocery list, which consists of items the user added
// the user can decide whether to remove or purchase items from the list
export class List 
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
        this.service.getList().subscribe(
        {
            next: (items) => 
            {
                this.items = items;
                this.message = 'Your grocery list has been successfully loaded!';
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

    // removes the grocery from the user's list, puts it back into the store
    removeFromList(grocery : Grocery) : void
    {
        this.service.removeGrocery(grocery).subscribe(
        {
            next: () => 
            {
                this.message = grocery.item_name + ' has been removed off your list!';
                this.showMessage = true;
                timer(3000).subscribe(() => 
                { 
                    this.showMessage = false; 
                    this.detector.detectChanges();
                });
                this.detector.detectChanges();
            }
        });
        const itemIndex = this.items.findIndex(item => item.item_name === grocery.item_name)
        this.items.splice(itemIndex, 1);
    }

    // removes the grocery from the user's list, does not put back into the store
    purchaseItem(grocery : Grocery) : void
    {
        this.service.purchaseGrocery(grocery).subscribe(
        {
            next: () => 
            {
                this.message = 'You have now purchased ' + grocery.item_name + '!';
                this.showMessage = true;
                timer(3000).subscribe(() => 
                { 
                    this.showMessage = false; 
                    this.detector.detectChanges();
                });
                this.detector.detectChanges();
            }
        });
        const itemIndex = this.items.findIndex(item => item.item_name === grocery.item_name)
        this.items.splice(itemIndex, 1);
    }

    handleErrors(status : number, message : string)
    {
        alert("An error has occurred with the application.\nMessage: " + message + "\nCode: " + status);
    }
}
