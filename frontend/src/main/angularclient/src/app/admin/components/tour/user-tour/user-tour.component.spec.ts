import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserTourComponent } from './user-tour.component';

describe('UserTourComponent', () => {
  let component: UserTourComponent;
  let fixture: ComponentFixture<UserTourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserTourComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserTourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
