import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RegistrationService } from 'src/app/services/registration.service';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss'],
})
export class ConfirmComponent implements OnInit {
  private token!: string;
  private confirmationMessage!: string;

  constructor(
    private registrationService: RegistrationService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.token = params['token'];
      this.confirmAccount(this.token);
    });
  }

  confirmAccount(token: string): void {
    this.registrationService.confirmAccount(token).subscribe({
      next: (response) => {
        this.confirmationMessage = response.message;
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 2000);
      },
      error: () => {
        this.confirmationMessage = 'Account confirmation failed';
      },
    });
  }

  public get confirmationMsg() {
    return this.confirmationMessage;
  }
}
