import { Routes } from '@angular/router';
import { UsersComponent } from './users/users.component';
import { UsersEditPageComponent } from './users/users-edit-page.component';

export const routes: Routes = [
    { path: '', redirectTo: 'users', pathMatch: 'full' },
    { path: 'users', component: UsersComponent },
    { path: 'users/:id/edit', component: UsersEditPageComponent }
];
