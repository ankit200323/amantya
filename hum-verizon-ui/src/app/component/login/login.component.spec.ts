import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DebugElement } from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DashboardComponent } from '../user/dashboard/dashboard.component';
import { LoginComponent } from './login.component';
import { LoginService } from './login.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let service: LoginService;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        { provide: LoginService }
      ],
      imports: [HttpClientTestingModule,
        FormsModule,
        MatProgressSpinnerModule,
        MatSnackBarModule,
        MatFormFieldModule,
        MatInputModule,
        BrowserAnimationsModule],
      declarations: [LoginComponent, DashboardComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    service = TestBed.get(LoginService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  })

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call ngOnInit method', () =>{
    expect(component.ngOnInit()).toBeUndefined();
  })

  // it('should call setLoginFormData method', () =>{
  //   expect(component.setLoginFormData({status: 200, msg: 'successfully logged in'})).toBeUndefined();
  // })

  it('should call removeCookies method', () =>{
    expect(component.removeCookies()).toBeUndefined();
  })

  it('should call openSnackBar method', () =>{
    expect(component.openSnackBar('Something went wrong', 'end', 'top', 'red-snackbar', 'Error')).toBeUndefined();
  })

  // it('should call submit method', () =>{
  //   expect(component.submit()).toBeUndefined();
  // })

  it(
		'should render login form  ',
		waitForAsync(() => {
      const bannerDe: DebugElement = fixture.debugElement;
      fixture.detectChanges();
      const paragraphDe = bannerDe.query(By.css('.container'));
      const p: HTMLElement = paragraphDe.nativeElement;
      expect(p.textContent).toBeTruthy();
		})
	);

  it('should call the `login` in loginSerive API and get success message', () => {
    const dummyRequest = [{
      username: component.userName,
      password: component.password
    }]
    const dummyResponse = 'Successfully logged in';
    service.login(dummyRequest).subscribe((res: any) => {
      expect(res).toEqual('Successfully logged in');
    })
    const request = httpMock.expectOne(`${service.baseUrl}/login`);
    expect(request.request.method).toBe('POST');
    request.flush(dummyResponse);

  });

});