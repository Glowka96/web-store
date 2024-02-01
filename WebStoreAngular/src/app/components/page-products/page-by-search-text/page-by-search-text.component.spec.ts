import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageBySearchTextComponent } from './page-by-search-text.component';

describe('PageBySearchTextComponent', () => {
  let component: PageBySearchTextComponent;
  let fixture: ComponentFixture<PageBySearchTextComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageBySearchTextComponent]
    });
    fixture = TestBed.createComponent(PageBySearchTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
