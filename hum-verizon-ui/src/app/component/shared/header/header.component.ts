import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DashboardService } from 'src/app/services/dashboard.service';
import { WebsocketapiService } from 'src/app/services/websocketapi.service';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import * as CONSTANT from '../../../constants/constant';
import * as URL from '../../../urls/url';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(public dashboardService: DashboardService, private router: Router,
    public webSocketAPI: WebsocketapiService,
    private _snackBar: MatSnackBar,
    public dialog: MatDialog) { }

  topicName: any = CONSTANT.REGIONAL_TOPIC;
  isClientSet: boolean = false;
  hide = true;
  isBtnDisable: boolean = false;
  connectingTime: boolean = false;

  //variables to change the mqtt user
  url: string = 'tcp://mqtt.vzmode-br.dltdemo.io:1883';
  userName: string = 'user3';
  password: string = 'dfFg7sEX52BQ';
  isUrlValid: boolean = true;
  hideShowConnectBtn: boolean = false;
  isPanelOpen: boolean = false;
  headerOpacity: boolean = false;
  clientType: string = 'all';
  isTopicApplied: boolean = false;

  ngOnInit(): void {
    this.dashboardService.$hideShowConnectBtn.subscribe((val: any) => {
      this.hideShowConnectBtn = val;
    })
    if (localStorage.getItem('isConnected') && localStorage.getItem('isConnected') == 'true') {
      this.isClientSet = true;
    } else {
      this.isClientSet = false;
    }
    if (localStorage.getItem('isTopicApplied') && localStorage.getItem('isTopicApplied') == 'true') {
      this.isTopicApplied = true;
    }
    if (localStorage.getItem('mqtt_url')
      && localStorage.getItem('mqtt_username')
      && localStorage.getItem('mqtt_password')) {
      this.url = localStorage.getItem('mqtt_url') || '';
      this.userName = localStorage.getItem('mqtt_username') || '';
      this.password = localStorage.getItem('mqtt_password') || '';
      if (localStorage.getItem('topicName')) {
        this.topicName = localStorage.getItem('topicName');
      }
      if (this.isClientSet) {
        this.dashboardService.setClientDetails(true);
        this.connectMqttServer(true);
      }
    }
    window.addEventListener("storage", (event: any) => {
      window.location.reload();
    });
    this.dashboardService.$headerOpacity.subscribe((res: any) => {
      this.headerOpacity = res;
    })

    this.dashboardService.$isClientSet.subscribe((res: any) => {
      this.isClientSet = res;
    })
  }

  connectMqttServer(isFromReload?: any) {
    let obj = {
      username: this.userName,
      password: this.password,
      url: this.url,
      sessionId: localStorage.getItem("sessionId")
    }
    if (obj.sessionId == null) {
      obj.sessionId = '';
    }
    this.webSocketAPI.loading = true;
    this.connectingTime = true;
    this.dashboardService.fetchData('post', URL.changeUser, obj).subscribe((res: any) => {
      this.webSocketAPI.loading = false;
      this.connectingTime = false;
      localStorage.setItem("mqtt_username", this.userName);
      localStorage.setItem("mqtt_password", this.password);
      localStorage.setItem("mqtt_url", this.url);
      if (res && res['status']) {
        this.dashboardService.isServerConnected(true);
        this.password = '';
        this.isClientSet = true;
        localStorage.setItem("isConnected", 'true');
        this.openSnackBar('Connected successfully to mqtt server', 'end', 'top', 'green-snackbar', '');
        this.webSocketAPI.connect();
        if (isFromReload && this.isTopicApplied) {
          setTimeout(() => {
            this.getMQTTData();
          }, 2000);
        }
      } else {
        this.openSnackBar(res['message'], 'end', 'top', 'red-snackbar', 'Error');
      }
    }, (error: any) => {
      this.connectingTime = false;
      this.webSocketAPI.loading = false;
      if (error && error['message']) {
        this.dashboardService.isServerConnected(true);
        this.openSnackBar('Invalid MQTT credentials', 'end', 'top', 'red-snackbar', 'Error');
      }
    })
  }

  validateURL() {
    this.isUrlValid = true;
    let expression = /([-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)):(\d[0-9]*)\/?/g
    let regex = new RegExp(expression);
    if (this.url.match(regex)) {
      this.isUrlValid = true;
    } else {
      this.isUrlValid = false;
    }
    this.isClientDisabled();
  }

  isClientDisabled() {
    if (this.userName && this.userName.trim() &&
      this.password && this.password.trim() &&
      this.url && this.url.trim()) {
      this.isBtnDisable = false;
    } else {
      this.isBtnDisable = true;
    }
  }

  logout() {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '35%',
    });
    dialogRef.afterClosed().subscribe((result: any) => {
      if (result) {
        if (this.isClientSet) {
          this.isClientSet = false;
          this.isBtnDisable = false;
          this.webSocketAPI.disconnect();
        }
        sessionStorage.clear();
        localStorage.clear();
        this.dashboardService.isClientSet.unsubscribe();
        this.dashboardService.isConnected.unsubscribe();
        this.dashboardService.dashboardData.unsubscribe();
        this.router.navigate(['/']);
      }
    });
  }

  disconnectSession() {
    this.isClientSet = !this.isClientSet;
    this.webSocketAPI.disconnect();
    this.isClientDisabled();
  }

  onPaste(type: any) {
    setTimeout(() => {
      if (type == 'url') {
        this.validateURL();
      } else {
        this.isClientDisabled();
      }
    }, 0);
  }

  onChangeClient(event: any) {
    if (event == 'individual') {
      this.topicName = CONSTANT.INDIVIDUAL_TOPIC;
    } else {
      this.topicName = CONSTANT.REGIONAL_TOPIC;
    }
  }

  getMQTTData() {
    this.dashboardService.isServerConnected(true);
    this.connectingTime = true;
    localStorage.setItem(CONSTANT.TOPIC_NAME, this.topicName);
    let obj = {
      topicName: this.topicName,
      sessionId: localStorage.getItem('sessionId'),
      filterName: "ALL"
    }
    this.webSocketAPI.loading = true;
    this.dashboardService.fetchData('put', URL.changeTopic, obj).subscribe((res: any) => {
      localStorage.setItem('isTopicApplied', 'true');
      this.isTopicApplied = true;
      this.webSocketAPI.loading = false;
      this.connectingTime = false;
      this.isPanelOpen = false;
    }, (error: any) => {
      this._snackBar.open(`Error: Something went wrong. Please Apply again`, '', {
        horizontalPosition: 'end',
        verticalPosition: 'top',
        duration: 2000,
        panelClass: ['red-snackbar']
      });
      this.webSocketAPI.loading = false;
      this.connectingTime = false;
      this.isPanelOpen = false;
    });
  }

  checkFormFields() {
    if (this.userName && this.userName.trim() &&
      this.password && this.password.trim() &&
      this.url && this.url.trim()) {
      return true;
    } else {
      return false;
    }
  }

  openSnackBar(textContent: string, horPosition: any, vertPosition: any, className: string, errorMsg: string) {
    this._snackBar.open(textContent, errorMsg, {
      horizontalPosition: horPosition,
      verticalPosition: vertPosition,
      duration: 2000,
      panelClass: [className]
    });
  }
}
