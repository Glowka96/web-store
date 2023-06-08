import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModProducerComponent } from './mod-producer.component';

describe('ModProducerComponent', () => {
  let component: ModProducerComponent;
  let fixture: ComponentFixture<ModProducerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModProducerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModProducerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
