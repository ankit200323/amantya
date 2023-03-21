import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'hum-verizon-UI';

  constructor(private authGuad: AuthGuard,
              private router: Router){}

  ngOnInit(){
    this.checkTokenExpiration();
  }

  checkTokenExpiration() {
    const isAuthenticated = this.authGuad.getAuthStatus(localStorage.getItem('loginUser'));
    if (!isAuthenticated) {
      this.router.navigate(['/']);
    }else{
      this.router.navigate(['dashboard/user']);
    }
  }
}
