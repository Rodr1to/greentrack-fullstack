import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = 'http://localhost:8080/auth'; // backend

  // iniciar sesion
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response.token) {
          // Guardamos el token en la memoria del navegador
          localStorage.setItem('token', response.token);
        }
      })
    );
  }

  // cerrar sesion
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  // obtener token
  getToken() {
    return localStorage.getItem('token');
  }

  // confirmar login
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // confirmar role/user desde token
  getUserDetails() {
    const token = this.getToken();
    if (!token) return null;
    try {
      return jwtDecode(token);
    } catch(e) {
      return null;
    }
  }
}