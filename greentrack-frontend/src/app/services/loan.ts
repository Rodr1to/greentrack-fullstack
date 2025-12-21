import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/loans'; // endpoint de prestamos

  // mostrar todo
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // filtros usuario y fecha
  filter(userId?: number, startDate?: string, endDate?: string): Observable<any[]> {
    let params = `?`;
    if (userId) params += `userId=${userId}&`;
    if (startDate) params += `startDate=${startDate}&`;
    if (endDate) params += `endDate=${endDate}&`;
    
    return this.http.get<any[]>(`${this.apiUrl}/filtrar${params}`);
  }

  // crear prestamo
  create(loan: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, loan);
  }

  // devolver equipo
  returnLoan(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/devolver`, {});
  }
}