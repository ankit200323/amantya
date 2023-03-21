import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { DashboardService } from 'src/app/services/dashboard.service';
import { WebsocketapiService } from 'src/app/services/websocketapi.service';

import { ProcessedDashboardDataComponent } from './processed-dashboard-data.component';

describe('ProcessedDashboardDataComponent', () => {
  let component: ProcessedDashboardDataComponent;
  let fixture: ComponentFixture<ProcessedDashboardDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatSnackBarModule],
      declarations: [ ProcessedDashboardDataComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProcessedDashboardDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call ngOnInit method', () =>{
    expect(component.ngOnInit()).toBeUndefined();
  })

  it('should call disableProccessedView method', () =>{
    expect(component.disableProccessedView()).toBeUndefined();
  })

  it('should call getServerData method', () =>{
    expect(component.getServerData({pageIndex:1, pageSize: 10})).toBeUndefined();
  })
});
