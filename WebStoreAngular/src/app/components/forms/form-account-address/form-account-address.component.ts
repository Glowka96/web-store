import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { AccountAddress } from 'src/app/models/account-address';
import { AccountService } from 'src/app/services/accounts/account.service';
import { AddressFormBuilderService } from 'src/app/services/forms/users/address-form-builder.service';

@Component({
  selector: 'app-form-account-address',
  templateUrl: './form-account-address.component.html',
  styleUrls: ['./form-account-address.component.scss'],
})
export class FormAccountAddressComponent implements OnInit {
  private isAccountAddress = false;
  private titleButton = 'Add';
  private errorMessage!: string;

  public accountAddressForm!: FormGroup;

  constructor(
    private accountService: AccountService,
    private addressFormService: AddressFormBuilderService,
    private router: Router
  ) {
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

  ngOnInit(): void {
    this.accountAddressForm =
      this.addressFormService.createAccountAddressFormGroup();
  }

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
