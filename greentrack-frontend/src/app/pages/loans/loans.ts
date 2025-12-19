import { Component, inject } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoanService } from '../../services/loan';
import { EquipmentService } from '../../services/equipment'; // <--- Necesitamos esto
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable, map, switchMap, catchError, of } from 'rxjs';

declare var bootstrap: any;

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [NavbarComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './loans.html',
  styleUrl: './loans.css'
})
export class LoansComponent {
  private loanService = inject(LoanService);
  private equipmentService = inject(EquipmentService); // inyectar equipos
  private fb = inject(FormBuilder);
  private toastr = inject(ToastrService);

  loanForm: FormGroup;
  
  // refrescar
  private refresh$ = new BehaviorSubject<void>(undefined);
  
  // flujo
  loans$: Observable<any[]>;
  availableEquipments$: Observable<any[]>; // dropdown de equipos

  constructor() {
    this.loanForm = this.fb.group({
      equipmentId: ['', Validators.required],
      userId: ['', Validators.required], // id usuario
      loanDate: [new Date().toISOString().split('T')[0], Validators.required] // fecha actual
    });

    // prestamos con refresh
    this.loans$ = this.refresh$.pipe(
      switchMap(() => this.loanService.getAll()),
      catchError(() => {
        this.toastr.error('Error cargando préstamos');
        return of([]);
      })
    );

    // equipos disponibles para select
    // filtrar solo los que tienen status AVAILABLE
    this.availableEquipments$ = this.refresh$.pipe(
        switchMap(() => this.equipmentService.getAll()),
        map(equipments => equipments.filter(e => e.status === 'AVAILABLE'))
    );
  }

  onSubmit() {
    if (this.loanForm.valid) {
      const formValues = this.loanForm.value;

      // transforma el payload
      const payload = {
        user: { id: parseInt(formValues.userId) }, 
        equipment: { id: parseInt(formValues.equipmentId) },
        loanDate: formValues.loanDate 
      };

      console.log('Enviando payload:', payload); // revisa consola

      this.loanService.create(payload).subscribe({
        next: () => {
          this.toastr.success('Préstamo registrado');
          this.refresh$.next();
          this.closeModal();
        },
        error: (err) => {
          // muestra error
          console.error('Error detallado:', err);
          const msg = err.error?.message || 'Verifica que el Usuario ID exista y sea correcto.';
          this.toastr.error(msg, 'Error al registrar');
        }
      });
    }
  }

  // devolver equipo
  onReturn(id: number) {
    if(confirm('¿Confirmar devolución del equipo?')) {
        this.loanService.returnLoan(id).subscribe({
            next: () => {
                this.toastr.info('Equipo devuelto correctamente');
                this.refresh$.next(); // actualizar la tabla
            },
            error: () => this.toastr.error('No se pudo procesar la devolución')
        });
    }
  }

  private closeModal() {
    this.loanForm.reset({ loanDate: new Date().toISOString().split('T')[0] });
    const modal = bootstrap.Modal.getInstance(document.getElementById('newLoanModal'));
    modal.hide();
  }
}