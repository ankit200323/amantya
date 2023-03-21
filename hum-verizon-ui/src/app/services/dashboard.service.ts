import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  readonly baseUrl = environment.baseUrl;
  isClientSet = new BehaviorSubject(false);
  $isClientSet = this.isClientSet.asObservable();
  isConnected = new BehaviorSubject(false);
  $isConnected = this.isConnected.asObservable();

  dashboardData = new BehaviorSubject(false);
  $dashboardData = this.dashboardData.asObservable();

  hideShowConnectBtn = new BehaviorSubject(false);
  $hideShowConnectBtn = this.hideShowConnectBtn.asObservable();

  headerOpacity = new BehaviorSubject(false);
  $headerOpacity = this.headerOpacity.asObservable();

  constructor(private httpClient: HttpClient) { }

  setDashboardData(val: any){
    this.dashboardData.next(val);
  }

  setClientDetails(clientDetail:any){
    this.isClientSet.next(clientDetail);
  }

  isServerConnected(val:any){
    this.isConnected.next(val);
  }

  changeConnectBtn(val: any){
    this.hideShowConnectBtn.next(val);
  }

  setHeaderOpacity(val: any){
    this.headerOpacity.next(val);
  }

  fetchData(apiType: string, urlContext: string, dataObj?: any){
    if(apiType == 'get'){
      return this.httpClient.get(this.baseUrl+urlContext);
    }else if(apiType == 'post'){
      return this.httpClient.post(this.baseUrl+urlContext, dataObj);
    }else if(apiType == 'put'){
      return this.httpClient.put(this.baseUrl+urlContext, dataObj);
    }
    return this.httpClient.get(this.baseUrl+urlContext);
  }
}
