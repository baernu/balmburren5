import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourDataComponent } from './tour-data.component';

describe('TourDataComponent', () => {
  let component: TourDataComponent;
  let fixture: ComponentFixture<TourDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TourDataComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
