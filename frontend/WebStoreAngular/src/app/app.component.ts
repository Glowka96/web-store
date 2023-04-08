import { Component, HostListener, OnInit } from '@angular/core';
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
  private currentTime!: number;

  constructor(private formLoginService: FormLoginService) {
    formLoginService.getOpacitySubject().subscribe((value) => {
      this.opacity = value;
    });
  }

  ngOnInit(): void {}

  // ngOnInit(): void {
  //   console.log('started component app');
  //   document.addEventListener('click', () => this.handleClick());
  // }

  // ngOnDestroy(): void {
  //   document.removeEventListener('click', this.handleClick.bind(this));
  // }

  // private handleClick() {
  //   console.log('handleClick() called');
  //   if (this.opacity === 0.2) {
  //    // this.currentTime = new Date().getTime();
  //     //const lastClickTime = this.formLoginService.getLastClickTime();
  //     //console.log(lastClickTime);
  //     //if (this.currentTime - lastClickTime > 500 || lastClickTime === 0) {
  //       this.formLoginService.changeStatusFormLogin();
  //       //this.formLoginService.setLastClickTime(this.currentTime);
  //     //}
  //   }
  // }

  public get getOpacity() {
    // this.currentTime = new Date().getTime();
    //  this.formLoginService.setLastClickTime(this.currentTime);
    return this.opacity;
  }

  public get getOpacity2() {
    return this.opacity;
  }

  public get showLoginForm(): boolean {
    return this.showLoginForm;
  }
}
