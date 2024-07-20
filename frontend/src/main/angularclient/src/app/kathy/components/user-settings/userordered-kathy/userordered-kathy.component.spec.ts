import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserorderedKathyComponent } from './userordered-kathy.component';

describe('UserorderedKathyComponent', () => {
  let component: UserorderedKathyComponent;
  let fixture: ComponentFixture<UserorderedKathyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserorderedKathyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserorderedKathyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
