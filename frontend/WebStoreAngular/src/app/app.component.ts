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
  private opacity: number = 1;

  constructor(private formLoginService: FormLoginService) {
    formLoginService.getOpacitySubject().subscribe((value) => {
      this.opacity = value;
    });
  }

  ngOnInit(): void {}

  public get getOpacity() {
    return this.opacity;
  }

  public get getOpacity2() {
    return this.opacity;
  }

  public get showLoginForm(): boolean {
    return this.showLoginForm;
  }
}
