import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserorderKathyComponent } from './userorder-kathy.component';

describe('UserorderKathyComponent', () => {
  let component: UserorderKathyComponent;
  let fixture: ComponentFixture<UserorderKathyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserorderKathyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserorderKathyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
