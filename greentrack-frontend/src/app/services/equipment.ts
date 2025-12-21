import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/equipos';
  private dashboardUrl = 'http://localhost:8080/dashboard';

  private refreshSubject = new BehaviorSubject<void>(undefined);
  refresh$ = this.refreshSubject.asObservable();

  notifyUpdate() {
    this.refreshSubject.next();
  }

  // crear equipo
  create(equipment: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, equipment)
      .pipe(tap(() => this.notifyUpdate())); 
  }

  // eliminar equipo
  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`)
      .pipe(tap(() => this.notifyUpdate())); 
  }

  filter(brand?: string, type?: string, status?: string): Observable<any[]> {
    let params = '?';
    if (brand && brand !== 'TODOS') params += `brand=${brand}&`;
    if (type && type !== 'TODOS') params += `type=${type}&`;
    if (status && status !== 'TODOS') params += `status=${status}&`;
    
    return this.http.get<any[]>(`${this.apiUrl}/filtrar${params}`);
  }

  // obtener todo
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // stock
  getStockSummary(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/stock-summary`);
  }

  // dashboard
  getStats(brand?: string, type?: string): Observable<any> {
    let url = `${this.dashboardUrl}/stats?`;
    if (brand && brand !== 'TODOS') {
      url += `brand=${brand}`;
    } else if (type && type !== 'TODOS') {
      url += `type=${type}`;
    }
    return this.http.get<any>(url);
  }

  // marcas
  getBrands(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/brands`);
  }

  // tipos
  getTypes(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/types`);
  }
}