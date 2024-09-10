import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverWorkComponent } from './driver-work.component';

describe('DriverWorkComponent', () => {
  let component: DriverWorkComponent;
  let fixture: ComponentFixture<DriverWorkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverWorkComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverWorkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
