import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WagepaymentComponent } from './wagepayment.component';

describe('WagepaymentComponent', () => {
  let component: WagepaymentComponent;
  let fixture: ComponentFixture<WagepaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WagepaymentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WagepaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
