import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from "../models/user";

export interface UserDto {
  id: number;
  name: string;
  email: string;
}

export interface UpdateUserRequest {
  name: string;
  email: string;
}

@Injectable({ providedIn: 'root' })
export class UsersService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/users';

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(this.baseUrl);
    }

    updateUser(id: number, payload: UpdateUserRequest): Observable<UserDto> {
        return this.http.put<UserDto>(`${this.baseUrl}/${id}`, payload);
    }

    addUser(payload: Omit<User,'id'>): Observable<User> {
        return this.http.post<User>(this.baseUrl, payload);
    }

    getUser(id: number): Observable<UserDto> {
        return this.http.get<UserDto>(`${this.baseUrl}/${id}`);
    }

    deleteUser(id: number): Observable<UserDto> {
        return this.http.delete<UserDto>(`${this.baseUrl}/${id}`);
    }
}
