import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModCategoryComponent } from './mod-category.component';

describe('ModCategoryComponent', () => {
  let component: ModCategoryComponent;
  let fixture: ComponentFixture<ModCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModCategoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
