import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { AccountAddress } from 'src/app/models/account-address';
import { AccountService } from 'src/app/services/accounts/account.service';

@Component({
  selector: 'app-form-account-address',
  templateUrl: './form-account-address.component.html',
  styleUrls: ['./form-account-address.component.scss'],
})
export class FormAccountAddressComponent implements OnInit {
  private isAccountAddress = false;
  private titleButton = 'Add';
  private errorMessage!: string;
  private postcodePattern = /^\d{2}-\d{3}$/;
  private addressPattern =
    /^(ul.?\s)?[A-Z]?[a-z]+\s\d{1,3}((\/\d{1,3})?|(\sm.?\s)\d{1,3})[a-z]?$/;

  public accountAddressForm = new FormGroup({
    city: new FormControl('', {
      validators: [
        Validators.required,
        Validators.min(2),
        Validators.max(32),
        Validators.pattern('[a-zA-z]+'),
      ],
      updateOn: 'change',
    }),
    postcode: new FormControl('', {
      validators: [
        Validators.required,
        Validators.pattern(this.postcodePattern),
      ],
      updateOn: 'change',
    }),
    street: new FormControl('', {
      validators: [
        Validators.required,
        Validators.pattern(this.addressPattern),
      ],
      updateOn: 'change',
    }),
  });

  constructor(private accountService: AccountService, private router: Router) {
    this.accountService
      .getAccount()
      .pipe(take(1))
      .subscribe((account) => {
        if (account.address) {
          this.isAccountAddress = true;
          this.titleButton = 'Update';
        }
      });
  }

  ngOnInit(): void {}

  onSumbite() {
    if (this.accountAddressForm.valid) {
      const request: AccountAddress = {
        city: this.accountAddressForm.controls['city']?.value ?? '',
        postcode: this.accountAddressForm.controls['postcode']?.value ?? '',
        street: this.accountAddressForm.controls['street']?.value ?? '',
      };
      if (this.isAccountAddress) {
        this.accountService.updateAddress(request).subscribe({
          next: () => {
            this.router.navigate(['/accounts'], {});
          },
          error: (error) => {
            const errorMessage = error.error.errors.join('<br>');
            this.errorMessage = errorMessage;
          },
        });
      } else {
        this.accountService.addAddress(request).subscribe({
          next: () => {
            this.router.navigate(['/accounts'], {});
          },
          error: (error) => {
            const errorMessage = error.error.errors.join('<br>');
            this.errorMessage = errorMessage;
          },
        });
      }
    }
  }

  public get getErrorMessage() {
    return this.errorMessage;
  }

  public get titleBtn() {
    return this.titleButton;
  }
}
