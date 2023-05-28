import { Component, OnInit } from '@angular/core';
import { __values } from 'tslib';
import { FormLoginService } from './services/form-login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'WebStore';
  private formLogin: boolean = false;

  constructor(private formLoginService: FormLoginService) {
    formLoginService.getIsFromLogin$().subscribe((value) => {
      this.formLogin = value;
    });
  }

  ngOnInit(): void {}

  public get isFormLogin() {
    return this.formLogin;
  }
}
