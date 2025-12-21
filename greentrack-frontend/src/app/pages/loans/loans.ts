import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms'; 
import { LoanService } from '../../services/loan';
import { EquipmentService } from '../../services/equipment';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable, map, switchMap, catchError, of } from 'rxjs';

declare var bootstrap: any;

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [NavbarComponent, CommonModule, ReactiveFormsModule, FormsModule], 
  templateUrl: './loans.html',
  styleUrl: './loans.css'
})
export class LoansComponent implements OnInit {
  private loanService = inject(LoanService);
  private equipmentService = inject(EquipmentService);
  private fb = inject(FormBuilder);
  private toastr = inject(ToastrService);

  loanForm: FormGroup;
  refresh$ = new BehaviorSubject<void>(undefined);
  
  loans$: Observable<any[]>;
  availableEquipments$: Observable<any[]>;
  
  isAdmin: boolean = false;

  // variables para los inputs de filtro del HTML
  filterUserId: number | null = null;
  filterStartDate: string = '';
  filterEndDate: string = '';

  constructor() {
    this.loanForm = this.fb.group({
      equipmentId: ['', Validators.required],
      userId: [''], 
      loanDate: [new Date().toISOString().split('T')[0], Validators.required]
    });

    // si hay filtros, usa filter() / getAll()
    this.loans$ = this.refresh$.pipe(
      switchMap(() => {
        if (this.filterUserId || (this.filterStartDate && this.filterEndDate)) {
          return this.loanService.filter(this.filterUserId || undefined, this.filterStartDate, this.filterEndDate);
        }
        return this.loanService.getAll();
      }),
      catchError(() => of([]))
    );

    this.availableEquipments$ = this.refresh$.pipe(
        switchMap(() => this.equipmentService.getAll()),
        map(equipments => equipments.filter(e => e.status === 'DISPONIBLE'))
    );
  }

  ngOnInit() {
    const role = localStorage.getItem('role');
    this.isAdmin = role === 'ADMIN';

    // userId del formulario sea obligatorio para ADMIN
    if (this.isAdmin) {
      this.loanForm.get('userId')?.setValidators(Validators.required);
    }
  }

  // filtrar
  applyFilters() {
    this.refresh$.next();
  }

  // limpiar filtros
  clearFilters() {
    this.filterUserId = null;
    this.filterStartDate = '';
    this.filterEndDate = '';
    this.refresh$.next();
  }

  onSubmit() {
    if (this.loanForm.valid) {
      let finalUserId: number;

      if (this.isAdmin) {
        // ID input de ADMIN
        finalUserId = parseInt(this.loanForm.value.userId);
      } else {
        // ID de USER
        const storedId = localStorage.getItem('userId');
        if (!storedId) return;
        finalUserId = parseInt(storedId);
      }

      const payload = {
        userId: finalUserId,
        equipmentId: parseInt(this.loanForm.value.equipmentId),
        loanDate: this.loanForm.value.loanDate
      };

      this.loanService.create(payload).subscribe({
        next: () => {
          this.toastr.success('Préstamo registrado');
          this.refresh$.next();
          this.equipmentService.notifyUpdate();
          this.closeModal();
        },
        error: (err) => this.toastr.error(err.error?.message || 'Error al registrar')
      });
    } else {
        this.loanForm.markAllAsTouched();
    }
  }

  onReturn(id: number) {
    if(confirm('¿Confirmar devolución del equipo?')) {
        this.loanService.returnLoan(id).subscribe({
            next: () => {
                this.toastr.info('Devolución exitosa');
                this.refresh$.next(); 
                this.equipmentService.notifyUpdate();
            },
            error: () => this.toastr.error('Error en devolución')
        });
    }
  }

  private closeModal() {
    this.loanForm.reset({ loanDate: new Date().toISOString().split('T')[0] });
    const modal = bootstrap.Modal.getInstance(document.getElementById('newLoanModal'));
    modal.hide();
  }
}