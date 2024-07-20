import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoiceEmailPreviewComponent } from './invoice-email-preview.component';

describe('InvoiceEmailPreviewComponent', () => {
  let component: InvoiceEmailPreviewComponent;
  let fixture: ComponentFixture<InvoiceEmailPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InvoiceEmailPreviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvoiceEmailPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
