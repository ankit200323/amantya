import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { SharedModule } from '../shared/shared.module';

const routes: Routes = [
  { path: '', 
    component: LoginComponent
  }
];

@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,FormsModule,MatProgressSpinnerModule, MatSnackBarModule,MatFormFieldModule,
    MatInputModule,SharedModule,
    RouterModule.forChild(routes)
  ]
})
export class LoginModule { }
