import { Component, OnInit } from '@angular/core';
import { RegistrationService } from 'src/app/services/accounts/registration.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-confirm-account',
  templateUrl: './confirm-account.component.html',
  styleUrls: ['./confirm-account.component.scss'],
})
export class ConfirmAccountComponent implements OnInit {
  private token!: string;
  private responseMessage!: string;

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

  private confirmAccount(token: string): void {
    this.registrationService.confirmAccount(token).subscribe({
      next: (response) => {
        this.responseMessage = response.message;
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 2000);
      },
      error: (error) => {
        const errorMessage = error.error.errors.join('<br>');
        this.responseMessage = errorMessage;
      },
    });
  }

  public get responseMsg() {
    return this.responseMessage;
  }
}
