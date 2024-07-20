import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KathyComponent } from './kathy.component';

describe('KathyComponent', () => {
  let component: KathyComponent;
  let fixture: ComponentFixture<KathyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KathyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KathyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
