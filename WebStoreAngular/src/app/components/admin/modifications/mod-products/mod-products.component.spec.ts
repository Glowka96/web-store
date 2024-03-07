import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModProductsComponent } from './mod-products.component';

describe('ModProductsComponent', () => {
  let component: ModProductsComponent;
  let fixture: ComponentFixture<ModProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModProductsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
