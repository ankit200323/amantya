import { Injectable, OnDestroy } from "@angular/core";
import { Observable, BehaviorSubject, ReplaySubject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebSocketShareService implements OnDestroy{

  private blogDataSubject = new BehaviorSubject<any>({});
  constructor() { }
  ngOnDestroy(): void {
      this.blogDataSubject.unsubscribe();
  }
  
  onNewValueReceive(msg: string) {        
      this.blogDataSubject.next(msg);
  }
  getNewValue(): Observable<any> {
      return this.blogDataSubject .asObservable();
  }
}
