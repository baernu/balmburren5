import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateProductDetailsComponent } from './create-product-details.component';

describe('CreateProductDetailsComponent', () => {
  let component: CreateProductDetailsComponent;
  let fixture: ComponentFixture<CreateProductDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateProductDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateProductDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
