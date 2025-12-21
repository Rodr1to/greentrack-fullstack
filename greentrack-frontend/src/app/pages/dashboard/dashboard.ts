import { Component, inject } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar';
import { RouterLink } from '@angular/router';
import { EquipmentService } from '../../services/equipment';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, Observable, combineLatest, switchMap, map, catchError, of } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NavbarComponent, RouterLink, CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent {
  private equipmentService = inject(EquipmentService);

  brandFilter$ = new BehaviorSubject<string>('TODOS');
  typeFilter$ = new BehaviorSubject<string>('TODOS');

  stats$: Observable<any> = combineLatest([
    this.equipmentService.refresh$, 
    this.brandFilter$,              // marca
    this.typeFilter$                // tipo
  ]).pipe(
    switchMap(([_, brand, type]) => 
      this.equipmentService.getStats(brand, type).pipe(
        catchError(err => {
          console.error('Error en Dashboard:', err);
          return of({ totalEquipos: 0, disponibles: 0, prestados: 0 }); // Retorna ceros en vez de morir
        })
      )
    )
  );

  brands$ = this.equipmentService.getBrands().pipe(
    map(b => ['TODOS', ...b]),
    catchError(() => of(['TODOS']))
  );
  
  types$ = this.equipmentService.getTypes().pipe(
    map(t => ['TODOS', ...t]),
    catchError(() => of(['TODOS']))
  );

  get selectedBrand(): string { return this.brandFilter$.value; }
  set selectedBrand(val: string) { 
    if (val !== 'TODOS') this.typeFilter$.next('TODOS'); 
    this.brandFilter$.next(val); 
  }

  get selectedType(): string { return this.typeFilter$.value; }
  set selectedType(val: string) { 
    if (val !== 'TODOS') this.brandFilter$.next('TODOS');
    this.typeFilter$.next(val); 
  }

  isAdmin: boolean = false;

  ngOnInit() { // Si no tienes ngOnInit, agrégalo
      const role = localStorage.getItem('role');
      this.isAdmin = role === 'ADMIN';
  }
}