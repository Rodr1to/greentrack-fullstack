import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar';
import { RouterLink } from '@angular/router';
import { EquipmentService } from '../../services/equipment';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NavbarComponent, RouterLink, CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private equipmentService = inject(EquipmentService);

  stats = {
    totalEquipos: 0,
    disponibles: 0,
    prestados: 0
  };

  selectedBrand: string = 'TODOS';
  
  brands: string[] = ['TODOS'];

  ngOnInit() {
    this.loadBrands(); 
    this.loadStats();  
  }

  loadBrands() {
    this.equipmentService.getBrands().subscribe({
      next: (data) => {
        // Agregamos 'TODOS' al principio de la lista que viene de la BD
        this.brands = ['TODOS', ...data];
      },
      error: (err) => console.error('Error cargando marcas', err)
    });
  }

  loadStats() {
    this.equipmentService.getStats(this.selectedBrand).subscribe({
      next: (data) => this.stats = data,
      error: (err) => console.error('Error cargando stats', err)
    });
  }

  onFilterChange() {
    this.loadStats();
  }
}
