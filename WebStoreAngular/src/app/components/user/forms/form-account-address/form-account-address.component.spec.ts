import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormAccountAddressComponent } from './form-account-address.component';

describe('FormAccountAddressComponent', () => {
  let component: FormAccountAddressComponent;
  let fixture: ComponentFixture<FormAccountAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormAccountAddressComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormAccountAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
