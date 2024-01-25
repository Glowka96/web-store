import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbstractPageProductComponent } from './abstract-page-product.component';

describe('AbstractPageProductComponent', () => {
  let component: AbstractPageProductComponent;
  let fixture: ComponentFixture<AbstractPageProductComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AbstractPageProductComponent]
    });
    fixture = TestBed.createComponent(AbstractPageProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
