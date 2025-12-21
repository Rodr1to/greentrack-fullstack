import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';
import { ToastrService } from 'ngx-toastr';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const toastr = inject(ToastrService);

  // verificar rol
  const role = authService.getRole();

  if (role === 'ADMIN') {
    return true; 
  } else {
    toastr.error('Acceso denegado: Requiere permisos de Administrador');
    router.navigate(['/loans']); 
    return false;
  }
};