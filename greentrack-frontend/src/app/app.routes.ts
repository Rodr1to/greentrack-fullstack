import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';          
import { DashboardComponent } from './pages/dashboard/dashboard'; 
import { EquipmentsComponent } from './pages/equipments/equipments'; 
import { LoansComponent } from './pages/loans/loans';          
import { authGuard } from './guards/auth-guard';     
import { UsersComponent } from './pages/users/users';          
import { adminGuard } from './guards/admin-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },

  
  // Rutas protegidas
  { 
    path: 'dashboard', 
    component: DashboardComponent, 
    canActivate: [authGuard] 
  },
  { 
    path: 'equipments', 
    component: EquipmentsComponent, 
    canActivate: [authGuard] 
  },
  { 
    path: 'loans', 
    component: LoansComponent, 
    canActivate: [authGuard] 
  },
  
  { 
    path: 'users', 
    component: UsersComponent, 
    canActivate: [authGuard, adminGuard] 
  },
];