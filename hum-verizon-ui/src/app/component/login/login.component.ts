import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthGuard } from 'src/app/auth/auth.guard';
import { WebsocketapiService } from 'src/app/services/websocketapi.service';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  userName: string = '';
  password: string = '';
  hide: boolean = true;
  isLogin: boolean = false;

  constructor(private router: Router,
    private loginService: LoginService,
    private _snackBar: MatSnackBar,
    private webSocketAPI: WebsocketapiService,
    private authGuard: AuthGuard) { }

  ngOnInit(): void {
    this.removeCookies();
    window.addEventListener("storage", (event: any) => {
      window.location.reload();
    });
  }

  removeCookies() {
    this.webSocketAPI.loading = false;
  }

  submit() {
    this.isLogin = true;
    let obj = {
      username: this.userName,
      password: this.password
    }
    this.loginService.login(obj).subscribe((res: any) => {
      this.setLoginFormData(res);
    }, (error) => {
      this.openSnackBar('Something went wrong', 'end', 'top', 'red-snackbar', 'Error');
      this.isLogin = false;
    })
  }

  setLoginFormData(res: any) {
    if (res && res['status']) {
      localStorage.setItem('loginUser', 'admin');
      localStorage.setItem("sessionId", res['sessionId']);
      this.isLogin = false;
      if (this.authGuard.getAuthStatus(localStorage.getItem('loginUser'))) {
        this.router.navigate(['dashboard/user']);
      } else {
        this.router.navigate(['/']);
      }
      window.location.reload();
    } else {
      this.openSnackBar(res['message'], 'end', 'top', 'red-snackbar', 'Error');
      this.isLogin = false;
    }
  }

  openSnackBar(textContent: string, horPosition: any, vertPosition: any, className: string, errorMsg: string) {
    this._snackBar.open(textContent, errorMsg, {
      horizontalPosition: horPosition,
      verticalPosition: vertPosition,
      duration: 5000,
      panelClass: [className]
    });
  }
}
