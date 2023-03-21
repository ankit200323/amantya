import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import * as URL from '../../urls/url';
@Injectable({
  providedIn: 'root'
})
export class LoginService {
  readonly baseUrl = environment.baseUrl;
  constructor(private httpClient: HttpClient) { }
  login(obj: any){
    let url = this.baseUrl + URL.login;
    return this.httpClient.post(url, obj);
  }
}
