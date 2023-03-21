import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';

const routes: Routes = [
  { path: '',
    loadChildren: () => import('./component/login/login.module').then(m => m.LoginModule),
  },
  { path: 'dashboard', 
    loadChildren: () => import('./component/user/user.module').then(m => m.UserModule),
    canActivate: [AuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
