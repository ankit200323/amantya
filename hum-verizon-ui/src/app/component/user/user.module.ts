import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from 'src/app/auth/auth.guard';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { NgApexchartsModule } from 'ng-apexcharts';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatMenuModule } from '@angular/material/menu';
import { AngularOpenlayersModule } from 'ngx-openlayers';
import { ProcessedDashboardDataComponent } from './processed-dashboard-data/processed-dashboard-data.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { CalendarModule } from 'primeng/calendar';
import { CheckboxModule } from 'primeng/checkbox';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputTextModule } from 'primeng/inputtext';
const routes: Routes = [
  {
    path: 'user',
    component: DashboardComponent
  }
];

@NgModule({
  declarations: [
    DashboardComponent,
    ProcessedDashboardDataComponent,
  ],
  imports: [
    CommonModule, ReactiveFormsModule, FormsModule, SharedModule, MatExpansionModule, MatFormFieldModule, MatInputModule,
    Ng2SearchPipeModule, NgApexchartsModule, MatSelectModule, MatDialogModule,
    MatMenuModule, AngularOpenlayersModule, MatTableModule, MatPaginatorModule, CalendarModule,
    MatButtonModule, CheckboxModule, RadioButtonModule, InputTextModule,
    MatProgressSpinnerModule, [RouterModule.forChild(routes)]
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class UserModule { }
