import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) { }
    
  canActivate():  boolean {
      return this.checkTokenExpiration();
  }

  checkTokenExpiration() {
    const isAuthenticated = this.getAuthStatus(localStorage.getItem('loginUser'));
    if (!isAuthenticated) {
      this.router.navigate(['/']);
    }
    return isAuthenticated;
  }

  getAuthStatus(isUserLoggedin: any){
    if (isUserLoggedin) {
      return true;
  }
    return false;
  }
}
