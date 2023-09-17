import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmResetPasswordComponent } from './confirm-reset-password.component';

describe('ConfirmResetPasswordComponent', () => {
  let component: ConfirmResetPasswordComponent;
  let fixture: ComponentFixture<ConfirmResetPasswordComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfirmResetPasswordComponent]
    });
    fixture = TestBed.createComponent(ConfirmResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
