import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModProductTypeComponent } from './mod-product-type.component';

describe('ModProductTypeComponent', () => {
  let component: ModProductTypeComponent;
  let fixture: ComponentFixture<ModProductTypeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModProductTypeComponent]
    });
    fixture = TestBed.createComponent(ModProductTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
