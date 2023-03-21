import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { DashboardService } from 'src/app/services/dashboard.service';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import * as URL from '../../../urls/url';
import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  let isUrl = false;
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let confirmComponent: ConfirmationDialogComponent;
  let service: DashboardService;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule,
        FormsModule,
        MatButtonModule,
        MatFormFieldModule,
        MatInputModule,
        MatSnackBarModule,
        MatMenuModule,
        MatDialogModule,
        MatSelectModule],
      declarations: [ HeaderComponent, ConfirmationDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    service = TestBed.get(DashboardService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  })

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call ngOnInit method', () =>{
    component.url
    expect(component.ngOnInit());
  })

  it('should call validateURL method for true condition', () =>{
    component.url = 'tcp://mqtt.vzmode-br.dltdemo.io:1883';
    component.validateURL();
    expect(component.isUrlValid).toBe(true);
  })

  it('should call validateURL method for false condition', () =>{
    component.url = 'abcd';
    component.validateURL();
    expect(component.isUrlValid).toBe(false);
  })

  it('should call isClientDisabled method for true condition', () =>{
    component.userName = 'user3';
    component.password = 'dfFg7sEX52BQ';
    component.url = 'tcp://mqtt.vzmode-br.dltdemo.io:1883';
    component.isClientDisabled();
    expect(component.isBtnDisable).toBe(false);
  })

  it('should call isClientDisabled method for false condition', () =>{
    component.userName = '';
    component.password = '';
    component.url = '';
    component.isClientDisabled();
    expect(component.isBtnDisable).toBe(true);
  })

  it('Happy path with valid values onPaste method', () =>{
    component.onPaste('url');
    expect(component.validateURL).toBeTruthy();
  })

  it('Happy path with invalid values onPaste method', () =>{
    component.onPaste('abcd');
    expect(component.isClientDisabled).toBeTruthy();
  })

  it('should call disconnectSession method', () =>{
    expect(component.disconnectSession()).toBeUndefined();
  })

  it('should call checkFormFields method for if condition', () =>{
    component.userName = 'user3';
    component.password = 'dfFg7sEX52BQ';
    component.url = 'tcp://mqtt.vzmode-br.dltdemo.io:1883';
    expect(component.checkFormFields()).toEqual(true);
  })

  it('should call checkFormFields method for else condition', () =>{
    component.userName = '';
    component.password = '';
    component.url = '';
    expect(component.checkFormFields()).toEqual(false);
  })

  it('should call onChangeClient method for true condition', () =>{
    component.onChangeClient('individual');
    expect(component.topicName).toBe('VZCV2X/3/IN/#');
  })

  it('should call onChangeClient method for false condition', () =>{
    component.onChangeClient('regional');
    expect(component.topicName).toBe('REGIONAL/DYN/4/#');
  })

  it('should call the `setMqttUser` in dashboardService API and get success message', () => {
    const dummyRequest = [{
      username: component.userName,
      password: component.password,
      url: component.url,
      sessionId: localStorage.getItem("sessionId")
    }]
    const dummyResponse = 'Successfully logged in';
    service.fetchData('post', URL.changeUser, dummyRequest).subscribe((res: any) => {
      expect(res).toEqual('Successfully logged in');
    })

    const request = httpMock.expectOne(`${service.baseUrl}/mqttConnectionCheck`);
    expect(request.request.method).toBe('POST');
    request.flush(dummyResponse);
  });

  it('should call the connectMqttServer API for success case', () => {
    let dummyRequest = {
      username: 'user3',
      password: 'dfFg7sEX52BQ',
      url: 'tcp://mqtt.vzmode-br.dltdemo.io:1883',
      sessionId: localStorage.getItem("sessionId")
    }
    const dummyResponse = 'Successfully connected';
    service.fetchData('post', '/mqttConnectionCheck', dummyRequest).subscribe((res: any) => {
      expect(res).toEqual('Successfully connected');
    })
    const request = httpMock.expectOne(`${service.baseUrl}/mqttConnectionCheck`);
    expect(request.request.method).toBe('POST');
    request.flush(dummyResponse);

  });

  it('should call the connectMqttServer API for failure case', () => {
    let dummyRequest = {
      username: 'wrong',
      password: 'wrong',
      url: 'tcp://mqtt.vzmode-br.dltdemo.io:1883',
      sessionId: localStorage.getItem("sessionId")
    }
    const dummyResponse = 'invalid credential';
    service.fetchData('post', '/mqttConnectionCheck', dummyRequest).subscribe((res: any) => {
      expect(res).toEqual('invalid credential');
    })
    const request = httpMock.expectOne(`${service.baseUrl}/mqttConnectionCheck`);
    expect(request.request.method).toBe('POST');
    request.flush(dummyResponse);

  });
});
