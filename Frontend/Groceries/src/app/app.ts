import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './header/header';
import { Table } from './table/table';

@Component(
{
    selector: 'app-root',
    imports: [RouterOutlet, Header, Table],
    template: `
        <app-header/>
        <app-table/>
        <router-outlet/>
    `,
    styleUrl: './app.scss'
})
export class App 
{
    protected title = 'Groceries';
}
