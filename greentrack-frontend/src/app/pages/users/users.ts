import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar';
import { UserService } from '../../services/user';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Observable, BehaviorSubject, switchMap } from 'rxjs';

declare var bootstrap: any;

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [NavbarComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './users.html'
})
export class UsersComponent implements OnInit {
  private userService = inject(UserService);
  private fb = inject(FormBuilder);
  private toastr = inject(ToastrService);

  private refresh$ = new BehaviorSubject<void>(undefined);

  users$: Observable<any[]>;
  userForm: FormGroup;

  constructor() {
    this.userForm = this.fb.group({
      fullName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      role: ['USER', Validators.required]
    });

    this.users$ = this.refresh$.pipe(
      switchMap(() => this.userService.getAll())
    );
  }

  ngOnInit() {
  }

  onSubmit() {
    if (this.userForm.valid) {
      this.userService.create(this.userForm.value).subscribe({
        next: () => {
          this.toastr.success('Usuario registrado');
          this.refresh$.next(); 
          this.userForm.reset({ role: 'USER' });
          const modal = bootstrap.Modal.getInstance(document.getElementById('newUserModal'));
          modal.hide();
        },
        error: (err) => this.toastr.error('Error al crear usuario')
      });
    }
  }

  deleteUser(user: any) {
    const currentUsername = localStorage.getItem('username'); 
    
    if (user.username === currentUsername) {
      this.toastr.error('No puedes eliminar tu propia cuenta mientras estás logueado.');
      return;
    }

    if (confirm(`¿Estás seguro de eliminar al usuario ${user.fullName}?`)) {
      this.userService.delete(user.id).subscribe({
        next: () => {
          this.toastr.success('Usuario eliminado');
          this.refresh$.next(); 
        },
        error: (err) => this.toastr.error('No se pudo eliminar el usuario')
      });
    }
  }

currentUsername: string | null = localStorage.getItem('username');

onRoleChange(user: any, event: any) {
  const newRole = event.target.value;
  const originalRole = user.role; 

  if (confirm(`¿Seguro que quieres cambiar el rol de ${user.fullName} de ${originalRole} a ${newRole}?`)) {
    this.userService.updateRole(user.id, newRole).subscribe({
      next: () => {
        this.toastr.success(`Rol actualizado a ${newRole}`);
        user.role = newRole; 
      },
      error: () => {
        this.toastr.error('Error al cambiar rol');
        event.target.value = originalRole; 
      }
    });
  } else {
    event.target.value = originalRole; 
  }
}
}