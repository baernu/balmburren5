import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserorderprofileComponent } from './userorderprofile.component';

describe('UserorderprofileComponent', () => {
  let component: UserorderprofileComponent;
  let fixture: ComponentFixture<UserorderprofileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserorderprofileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserorderprofileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
