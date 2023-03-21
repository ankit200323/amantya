import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DashboardService } from './dashboard.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';

describe('DashboardService', () => {
  let service: DashboardService;
  let fixture: ComponentFixture<DashboardService>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        { provide: DashboardService }
      ],
      imports: [HttpClientTestingModule,
        FormsModule,
        MatProgressSpinnerModule,
        MatSnackBarModule,
        MatFormFieldModule,
        MatInputModule,
        BrowserAnimationsModule]
    })
      .compileComponents();

    // fixture = TestBed.createComponent(DashboardService);
    // fixture.detectChanges();
    service = TestBed.get(DashboardService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    const service: DashboardService = TestBed.get(DashboardService);
    expect(service).toBeTruthy();
   });

  //  it('should call the `login` API and get success message', () => {
  //   const dummyRequest = [{
  //     username: component.userName,
  //     password: component.password
  //   }]
  //   const dummyResponse = 'Successfully logged in';
  //   service.login(dummyRequest).subscribe((res: any) => {
  //     expect(res).toEqual('Successfully logged in');
  //   })

  //   const request = httpMock.expectOne(`${service.baseUrl}/login`);
  //   expect(request.request.method).toBe('POST');
  //   request.flush(dummyResponse);
  // });
});
