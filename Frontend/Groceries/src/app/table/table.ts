import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Grocery, GroceryAPI } from '../services/grocery';
import { FormsModule } from '@angular/forms';

// representation of a row of the groceries tbale
interface TableRow
{
    rowIndex : number, 
    rowContents : string[],
}

@Component(
{
    selector: 'app-table',
    imports: [CommonModule, FormsModule],
    templateUrl: './table.html',
    styleUrl: './table.scss'
})
// code needed to form table from database contents
export class Table implements OnInit
{
    // consists of row data for each grocery item
    groceries: Grocery[] = [];
    // variable for input
    input: string = '';

    constructor(private service : GroceryAPI){};

    // initializes table information such as header and grocery data
    ngOnInit(): void
    {
        this.displayTable();
    }

    // also performs updates on the table whenever "add", "delete" or "update" buttons are clicked
    displayTable() : void
    {
        this.service.getGroceries().subscribe(
        {
            next: (groceries) => (this.groceries = groceries),
        });
    }

    // checks if a grocery item is in the groceries array
    // adds the grocery to the database, updates display
    addGrocery() : boolean
    {
        // do not allow empty input
        if(this.input.trim() === "")
        {
            alert("Please insert a grocery item.");
            this.input = '';
            return false;
        }
        for(let row = 0; row < this.groceries.length; row++)
        {
            if(this.groceries[row].item_name.toLowerCase().trim() == this.input.toLowerCase().trim())
            {
                alert("This item is already in your grocery list!");
                this.input = '';
                return false;
            }
        }
        this.service.addGrocery(this.input).subscribe();
        this.groceries.push(
        {
            item_name: this.input, 
            price: 0.0, 
            quantity: 0, 
            on_sale: false,
        });
        this.input = ''; 
        return true;
    }

    // deletes from the grocery list the specified item
    // for display purposes removes from the grocery list the row with the specified item name
    deleteGrocery(itemName : string) : void
    {
        this.service.deleteGrocery(itemName).subscribe();
        const itemIndex = this.groceries.findIndex(grocery => grocery.item_name === itemName)
        this.groceries.splice(itemIndex, 1);
    }

    // updates the given row of the database when the user updates the price or quantity attributes
    updateValue(row : Grocery)
    {
        this.service.updateGrocery(row).subscribe();
    }

    // updates the given row of the database when the user updates the "on sale" attribute
    updateOnSale(row : Grocery, updatedValue : boolean) : void
    {
        row.on_sale = updatedValue;
        this.service.updateGrocery(row).subscribe();
    }

    // helper method, prevent invalid characters on key down
    // used for preventing invalid input in columns price & quantity
    onKeyDown(event: KeyboardEvent) 
    {
        if(['e', 'E', '+', '-'].includes(event.key)) 
        {
            event.preventDefault();
        }
    }

    // helper method, prevent invalid characters on paste
    // used for preventing invalid input in columns price & quantity
    onPaste(event: ClipboardEvent) 
    {
        if(/[eE+\-]/.test(event.clipboardData?.getData('text') || '' ))
        {
            event.preventDefault();
        }
    }

    // helper method, prevent invalid characters on input
    // used for preventing invalid input in columns price & quantity
    onInput(event: Event) 
    {
        const input = event.target as HTMLInputElement;
        input.value = input.value.replace(/[eE+\-]/g, '');
    }
}
