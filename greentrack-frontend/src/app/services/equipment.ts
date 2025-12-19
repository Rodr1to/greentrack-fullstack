import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/equipos'; // backend 

  // obtener todos los equipos
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  getStats(brand?: string): Observable<any> {
  let url = 'http://localhost:8080/dashboard/stats';
  if (brand && brand !== 'TODOS') {
    url += `?brand=${brand}`;
  }
  return this.http.get<any>(url, { headers: this.getAuthHeaders() });
  }

  // crear un equipo nuevo
  create(equipment: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, equipment, { headers: this.getAuthHeaders() });
  }

  // eliminar equipo
  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  // enviar el token en cada petición
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getBrands(): Observable<string[]> {
  return this.http.get<string[]>(`${this.apiUrl}/brands`, { headers: this.getAuthHeaders() });
  }

}
