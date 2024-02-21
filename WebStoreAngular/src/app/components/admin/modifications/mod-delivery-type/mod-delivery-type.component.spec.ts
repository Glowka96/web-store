import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModDeliveryTypeComponent } from './mod-delivery-type.component';

describe('ModDeliveryTypeComponent', () => {
  let component: ModDeliveryTypeComponent;
  let fixture: ComponentFixture<ModDeliveryTypeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModDeliveryTypeComponent]
    });
    fixture = TestBed.createComponent(ModDeliveryTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
