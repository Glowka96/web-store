import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss'],
})
export class BoardComponent implements OnInit {
  private isLogged!: string;

  constructor(private authService: AuthenticationService) {
    authService.isLoggedRole().subscribe((value) => {
      this.isLogged = value;
    });
  }

  ngOnInit(): void {}
}
