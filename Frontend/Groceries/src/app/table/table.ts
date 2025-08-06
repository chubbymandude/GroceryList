import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Grocery } from '../services/grocery';
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
    // variables representing table information
    header: string[] = [];
    groceries: string[][] = [];
    table: TableRow[] = [];

    // variable for input
    input: string = '';

    // variable for tracking # of groceries for proper row display
    rowCount = 0;

    constructor(private service : Grocery){};

    // initializes table information such as header and grocery data
    ngOnInit(): void
    {
        this.displayTable();
    }

    // also performs updates on the table whenever "add", "delete" or "update" buttons are clicked
    displayTable() : void
    {
        this.service.getHeader().subscribe(
        {
            next : header => { this.header = header },
        });
        this.service.getGroceries().subscribe(
        {
            next : groceries =>
            { 
                this.groceries = groceries; 
                this.service.getNumGroceries().subscribe(
                {
                    next: (numGroceries : number) => 
                    { 
                        this.rowCount = numGroceries;
                        for(let row = 0; row < this.rowCount; row++)
                        {
                            this.table[row] = {rowIndex : row, rowContents : this.groceries[row]};
                        }
                    },
                });
            },
        });
    }

    // checks if a grocery item is in the groceries array
    // then adds the grocery to the database
    addGrocery() : boolean
    {
        // do not allow empty input
        if(this.input.trim() === "")
        {
            alert("Please insert a grocery item.");
            this.input = '';
            return false;
        }
        // loop through and check that the grocery is not already in the list
        for(let row = 0; row < this.rowCount; row++)
        {
            if(this.groceries[row][0].toLowerCase() === this.input.toLowerCase().trim())
            {
                alert(this.input.trim() + " is already on your grocery list");
                this.input = '';
                return false;
            }   
        }
        this.service.addGrocery(this.input).subscribe(
        {
            next: () => { this.displayTable(); },
        });
        this.input = ''; 
        location.reload();
        return true;
    }

    // deletes from the grocery list the specified item
    deleteGrocery(itemName : string) : void
    {
        this.service.deleteGrocery(itemName).subscribe(
        {
            next: () => { this.displayTable(); },
        });
        location.reload();
    }

    // updates entire row of this grocery item based on user input
    // returns false if user input was invalid
    updateGrocery(row : string[]) : boolean
    {
        // validate update
        if(parseFloat(row[1]) < 0.0 || parseFloat(row[1]) > 999.9) // prices
        {
            alert("Prices have to be non-negative and below $1,000.");
            return false;
        }
        else if(parseInt(row[2]) < 0) // quantities
        {
            alert("Quantity of items must be 0 or above.");
            return false;
        }
        else if(row[3].toLowerCase().trim() !== "false" && row[3].toLowerCase().trim() !== "true")
        { // on_sale
            alert("Must insert either TRUE or FALSE for sales.");
            return false;
        }
        // perform update
        this.service.updateGrocery(row).subscribe(
        {
            next: () => { this.displayTable() },
        });
        location.reload();
        return true;
    }
}
