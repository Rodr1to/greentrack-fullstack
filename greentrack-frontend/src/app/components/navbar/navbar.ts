import { Component, inject, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule], 
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent implements OnInit {
  private authService = inject(AuthService);

  username: string = '';
  isAdmin: boolean = false;

  ngOnInit() {
    this.username = localStorage.getItem('username') || 'Usuario';
    const role = localStorage.getItem('role');
    this.isAdmin = role === 'ADMIN';
  }

  logout() {
    this.authService.logout();
  }
}