import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';          
import { RegisterComponent } from './pages/register/register'; 
import { DashboardComponent } from './pages/dashboard/dashboard'; 
import { EquipmentsComponent } from './pages/equipments/equipments'; 
import { LoansComponent } from './pages/loans/loans';          
import { authGuard } from './guards/auth-guard';               

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  
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
  }
];