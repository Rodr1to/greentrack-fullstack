import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoansComponent } from './loans'; 
import { HttpClientTestingModule } from '@angular/common/http/testing'; 
import { ToastrModule } from 'ngx-toastr'; 

describe('LoansComponent', () => {
  let component: LoansComponent;
  let fixture: ComponentFixture<LoansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        LoansComponent, 
        HttpClientTestingModule, 
        ToastrModule.forRoot()   
      ] 
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
