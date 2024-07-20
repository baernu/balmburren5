import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActualTourComponent } from './actual-tour.component';

describe('ActualTourComponent', () => {
  let component: ActualTourComponent;
  let fixture: ComponentFixture<ActualTourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActualTourComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActualTourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
