import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar';
import { EquipmentService } from '../../services/equipment';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable, switchMap, catchError, of, map } from 'rxjs';

declare var bootstrap: any;

@Component({
  selector: 'app-equipments',
  standalone: true,
  imports: [NavbarComponent, CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './equipments.html',
  styleUrl: './equipments.css'
})
export class EquipmentsComponent implements OnInit {
  private equipmentService = inject(EquipmentService);
  private fb = inject(FormBuilder);
  private toastr = inject(ToastrService);

  equipmentForm: FormGroup;
  private refresh$ = new BehaviorSubject<void>(undefined);
  
  equipments$: Observable<any[]>;
  stockSummary$: Observable<any[]>;
  showSummary: boolean = false;
  // verificar rol
  isAdmin: boolean = false; 

  filterBrand: string = 'TODOS';
  filterType: string = 'TODOS';
  filterStatus: string = 'TODOS';

  brands: string[] = [];
  types: string[] = [];

  constructor() {
    this.equipmentForm = this.fb.group({
      name: ['', Validators.required],
      brand: ['', Validators.required],
      type: ['Laptop', Validators.required]
    });

    this.equipments$ = this.refresh$.pipe(
      switchMap(() => {
        if (this.filterBrand !== 'TODOS' || this.filterType !== 'TODOS' || this.filterStatus !== 'TODOS') {
          return this.equipmentService.filter(this.filterBrand, this.filterType, this.filterStatus);
        }

        return this.equipmentService.getAll();
      }),
      catchError(() => {
        this.toastr.error('Error cargando equipos');
        return of([]);
      })
    );

    this.stockSummary$ = this.refresh$.pipe(
      switchMap(() => this.equipmentService.getStockSummary()),
      catchError(() => of([]))
    );
  }

  ngOnInit() {
    // verificar al iniciar
    const role = localStorage.getItem('role');
    this.isAdmin = role === 'ADMIN';
    this.loadFilterLists();
  }

  loadFilterLists() {
    this.equipmentService.getBrands().subscribe({
      next: (data) => this.brands = data,
      error: () => console.error('Error cargando marcas')
    });

    this.equipmentService.getTypes().subscribe({
      next: (data) => this.types = data,
      error: () => console.error('Error cargando tipos')
    });
  }

  applyFilters() {
    this.refresh$.next(); 
  }

  clearFilters() {
    this.filterBrand = 'TODOS';
    this.filterType = 'TODOS';
    this.filterStatus = 'TODOS';
    this.refresh$.next(); 
  }

  toggleView() {
    this.showSummary = !this.showSummary;
  }

  delete(id: number) {
    if(confirm('¿Estás seguro de eliminar este equipo?')) {
      this.equipmentService.delete(id).subscribe({
        next: () => {
          this.toastr.success('Equipo eliminado');
          this.refresh$.next(); 
          this.loadFilterLists();
        },
        error: () => this.toastr.error('No se pudo eliminar')
      });
    }
  }

  onSubmit() {
    if (this.equipmentForm.valid) {
      this.equipmentService.create(this.equipmentForm.value).subscribe({
        next: () => {
          this.toastr.success('Equipo creado correctamente');
          this.refresh$.next();
          this.loadFilterLists();

          this.equipmentForm.reset({ type: 'Laptop' });
          const modal = bootstrap.Modal.getInstance(document.getElementById('newEquipmentModal'));
          modal.hide();
        },
        error: () => this.toastr.error('Error al crear el equipo')
      });
    }
  }
}