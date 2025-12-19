import { Component, inject } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar';
import { EquipmentService } from '../../services/equipment';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable, switchMap, catchError, of, tap } from 'rxjs';

declare var bootstrap: any;

@Component({
  selector: 'app-equipments',
  standalone: true,
  imports: [NavbarComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './equipments.html',
  styleUrl: './equipments.css'
})
export class EquipmentsComponent {
  private equipmentService = inject(EquipmentService);
  private fb = inject(FormBuilder);
  private toastr = inject(ToastrService);

  equipmentForm: FormGroup;
  
  // refrescar tabla
  private refresh$ = new BehaviorSubject<void>(undefined);
  
  // flujo de datos
  equipments$: Observable<any[]>;

  constructor() {
    this.equipmentForm = this.fb.group({
      name: ['', Validators.required],
      brand: ['', Validators.required],
      type: ['Laptop', Validators.required]
    });

    // toma datos del pipeline refrescado
    this.equipments$ = this.refresh$.pipe(
      switchMap(() => this.equipmentService.getAll()),
      catchError(err => {
        this.toastr.error('Error cargando equipos');
        return of([]); // vacio si hay error para no romper la tabla
      })
    );
  }

  // guardar
  onSubmit() {
    if (this.equipmentForm.valid) {
      this.equipmentService.create(this.equipmentForm.value).subscribe({
        next: () => {
          this.toastr.success('Equipo creado correctamente');
          
          // refresh
          this.refresh$.next(); 
          
          this.equipmentForm.reset({ type: 'Laptop' });
          const modal = bootstrap.Modal.getInstance(document.getElementById('newEquipmentModal'));
          modal.hide();
        },
        error: () => this.toastr.error('Error al crear el equipo')
      });
    }
  }
}
