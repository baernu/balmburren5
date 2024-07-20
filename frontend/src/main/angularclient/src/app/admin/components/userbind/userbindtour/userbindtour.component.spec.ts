import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserbindtourComponent } from './userbindtour.component';

describe('UserbindtourComponent', () => {
  let component: UserbindtourComponent;
  let fixture: ComponentFixture<UserbindtourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserbindtourComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserbindtourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
