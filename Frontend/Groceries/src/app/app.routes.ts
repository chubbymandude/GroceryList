import { Routes } from '@angular/router';
import { Store } from './store/store';
import { List } from './list/list';

export const routes: Routes = 
[
    { 
        path: '', 
        component: Store,
        pathMatch: 'full' 
    },
    {
        path: 'list',
        component: List,
    }
];
