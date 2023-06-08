import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModSubcategoryComponent } from './mod-subcategory.component';

describe('ModSubcategoryComponent', () => {
  let component: ModSubcategoryComponent;
  let fixture: ComponentFixture<ModSubcategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModSubcategoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModSubcategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
