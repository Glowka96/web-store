import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModPromotionComponent } from './mod-promotion.component';

describe('ModPromotionComponent', () => {
  let component: ModPromotionComponent;
  let fixture: ComponentFixture<ModPromotionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModPromotionComponent]
    });
    fixture = TestBed.createComponent(ModPromotionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
