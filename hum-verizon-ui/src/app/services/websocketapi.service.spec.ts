import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { WebsocketapiService } from './websocketapi.service';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

describe('WebsocketapiService', () => {
  let service: WebsocketapiService;
  let httpMock: HttpTestingController;

  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule,MatSnackBarModule], 
    providers: [WebsocketapiService]
  }));

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        { provide: WebsocketapiService }
      ],
      imports: [HttpClientTestingModule, MatSnackBarModule,]
    })
      .compileComponents();

    service = TestBed.get(WebsocketapiService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    const service: WebsocketapiService = TestBed.get(WebsocketapiService);
    expect(service).toBeTruthy();
   });

   it('Happy path with true value updateTopic method', () =>{
     service.firstTimeSubscription = true;
    service.updateTopic('REGIONAL/DYN/4/#','ALL',true);
    expect(service.connect).toBeTruthy();
    expect(service.sendNotification(service.topicName, service.filterName, service.isSubscribe, 'C21CBA2693221D27266425091AAD4AA7')).toBeUndefined();
  })

  it('Happy path with false value updateTopic method', () =>{
    service.firstTimeSubscription = false;
    service.updateTopic('REGIONAL/DYN/4/#','ALL',true);
    expect(service.sendNotification(service.topicName, service.filterName, service.isSubscribe, 'C21CBA2693221D27266425091AAD4AA7')).toBeUndefined();
  })

  it('Happy path with false value connect method', () =>{
    expect(service.connect(true)).toBeUndefined(); 
  })

  it('Happy path for connectMqttServer method', () =>{
    expect(service.connectMqttServer()).toBeUndefined(); 
  })

  // it('Happy path for intervalCall method', () =>{
  //   let ws = new SockJS(service.webSocketEndPoint);
  //   service.stompClient = Stomp.over(ws);
  //   service.stompClient.connect({}, (frame: any) => {
  //   })
  //   expect(service.intervalCall(service.stompClient)).toBeUndefined(); 
  // })

  it('Happy path for onMessageReceived method', () =>{
    expect(service.onMessageReceived('message')).toBeUndefined(); 
  })
});
