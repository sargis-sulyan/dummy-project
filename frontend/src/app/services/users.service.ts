import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({ providedIn: 'root' })
export class UsersService {
    private readonly http = inject(HttpClient);
    private readonly base = '/api/users';

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(this.base);
    }

    addUser(payload: Omit<User,'id'>): Observable<User> {
        return this.http.post<User>(this.base, payload);
    }
}
