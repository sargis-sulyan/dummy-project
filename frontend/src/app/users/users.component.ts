import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersService } from '../services/users.service';
import { User } from '../models/user';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-users',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './users.component.html'
})
export class UsersComponent {
    private readonly api = inject(UsersService);

    users: User[] = [];
    name = '';
    email = '';
    loading = false;
    error: string | null = null;

    ngOnInit() { this.refresh(); }

    refresh() {
        this.loading = true;
        this.error = null;
        this.api.getUsers().subscribe({
            next: (list) => { this.users = list; this.loading = false; },
            error: (e) => { this.error = 'Failed to load users'; this.loading = false; }
        });
    }

    add() {
        const name = this.name.trim();
        const email = this.email.trim();
        if (!name || !email) return;

        this.loading = true;
        this.api.addUser({ name, email } as any).subscribe({
            next: () => {
                this.name = '';
                this.email = '';
                this.refresh();
            },
            error: () => {
                this.error = 'Failed to add user';
                this.loading = false;
            }
        });
    }
}
