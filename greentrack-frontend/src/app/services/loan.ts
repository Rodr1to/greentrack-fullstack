import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/loans'; // endpoint de prestamos

  // headers con token
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // mostrar todo
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  // crear prestamo
  create(loan: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, loan, { headers: this.getAuthHeaders() });
  }

  // devolver equipo
  returnLoan(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/devolver`, {}, { headers: this.getAuthHeaders() });
  }
}