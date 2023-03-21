import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { WebSocketShareService } from './web-socket-share.service';
import { DashboardService } from './dashboard.service';
import { environment } from 'src/environments/environment';
import { TopicData } from './topicData';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import * as URL from '../urls/url';
@Injectable({
  providedIn: 'root'
})
export class WebsocketapiService {
  webSocketEndPoint: string = environment.socketUrl;
  webSocket_topic: string = '/topic/rsadata'; // receive of all information mqtt
  connectionCheck: string = '/app/checkConnectivity';
  notifyCheck: string = '/app/notify';
  stompClient: any;
  topicName: any = '';
  isSubscribe: boolean = true;
  filterName: any = true;
  firstTimeSubscription: boolean = true;
  loading: boolean = false;
  readonly baseUrl = environment.baseUrl;
  constructor(private websocketShare: WebSocketShareService,
    private dashboardService: DashboardService,
    private _snackBar: MatSnackBar,
    private httpClient: HttpClient) {
  }

  updateTopic(topic: any, filterName: any, isubscribe: any) {
    this.topicName = topic;
    this.isSubscribe = isubscribe;
    this.filterName = filterName;
    if (this.firstTimeSubscription == true) {
      this.connect();
      this.sendNotification(this.topicName, this.filterName, this.isSubscribe, localStorage.getItem("sessionId"));
    } else {
      this.sendNotification(this.topicName, this.filterName, this.isSubscribe, localStorage.getItem("sessionId"));
    }
  }

  connect(isFromReconnect?: any) {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({}, (frame: any) => {

      // common topic to receieve mqtt data
      const sessionId = localStorage.getItem('sessionId');
      console.log("webSocket_topic " + sessionId);
      const updateWebSocketTopic = this.webSocket_topic + "/" + sessionId;
      console.log("webSocket_topic " + updateWebSocketTopic);
      this.stompClient.subscribe(updateWebSocketTopic, (sdkEvent: any) => {
        console.log("this.topicName------->", this.topicName);
        this.onMessageReceived(sdkEvent);
      });

      this.firstTimeSubscription = false;

      // run every 5 minutes and send session id
      setInterval(() => this.intervalCall(this.stompClient), 300000);
      if (isFromReconnect) {
        this.connectMqttServer();
      }
      console.log("Successfully finish all topic setup");
    }, (error: any) => {   // on error, schedule a reconnection attempt
      console.log("errorCallBack, going to reconnect-> " + error);
      this.connect(true);
    });
  }

  connectMqttServer() {
    let obj = {
      username: localStorage.getItem("mqtt_username"),
      password: localStorage.getItem("mqtt_password"),
      url: localStorage.getItem("mqtt_url"),
      sessionId: localStorage.getItem("sessionId")
    }
    if (obj.sessionId == null) {
      obj.sessionId = '';
    }
    this.dashboardService.fetchData('post', URL.changeUser, obj).subscribe((res: any) => {
      if (res && res['status']) {
        this.dashboardService.isServerConnected(true);
        localStorage.setItem("isConnected", 'true');
        this._snackBar.open('Connected successfully to mqtt server', '', {
          horizontalPosition: 'end',
          verticalPosition: 'top',
          duration: 2000,
          panelClass: ['green-snackbar']
        });
        this.connect();
        setTimeout(() => {
          let obj = {
            topicName: localStorage.getItem('topicName'),
            sessionId: localStorage.getItem('sessionId'),
            filterName: "ALL"
          }
          this.dashboardService.fetchData('put', URL.changeTopic, obj).subscribe((res: any) => {
            localStorage.setItem('isTopicApplied', 'true');
          });
        }, 2000);
      } else {
        this._snackBar.open(`Error: ${res['message']}`, '', {
          horizontalPosition: 'end',
          verticalPosition: 'top',
          duration: 2000,
          panelClass: ['red-snackbar']
        });
      }
    })
  }

  intervalCall(stompClient: any) {
    stompClient.send(this.connectionCheck, function (sdkEvent: any) {
      console.log("check connection--------->", sdkEvent);
    }, localStorage.getItem("sessionId"));
  }

  onMessageReceived(message: any) {
    this.websocketShare.onNewValueReceive(message.body);
  }

  sendNotification(topicName: any, filterName: any, isSubscribe: any, sessionId: any) {
    const data: TopicData = {
      topicName: topicName,
      isSubscribe: isSubscribe,
      filterName: filterName,
      sessionId: sessionId
    };
    if (this.stompClient && this.stompClient['connected']) {
      this.stompClient.send(this.notifyCheck, (sdkEvent: any) => {
      }, JSON.stringify(data));
    }
  }

  disconnect() {
    if (this.stompClient) {
      let url = this.baseUrl + URL.diconnect;
      this.loading = true;
      this.httpClient.post(url, localStorage.getItem('sessionId')).subscribe((res: any) => {
        this.loading = false;
        this.firstTimeSubscription = false;
        localStorage.removeItem('topicName');
        console.log("Disconnecting from MQTT: ", res);
        this.dashboardService.setClientDetails(false);
        this.dashboardService.isServerConnected(true);
        localStorage.setItem("isConnected", "false");
        this._snackBar.open('Disconnected successfully', '', {
          horizontalPosition: 'end',
          verticalPosition: 'top',
          duration: 2000,
          panelClass: ['green-snackbar']
        });
      }, (error) => {
        this.loading = false;
        this.dashboardService.setClientDetails(true);
        this.dashboardService.isServerConnected(false);
        this._snackBar.open('Error While disconnecting', '', {
          horizontalPosition: 'end',
          verticalPosition: 'top',
          duration: 2000,
          panelClass: ['red-snackbar']
        });
      });
    }
  }
}