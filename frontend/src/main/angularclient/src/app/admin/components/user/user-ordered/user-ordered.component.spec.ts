import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserOrderedComponent } from './user-ordered.component';

describe('UserOrderedComponent', () => {
  let component: UserOrderedComponent;
  let fixture: ComponentFixture<UserOrderedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserOrderedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserOrderedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
