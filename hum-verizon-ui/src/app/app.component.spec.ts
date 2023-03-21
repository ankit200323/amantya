import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { NgApexchartsModule } from 'ng-apexcharts';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { AppComponent } from './app.component';
import { AuthGuard } from './auth/auth.guard';
import { DashboardComponent } from './component/user/dashboard/dashboard.component';

describe('AppComponent', () => {
  let component: AppComponent;
  let authGuad: AuthGuard;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule,
        HttpClientTestingModule,
        MatDialogModule,
        MatSnackBarModule,
        RouterTestingModule.withRoutes([
          { path: 'dashboard/user', component: DashboardComponent}
      ])
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
    const fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges()
    authGuad = TestBed.get(AuthGuard);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it(`should have as title 'hum-verizon-UI'`, () => {
    expect(component.title).toEqual('hum-verizon-UI');
  });
});
