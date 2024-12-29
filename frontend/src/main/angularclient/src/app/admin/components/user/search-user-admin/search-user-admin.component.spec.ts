import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchUserAdminComponent } from './search-user-admin.component';

describe('SearchUserAdminComponent', () => {
  let component: SearchUserAdminComponent;
  let fixture: ComponentFixture<SearchUserAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchUserAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchUserAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
