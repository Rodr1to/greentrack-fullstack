import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/users'; 
  private authUrl = 'http://localhost:8080/auth/register'; // crear

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // crear usuario /auth/register
  create(user: any): Observable<any> {
    return this.http.post<any>(this.authUrl, user);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  updateRole(id: number, newRole: string): Observable<any> {
  return this.http.put(`${this.apiUrl}/${id}/role?newRole=${newRole}`, {});
  }
}
