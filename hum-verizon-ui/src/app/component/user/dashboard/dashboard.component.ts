import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DashboardService } from 'src/app/services/dashboard.service';
import { WebSocketShareService } from 'src/app/services/web-socket-share.service';
import { WebsocketapiService } from 'src/app/services/websocketapi.service';
import * as ApexCharts from 'apexcharts';
import { DashboardCalculation } from 'src/app/services/dashboard-calculation';
import * as CONSTANT from '../../../constants/constant';
import { MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {

  mapDataObj = {
    opacity: 1,
    latitude: 40.730610,
    longitude: -73.935242,
    zoom: 10,
    mapMode: 'Google Hybrid',
    vehicleUrl: '../assets/ic_vehicle_road.svg',
    pedestrianUrl: '../assets/pad.svg',
    emergencyVehicleUrl: '../assets/ic_emergency_road.svg',
    spatStationURL: '../assets/traffic-light.svg',
    vehicleIcon: '../../assets/vehicle_group_icon.svg',
    evaIcon: '../../assets/eva-card.svg',
    pedestrianIcon: '../../assets/ped_group_icon.svg',
    hideShowMap: true,
  }
  
  rightPanelDataObj = {
    selectedCar: {
      lat: 0,
      long: 0
    },
    isMapVisible: false,
    searchTerm: '',
    listVal: -1,
    checkedListIndex: -1,
    tabType: "vehicle",
    isRightPanelOpen: false,
    isDetailViewEnabled: false,
    isProccessedDataEnabled: false,
    getDetailInfo: false,
    currentDate: new Date(),
  }

  carDetailForm!: FormGroup;
  spatDetailForm!: FormGroup;

  //websocket variables
  topicName: string = CONSTANT.REGIONAL_TOPIC;
  filterType: string = "ALL";

  //2nd page variables
  pedestrianChart: any;
  pedChartOptions = {
    series: [{
      name: '',
      data: [0, 0],
    }
    ],
    zoom: {
      enabled: true,
    },
    chart: {
      overflow: 'auto',
      type: 'bar',
      height: 170,
      stacked: true,
      toolbar: {
        show: false
      }
    },
    plotOptions: {
      bar: {
        columnWidth: '80%',
        distributed: true,
      }
    },
    stroke: {
      width: 1,
      colors: ['#fff']
    },
    xaxis: {
      categories: ['Not Moving', 'Moving'],
      labels: {
        formatter: function (val: any) {
          return val
        }
      }
    },
    yaxis: {
      tickAmount: 1,
      title: {
        text: undefined
      },
      labels: {
        formatter: function (val: any) {
          return val.toFixed(0);
        }
      }
    },
    tooltip: {
      y: {
        formatter: function (val: any) {
          return val
        }
      }
    },
    fill: {
      opacity: 1
    },
    colors: ['#339F50', '#FFBC3D', '#ED7000'],
    legend: {
      position: 'bottom',
      horizontalAlign: 'left',
      offsetX: 20
    },
    labels: {
      style: {
        colors: ['#339F50', '#FFBC3D', '#ED7000'],
        fontSize: '12px',
      }
    }
  };

  
  signalType: any = {
    'stop-And-Remain': 1, 'stop-Then-Proceed': 1, 'pre-Movement': 3, 'permissive-Movement-Allowed': 3,
    'protected-Movement-Allowed': 3, 'permissive-clearance': 2, 'protected-clearance': 2, 'caution-Conflicting-Traffic': 2
  }

  constructor(
    private formBuilder: FormBuilder,
    private dashBoardService: DashboardService,
    private websocketService: WebSocketShareService,
    public webSocketAPI: WebsocketapiService,
    public dialog: MatDialog,
    private httpClient: HttpClient) { }

  @ViewChild('search', { static: false })
  public searchElementRef: ElementRef | undefined;

  ngOnInit(): void {
    this.dashBoardService.changeConnectBtn(false);
    this.carDetailForm = this.formBuilder.group({
      head_by: ['', Validators.required],
      latitude_car: ['', Validators.required],
      longitude_car: ['', Validators.required],
      speed_car: ['', Validators.required],
      direction: ['', Validators.required],
      unit: ['', Validators.required],
      vin_car: ['', Validators.required]
    });
    this.spatDetailForm = this.formBuilder.group({
      id: [''],
      speed: [0],
      name: [''],
      distance: [''],
      type: ['']
    })
    this.clearDashboardData();

    this.dashBoardService.$isConnected.subscribe((res: any) => {
      if (res) {
        DashboardCalculation.vehicleCircleArr.color = 'transparent';
        this.clearDashboardData();
        this.rightPanelDataObj.listVal = -1;
      }
    })

    this.onNewValueReceive();

    DashboardCalculation.timeCheckInterval = setInterval(() => {
      let dataObj = {
        dataTimeStampMap: DashboardCalculation.dataTimeStampMap,
        circleMarker: DashboardCalculation.circleMarker,
        leftSpatMenuOpen: DashboardCalculation.leftSpatMenuOpen,
        vehicleCircleArr: DashboardCalculation.vehicleCircleArr,
        allData: DashboardCalculation.allData,
        spatData: DashboardCalculation.spatData,
        dataCount: DashboardCalculation.dataCount,
        psgrType: DashboardCalculation.psgrType,
        truckType: DashboardCalculation.truckType,
        evType: DashboardCalculation.evType,
        pedestrianCardData: DashboardCalculation.pedestrianCardData,
        emergencyCardData: DashboardCalculation.emergencyCardData,
        type: 'removal',
        selectedSpatId: DashboardCalculation.selectedSpatId
      }
      if (typeof Worker !== 'undefined') {
        // Create a new
        const worker = new Worker(new URL('../../../app.worker', import.meta.url));
        worker.onmessage = ({ data }) => {
          DashboardCalculation.dataTimeStampMap = data.dataTimeStampMap,
            DashboardCalculation.circleMarker = data.circleMarker,
            DashboardCalculation.leftSpatMenuOpen = data.leftSpatMenuOpen,
            DashboardCalculation.vehicleCircleArr = data.vehicleCircleArr,
            DashboardCalculation.allData = data.allData,
            DashboardCalculation.spatData = data.spatData,
            DashboardCalculation.dataCount = data.dataCount,
            DashboardCalculation.psgrType = data.psgrType,
            DashboardCalculation.truckType = data.truckType,
            DashboardCalculation.evType = data.evType,
            DashboardCalculation.pedestrianCardData = data.pedestrianCardData,
            DashboardCalculation.emergencyCardData = data.emergencyCardData,
            DashboardCalculation.selectedSpatId = data.selectedSpatId
          if (this.rightPanelDataObj.isDetailViewEnabled) {
            this.getPedChartData();
          }
        };
        worker.postMessage(dataObj);
      } else {
        // Web Workers are not supported in this environment.
        // You should add a fallback so that your program still executes correctly.
      }
    }, 8000);
  }

  drawCharts() {
    this.pedestrianChart = new ApexCharts(document.getElementById("pedestrianChart"), this.pedChartOptions);
    this.pedestrianChart.render();
  }

  clearDashboardData() {
    DashboardCalculation.dataCount.pedestrian = 0;
    DashboardCalculation.dataCount.vehicle = 0;
    DashboardCalculation.dataCount.emergency = 0;
    DashboardCalculation.psgrType = 0;
    DashboardCalculation.truckType = 0;
    DashboardCalculation.evType = 0;
    DashboardCalculation.allData.clear();
    DashboardCalculation.spatData.clear();
    this.carDetailForm.reset();
    this.resetVehicleId();
  }

  onVehicleChange(val: number) {
    this.topicName = localStorage.getItem('topicName') || '';
    if (this.rightPanelDataObj.listVal != val) {
      DashboardCalculation.vehicleCircleArr.color = 'transparent';
      this.rightPanelDataObj.listVal = val;
      this.clearDashboardData();
      if (val == 0) {
        this.filterType = "ALL";
        this.webSocketAPI.updateTopic(this.topicName, this.filterType, true);
      } else if (val == 1) {
        this.filterType = CONSTANT.BSM;
        this.webSocketAPI.updateTopic(this.topicName, this.filterType, true);
      } else if (val == 2) {
        this.filterType = CONSTANT.EVA;
        this.webSocketAPI.updateTopic(this.topicName, this.filterType, true);
      }
      if (val == 3) {
        this.filterType = CONSTANT.PSM;
        this.webSocketAPI.updateTopic(this.topicName, this.filterType, true);
      }
    } else {
      this.rightPanelDataObj.listVal = -1;
    }
  }

  // method to receive the updated data.
  onNewValueReceive() {
    this.websocketService.getNewValue().subscribe((resp: any) => {
      if (resp && Object.keys(resp).length != 0) {
        try {
          let vehicleData = JSON.parse(resp);
            if (localStorage.getItem(CONSTANT.TOPIC_NAME)?.includes(CONSTANT.REGIONAL_3_TOPIC) || vehicleData[CONSTANT.TOPICTYPE] == CONSTANT.JER) {
              this.encodedValueReceived(vehicleData);
            } else {
              this.decodedValueReceived(vehicleData);
            }
        } catch (error) {
          console.log(error);
        }
      }
    });
  }

  encodedValueReceived(vehicleData: any) {
    if (vehicleData && vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE]) {
      this.setBSMDataOnMap(vehicleData);
    } else if (vehicleData && vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE]) {
      this.setPSMDataOnMap(vehicleData);
    }
    else if (vehicleData && vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]) {
      this.setEVADataOnMap(vehicleData);
    }
    else if(vehicleData && (vehicleData[CONSTANT.TYPE] == CONSTANT.MAP || vehicleData[CONSTANT.TYPE] == CONSTANT.SPAT)){
      this.encodedSPATValueReceived(vehicleData);
    }
  }

  decodedValueReceived(vehicleData: any) {
    if (vehicleData && vehicleData[CONSTANT.TYPE] == CONSTANT.BSM) {
      this.setDecodedBSMDataOnMap(vehicleData);
    } else if (vehicleData && vehicleData[CONSTANT.TYPE] == CONSTANT.PSM) {
      this.setDecodedPSMDataOnMap(vehicleData);
    }
    else if (vehicleData && vehicleData[CONSTANT.TYPE] == CONSTANT.EVA) {
      this.setDecodedEVADataOnMap(vehicleData);
    }
    else if(vehicleData && (vehicleData[CONSTANT.TYPE] == 'MAP' || vehicleData[CONSTANT.TYPE] == CONSTANT.SPAT)){
      this.decodedSPATValueReceived(vehicleData);
    }
  }

  setDecodedBSMDataOnMap(vehicleData: any){
    let direction = DashboardCalculation.convertToDirection(vehicleData[CONSTANT.COREDATA][CONSTANT.HEADING]);
    vehicleData[CONSTANT.COREDATA][CONSTANT.ICON] = this.mapDataObj.vehicleUrl;
    vehicleData[CONSTANT.COREDATA][CONSTANT.ICON_URL] = this.mapDataObj.vehicleIcon;
    vehicleData[CONSTANT.COREDATA][CONSTANT.LAT] = Number(vehicleData[CONSTANT.COREDATA][CONSTANT.LAT]) / 10000000;
    vehicleData[CONSTANT.COREDATA][CONSTANT.LONG] = Number(vehicleData[CONSTANT.COREDATA][CONSTANT.LONG]) / 10000000;
    vehicleData[CONSTANT.COREDATA][CONSTANT.SPEED] = Number((Number(vehicleData[CONSTANT.COREDATA][CONSTANT.SPEED]) * 3.6 * 0.62).toFixed(2));
    vehicleData[CONSTANT.COREDATA][CONSTANT.UNIT] = 'mph';
    vehicleData[CONSTANT.COREDATA][CONSTANT.DIRECTION] = direction;
    vehicleData[CONSTANT.COREDATA][CONSTANT.COLOR] = 'black';
    vehicleData[CONSTANT.COREDATA][CONSTANT.TYPE] = CONSTANT.BSM;
    vehicleData[CONSTANT.COREDATA][CONSTANT.SUBTYPE] = vehicleData[CONSTANT.SUBTYPE];
    DashboardCalculation.dataTimeStampMap.set(vehicleData[CONSTANT.COREDATA][CONSTANT.ID], this.rightPanelDataObj.currentDate.getTime());
    if (!DashboardCalculation.allData.has(vehicleData[CONSTANT.COREDATA][CONSTANT.ID])) {
      vehicleData[CONSTANT.COREDATA][CONSTANT.IS_OPEN] = false;
      DashboardCalculation.allData.set(vehicleData[CONSTANT.COREDATA][CONSTANT.ID], vehicleData[CONSTANT.COREDATA]);
      DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle + 1;
    }
    else {
      if (DashboardCalculation.circleMarker && DashboardCalculation.allData.has(DashboardCalculation.circleMarker.id) && vehicleData[CONSTANT.COREDATA][CONSTANT.ID] == DashboardCalculation.circleMarker.id) {
        DashboardCalculation.vehicleCircleArr.lat = vehicleData[CONSTANT.COREDATA][CONSTANT.LAT];
        DashboardCalculation.vehicleCircleArr.lng = vehicleData[CONSTANT.COREDATA][CONSTANT.LONG];
        if (this.rightPanelDataObj.checkedListIndex != -1 && (this.rightPanelDataObj.listVal == 1 || this.rightPanelDataObj.listVal == 0)) {
          this.mapDataObj.latitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).lat;
          this.mapDataObj.longitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).long;
          DashboardCalculation.vehicleCircleArr.color = '#0081F9';
          vehicleData[CONSTANT.COREDATA][CONSTANT.IS_OPEN] = true;
        }
      }
      if (DashboardCalculation.allData.get(vehicleData[CONSTANT.COREDATA][CONSTANT.ID]).type == CONSTANT.PSM) {
        if (DashboardCalculation.dataCount.pedestrian > 0) {
          DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian - 1;
        }
        if (DashboardCalculation.pedestrianCardData.has(vehicleData[CONSTANT.COREDATA][CONSTANT.ID])) {
          DashboardCalculation.pedestrianCardData.delete(vehicleData[CONSTANT.COREDATA][CONSTANT.ID]);
          DashboardCalculation.allData.get(vehicleData[CONSTANT.COREDATA][CONSTANT.ID]).type = CONSTANT.BSM;
        }
        DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle + 1;
      } else if (DashboardCalculation.allData.get(vehicleData[CONSTANT.COREDATA][CONSTANT.ID]).type == CONSTANT.EVA) {
        if (DashboardCalculation.dataCount.emergency > 0) {
          DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency - 1;
        }
        if (DashboardCalculation.emergencyCardData.has(vehicleData[CONSTANT.COREDATA][CONSTANT.ID])) {
          DashboardCalculation.emergencyCardData.delete(vehicleData[CONSTANT.COREDATA][CONSTANT.ID]);
          DashboardCalculation.allData.get(vehicleData[CONSTANT.COREDATA][CONSTANT.ID]).type = CONSTANT.BSM;
        }
        DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle + 1;
      }
      this.setVehicleInfo(1, vehicleData[CONSTANT.COREDATA]);
    }
    if (this.rightPanelDataObj.isDetailViewEnabled) {
      this.getPedChartData();
    }
  }

  setDecodedPSMDataOnMap(vehicleData: any){
    let direction = DashboardCalculation.convertToDirection(vehicleData[CONSTANT.HEADING]);
    vehicleData[CONSTANT.ICON] = this.mapDataObj.pedestrianUrl;
    vehicleData[CONSTANT.ICON_URL] = this.mapDataObj.pedestrianIcon;
    vehicleData[CONSTANT.LAT] = Number(vehicleData[CONSTANT.POSITION][CONSTANT.LAT] / 10000000);
    vehicleData[CONSTANT.LONG] = Number(vehicleData[CONSTANT.POSITION][CONSTANT.LONG] / 10000000);
    vehicleData[CONSTANT.SPEED] = Number(vehicleData[CONSTANT.SPEED]);
    vehicleData['uiSpeed'] = `${vehicleData[CONSTANT.SPEED]}m/s`;
    vehicleData[CONSTANT.UNIT] = 'm/s';
    vehicleData[CONSTANT.DIRECTION] = direction;
    vehicleData[CONSTANT.COLOR] = 'green';
    DashboardCalculation.dataTimeStampMap.set(vehicleData[CONSTANT.ID], this.rightPanelDataObj.currentDate.getTime());
    if (DashboardCalculation.allData.has(vehicleData[CONSTANT.ID])) {
      if (DashboardCalculation.circleMarker && DashboardCalculation.allData.has(DashboardCalculation.circleMarker.id) && vehicleData[CONSTANT.ID] == DashboardCalculation.circleMarker.id) {
        DashboardCalculation.vehicleCircleArr.lat = vehicleData[CONSTANT.LAT];
        DashboardCalculation.vehicleCircleArr.lng = vehicleData[CONSTANT.LONG];
        if (this.rightPanelDataObj.checkedListIndex != -1 && (this.rightPanelDataObj.listVal == 3 || this.rightPanelDataObj.listVal == 0)) {
          this.mapDataObj.latitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).lat;
          this.mapDataObj.longitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).long;
          DashboardCalculation.vehicleCircleArr.color = '#3CE711';
          vehicleData[CONSTANT.IS_OPEN] = true;
        }
      }
      if (DashboardCalculation.allData.get(vehicleData[CONSTANT.ID]).type == CONSTANT.BSM) {
        if (DashboardCalculation.dataCount.vehicle > 0) {
          DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle - 1;
        }
        DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian + 1;
      } else if (DashboardCalculation.allData.get(vehicleData[CONSTANT.ID]).type == CONSTANT.EVA) {
        if (DashboardCalculation.emergencyCardData.has(vehicleData[CONSTANT.ID])) {
          DashboardCalculation.emergencyCardData.delete(vehicleData[CONSTANT.ID]);
          DashboardCalculation.allData.get(vehicleData[CONSTANT.ID]).type = CONSTANT.PSM;
        }
        if (DashboardCalculation.dataCount.emergency > 0) {
          DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency - 1;
        }
        DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian + 1;
      }
      this.setVehicleInfo(1, vehicleData);
    } else {
      vehicleData[CONSTANT.IS_OPEN] = false;
      DashboardCalculation.allData.set(vehicleData[CONSTANT.ID], vehicleData);
      DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian + 1;
    }
    if (this.rightPanelDataObj.isDetailViewEnabled) {
      this.getPedChartData();
    }
  }

  setDecodedEVADataOnMap(vehicleData: any){
    let direction = DashboardCalculation.convertToDirection(vehicleData['rsaMsg'][CONSTANT.POSITION][CONSTANT.HEADING]);
    vehicleData[CONSTANT.DIRECTION] = direction;
    vehicleData[CONSTANT.ICON] = this.mapDataObj.emergencyVehicleUrl;
    vehicleData[CONSTANT.ICON_URL] = this.mapDataObj.evaIcon;
    vehicleData[CONSTANT.LAT] = Number(vehicleData['rsaMsg'][CONSTANT.POSITION][CONSTANT.LAT] / 10000000);
    vehicleData[CONSTANT.LONG] = Number(vehicleData['rsaMsg'][CONSTANT.POSITION][CONSTANT.LONG] / 10000000);
    vehicleData[CONSTANT.HEADING] = Number(vehicleData['rsaMsg'][CONSTANT.POSITION][CONSTANT.HEADING]);
    vehicleData[CONSTANT.SPEED] = Number((Number(vehicleData['rsaMsg'][CONSTANT.POSITION][CONSTANT.SPEED][CONSTANT.SPEED]) * 3.6 * 0.62).toFixed(2));
    vehicleData[CONSTANT.UNIT] = 'mph';
    vehicleData['uiSpeed'] = `${vehicleData[CONSTANT.SPEED]}mph`;
    vehicleData[CONSTANT.COLOR] = 'red';
    vehicleData['location'] = '';
    DashboardCalculation.dataTimeStampMap.set(vehicleData[CONSTANT.ID], this.rightPanelDataObj.currentDate.getTime());

    this.httpClient.get(`https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/reverseGeocode?f=pjson&featureTypes=&location=${vehicleData[CONSTANT.LONG]}%2C${vehicleData[CONSTANT.LAT]}`).subscribe((res: any) => {
      DashboardCalculation.allData.get(vehicleData[CONSTANT.ID]).location = res['address']['Match_addr'];
    })

    if (!DashboardCalculation.allData.has(vehicleData[CONSTANT.ID])) {
      vehicleData[CONSTANT.IS_OPEN] = false;
      DashboardCalculation.allData.set(vehicleData[CONSTANT.ID], vehicleData);
      DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency + 1;
    }
    else {
      if (DashboardCalculation.circleMarker && DashboardCalculation.allData.has(DashboardCalculation.circleMarker.id) && vehicleData[CONSTANT.ID] == DashboardCalculation.circleMarker.id) {
        DashboardCalculation.vehicleCircleArr.lat = vehicleData[CONSTANT.LAT];
        DashboardCalculation.vehicleCircleArr.lng = vehicleData[CONSTANT.LONG];
        if (this.rightPanelDataObj.checkedListIndex != -1 && (this.rightPanelDataObj.listVal == 2 || this.rightPanelDataObj.listVal == 0)) {
          this.mapDataObj.latitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).lat;
          this.mapDataObj.longitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).long;
          DashboardCalculation.vehicleCircleArr.color = '#FF0000';
          vehicleData[CONSTANT.IS_OPEN] = true;
        }
      }
      if (DashboardCalculation.allData.get(vehicleData[CONSTANT.ID]).type == CONSTANT.BSM) {
        DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle - 1;
        DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency + 1;
      } else if (DashboardCalculation.allData.get(vehicleData[CONSTANT.ID]).type == CONSTANT.PSM) {
        if (DashboardCalculation.pedestrianCardData.has(vehicleData[CONSTANT.ID])) {
          DashboardCalculation.pedestrianCardData.delete(vehicleData[CONSTANT.ID]);
          DashboardCalculation.allData.get(vehicleData[CONSTANT.ID]).type = CONSTANT.EVA;
        }
        DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency + 1;
        DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian - 1;
      }
      this.setVehicleInfo(1, vehicleData);
    }
    if (this.rightPanelDataObj.isDetailViewEnabled) {
      this.getPedChartData();
    }
  }

  getPedChartData() {
    let pedMovingCount = DashboardCalculation.getPedChartData();
    this.pedestrianChart.updateSeries([
      {
        data: [pedMovingCount.moving, pedMovingCount.notMoving]
      }
    ])
  }

  setAllDataInfo(vehicleDetails: any) {
    if (DashboardCalculation.allData.has(vehicleDetails[CONSTANT.ID])) {
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).icon = vehicleDetails[CONSTANT.ICON];
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).iconurl = vehicleDetails[CONSTANT.ICON_URL];
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).lat = vehicleDetails[CONSTANT.LAT];
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).long = vehicleDetails[CONSTANT.LONG];
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).speed = vehicleDetails[CONSTANT.SPEED];
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).heading = vehicleDetails[CONSTANT.HEADING];
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).type = vehicleDetails[CONSTANT.TYPE];
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).unit = vehicleDetails[CONSTANT.UNIT];
      if (vehicleDetails[CONSTANT.SUBTYPE]) {
        DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).subtype = vehicleDetails[CONSTANT.SUBTYPE];
      }
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).color = vehicleDetails[CONSTANT.COLOR];
      DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).unit = vehicleDetails[CONSTANT.UNIT];
      if (vehicleDetails['uiSpeed']) {
        DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).uiSpeed = vehicleDetails['uiSpeed'];
      }
    } else if (DashboardCalculation.allData.size == 0) {
      DashboardCalculation.allData.set(vehicleDetails[CONSTANT.ID], vehicleDetails);
    }
  }

  setVehicleInfo(index: number, vehicleDetails: any) {
    this.setAllDataInfo(vehicleDetails);
    this.rightPanelDataObj.selectedCar = vehicleDetails;
    if (DashboardCalculation.allData.get(vehicleDetails[CONSTANT.ID]).isOpen) {
      this.patch(this.rightPanelDataObj.selectedCar);
    }
  }

  selectLocationCar(index: number, isChecked: any, val: any) {
    this.resetVehicleId();
    DashboardCalculation.allData.get(val.id).isOpen = isChecked;
    this.rightPanelDataObj.selectedCar = val;
    if (isChecked) {
      this.mapDataObj.zoom = 10;
      this.rightPanelDataObj.checkedListIndex = index;
    } else {
      this.rightPanelDataObj.checkedListIndex = -1;
    }
    DashboardCalculation.circleMarker = val;
    if (val.type == CONSTANT.BSM) {
      DashboardCalculation.vehicleCircleArr.color = '#0081F9';
    } else if (val.type == CONSTANT.PSM) {
      DashboardCalculation.vehicleCircleArr.color = '#3CE711';
    }
    else if (val.type == CONSTANT.EVA) {
      DashboardCalculation.vehicleCircleArr.color = '#FF0000';
    }
    DashboardCalculation.vehicleCircleArr.lat = this.rightPanelDataObj.selectedCar.lat;
    DashboardCalculation.vehicleCircleArr.lng = this.rightPanelDataObj.selectedCar.long;
    this.patch(this.rightPanelDataObj.selectedCar);
    setTimeout(() => {
      this.carsLocation(this.rightPanelDataObj.selectedCar);
    }, 10);

    if (!isChecked) {
      DashboardCalculation.vehicleCircleArr.color = 'transparent';
    }
  }

  patch(info: any) {
    this.carDetailForm.patchValue({
      head_by: info.heading,
      latitude_car: info.lat,
      longitude_car: info.long,
      speed_car: info.speed,
      direction: info.direction,
      unit: info.unit,
      vin_car: info.id
    })
  }

  patchSpatData(info: any) {
    this.spatDetailForm.patchValue({
      id: info.id,
      name: info.name,
      type: info.type
    })
  }

  carsLocation(car: any) {
    this.mapDataObj.latitude = (car.lat);
    this.mapDataObj.longitude = (car.long);
    this.mapDataObj.zoom = 17;
  }

  changeMapMode(val: string) {
    this.mapDataObj.mapMode = val;
    this.rightPanelDataObj.isMapVisible = false;
  }

  showHideMapMode() {
    this.rightPanelDataObj.isMapVisible = !this.rightPanelDataObj.isMapVisible;
  }

  resetVehicleId() {
    DashboardCalculation.allData.forEach((x: any, i: any) => {
      DashboardCalculation.allData.set(i, { ...DashboardCalculation.allData.get(i), isOpen: false });
    })
  }

  ngOnDestroy() {
    clearInterval(DashboardCalculation.timeCheckInterval);
  }

  changeTab() {
    this.topicName = localStorage.getItem('topicName') || '';
    this.rightPanelDataObj.listVal = -1;
    DashboardCalculation.vehicleCircleArr.color = 'transparent';
    if (this.rightPanelDataObj.tabType == 'vehicle') {
      DashboardCalculation.allData.clear();
      this.rightPanelDataObj.tabType = 'details';
      this.mapDataObj.mapMode = 'Google Roadmap';
      this.webSocketAPI.updateTopic(this.topicName, "ALL", true);
    } else {
      DashboardCalculation.spatData.clear();
      this.rightPanelDataObj.tabType = 'vehicle';
      this.mapDataObj.mapMode = 'Google Hybrid';
      this.webSocketAPI.updateTopic(this.topicName, this.filterType, true);
    }
  }

  trackBy(item: any) {
    return item.key;
  }

  trackByDetails(item: any) {
    return item.key;
  }

  trackByMarker(item: any) {
    return item.key;
  }

  get staticAllData() {
    return DashboardCalculation.allData;
  }

  get staticPedestrianCardData() {
    return DashboardCalculation.pedestrianCardData;
  }

  get staticEmergencyCardData() {
    return DashboardCalculation.emergencyCardData;
  }

  get staticDataCount() {
    return DashboardCalculation.dataCount;
  }

  get staticTruckType() {
    return DashboardCalculation.truckType;
  }

  get staticPsgrType() {
    return DashboardCalculation.psgrType;
  }

  get staticEVType() {
    return DashboardCalculation.evType;
  }

  get staticVehicleCircleArr() {
    return DashboardCalculation.vehicleCircleArr;
  }

  get staticLeftSpatMenuOpen() {
    return DashboardCalculation.leftSpatMenuOpen;
  }

  get staticSPATData() {
    return DashboardCalculation.spatData;
  }

  get staticSpatDataMarker() {
    return DashboardCalculation.spatData;
  }

  openRightPanel(type: any) {
    this.mapDataObj.hideShowMap = false;
    setTimeout(() => {
      this.mapDataObj.hideShowMap = true;
    }, 0);
    if (type == 'vehicle') {
      this.rightPanelDataObj.tabType = 'vehicle';
    } else {
      this.rightPanelDataObj.tabType = 'details';
    }
    this.rightPanelDataObj.isRightPanelOpen = !this.rightPanelDataObj.isRightPanelOpen;
    if (this.rightPanelDataObj.isRightPanelOpen) {
      this.dashBoardService.setHeaderOpacity(true);
    } else {
      this.dashBoardService.setHeaderOpacity(false);
    }
  }

  decodedSPATValueReceived(vehicleData: any) {
    let dataObj: any = {};
    if (vehicleData && vehicleData[CONSTANT.TYPE] == CONSTANT.SPAT && !vehicleData[CONSTANT.MESSAGE_FRAME]) {
      dataObj[CONSTANT.NAME] = vehicleData[CONSTANT.INTERSECTIONS][0][CONSTANT.NAME];
      dataObj[CONSTANT.ID] = vehicleData[CONSTANT.INTERSECTIONS][0][CONSTANT.ID][CONSTANT.ID];
      dataObj['spatData'] = vehicleData[CONSTANT.INTERSECTIONS][0][CONSTANT.STATES][1]['state-time-speed'][0];
      if (DashboardCalculation.mapData.get(dataObj[CONSTANT.ID])) {
        console.log("got the id in 1st method");
        dataObj[CONSTANT.ICON] = this.mapDataObj.spatStationURL;
        dataObj[CONSTANT.ID] = DashboardCalculation.mapData.get(dataObj[CONSTANT.ID]).id;
        dataObj[CONSTANT.LAT] = DashboardCalculation.mapData.get(dataObj[CONSTANT.ID]).lat;
        dataObj[CONSTANT.LONG] = DashboardCalculation.mapData.get(dataObj[CONSTANT.ID]).long;
        dataObj[CONSTANT.NAME] = DashboardCalculation.mapData.get(dataObj[CONSTANT.ID]).name;
        dataObj[CONSTANT.TYPE] = this.signalType[dataObj['spatData']['eventState']];
        if (DashboardCalculation.leftSpatMenuOpen && DashboardCalculation.mapData.get(DashboardCalculation.selectedSpatId).id == dataObj[CONSTANT.ID]) {
          dataObj[CONSTANT.IS_OPEN] = DashboardCalculation.spatData.get(dataObj[CONSTANT.ID]).isOpen;
          this.patchSpatData(dataObj);
        } else {
          dataObj[CONSTANT.IS_OPEN] = false;
        }
        DashboardCalculation.spatDataTimeStampMap.set(dataObj[CONSTANT.ID], this.rightPanelDataObj.currentDate.getTime());
        DashboardCalculation.spatData.set(dataObj[CONSTANT.ID], dataObj);
      }
    }
    else if (vehicleData && vehicleData[CONSTANT.TYPE] == "MAP" && !vehicleData[CONSTANT.MESSAGE_FRAME]) {
      dataObj[CONSTANT.NAME] = vehicleData[CONSTANT.INTERSECTIONS][0][CONSTANT.NAME];
      dataObj[CONSTANT.ID] = vehicleData[CONSTANT.INTERSECTIONS][0][CONSTANT.ID][CONSTANT.ID];
      dataObj[CONSTANT.LAT] = Number(vehicleData[CONSTANT.INTERSECTIONS][0]['refPoint'][CONSTANT.LAT] / 10000000);
      dataObj[CONSTANT.LONG] = (vehicleData[CONSTANT.INTERSECTIONS][0]['refPoint'][CONSTANT.LONG] / 10000000);
      DashboardCalculation.mapData.set(dataObj[CONSTANT.ID], dataObj);
    }
  }

  encodedSPATValueReceived(vehicleData: any) {
    let dataObj: any = {};
    if (vehicleData && vehicleData[CONSTANT.TYPE] == CONSTANT.SPAT && vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.SPAT]) {
      if(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.SPAT][CONSTANT.INTERSECTIONS]['IntersectionState'].length){
        dataObj[CONSTANT.ID] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.SPAT][CONSTANT.INTERSECTIONS]['IntersectionState'][0][CONSTANT.ID][CONSTANT.ID];
        dataObj['spatData'] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.SPAT][CONSTANT.INTERSECTIONS]['IntersectionState'][0][CONSTANT.STATES]['MovementState'][1]['state-time-speed']['MovementEvent'];
      }else{
        dataObj[CONSTANT.ID] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.SPAT][CONSTANT.INTERSECTIONS]['IntersectionState'][CONSTANT.ID][CONSTANT.ID];
        dataObj['spatData'] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.SPAT][CONSTANT.INTERSECTIONS]['IntersectionState'][CONSTANT.STATES]['MovementState'][1]['state-time-speed']['MovementEvent'];
      }
      if (DashboardCalculation.mapData.get(dataObj[CONSTANT.ID])) {
        dataObj[CONSTANT.ICON] = this.mapDataObj.spatStationURL;
        dataObj[CONSTANT.ID] = DashboardCalculation.mapData.get(dataObj[CONSTANT.ID]).id;
        dataObj[CONSTANT.LAT] = DashboardCalculation.mapData.get(dataObj[CONSTANT.ID]).lat;
        dataObj[CONSTANT.LONG] = DashboardCalculation.mapData.get(dataObj[CONSTANT.ID]).long;
        dataObj[CONSTANT.NAME] = DashboardCalculation.mapData.get(dataObj[CONSTANT.ID]).name;
        let signalStateKey = Object.keys(dataObj['spatData']['eventState']);
        dataObj[CONSTANT.TYPE] = this.signalType[signalStateKey[0]];
        if (DashboardCalculation.leftSpatMenuOpen && DashboardCalculation.mapData.get(DashboardCalculation.selectedSpatId).id == dataObj[CONSTANT.ID]) {
          dataObj[CONSTANT.IS_OPEN] = DashboardCalculation.spatData.get(dataObj[CONSTANT.ID]).isOpen;
          this.patchSpatData(dataObj);
        } else {
          dataObj[CONSTANT.IS_OPEN] = false;
        }
        DashboardCalculation.spatDataTimeStampMap.set(dataObj[CONSTANT.ID], this.rightPanelDataObj.currentDate.getTime());
        DashboardCalculation.spatData.set(dataObj[CONSTANT.ID], dataObj);
      }
    }
    else if (vehicleData && vehicleData[CONSTANT.TYPE] == CONSTANT.MAP && vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.MAP]) {
      dataObj[CONSTANT.NAME] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.MAP][CONSTANT.INTERSECTIONS]['IntersectionGeometry'][CONSTANT.NAME];
      dataObj[CONSTANT.ID] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.MAP][CONSTANT.INTERSECTIONS]['IntersectionGeometry'][CONSTANT.ID][CONSTANT.ID];
      dataObj[CONSTANT.LAT] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.MAP][CONSTANT.INTERSECTIONS]['IntersectionGeometry']['refPoint'][CONSTANT.LAT] / 10000000);
      dataObj[CONSTANT.LONG] = (vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.MAP][CONSTANT.INTERSECTIONS]['IntersectionGeometry']['refPoint'][CONSTANT.LONG] / 10000000);
      DashboardCalculation.mapData.set(dataObj[CONSTANT.ID], dataObj);
    }
  }

  selectSpatLocationCar(index: number, isChecked: any, val: any) {
    this.resetSpatId();
    if (isChecked) {
      this.mapDataObj.zoom = 10;
      this.mapDataObj.latitude = 0;
      this.mapDataObj.longitude = 0;
      DashboardCalculation.selectedSpatId = val.id;
      DashboardCalculation.leftSpatMenuOpen = true;
      this.patchSpatData(val);
      DashboardCalculation.vehicleCircleArr.color = 'green';
    } else {
      DashboardCalculation.selectedSpatId = null;
      DashboardCalculation.leftSpatMenuOpen = false;
      DashboardCalculation.vehicleCircleArr.color = 'transparent';
    }
    DashboardCalculation.circleMarker = val;
    DashboardCalculation.spatData.get(val.id)['isOpen'] = isChecked;
    DashboardCalculation.vehicleCircleArr.lat = val.lat;
    DashboardCalculation.vehicleCircleArr.lng = val.long;
    setTimeout(() => {
      this.mapDataObj.zoom = 17;
      this.mapDataObj.latitude = val.lat;
      this.mapDataObj.longitude = val.long;
    }, 100);
  }

  resetSpatId() {
    DashboardCalculation.spatData.forEach((x: any, i: any) => {
      DashboardCalculation.spatData.set(i, { ...DashboardCalculation.spatData.get(i), isOpen: false });
    })
  }

  enableDetailedView() {
    this.rightPanelDataObj.isDetailViewEnabled = !this.rightPanelDataObj.isDetailViewEnabled;
    if (this.rightPanelDataObj.isDetailViewEnabled) {
      setTimeout(() => {
        this.drawCharts();
      }, 0);
    }
  }

  enableDProccessedDataView() {
    this.rightPanelDataObj.isProccessedDataEnabled = !this.rightPanelDataObj.isProccessedDataEnabled;
  }

  changeProccessedVal(val: any) {
    this.rightPanelDataObj.isProccessedDataEnabled = val;
  }

  setBSMDataOnMap(vehicleData: any){
      let direction = DashboardCalculation.convertToDirection(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.HEADING]);
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ICON] = this.mapDataObj.vehicleUrl;
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ICON_URL] = this.mapDataObj.vehicleIcon;
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.LAT] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.LAT]) / 10000000;
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.LONG] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.LONG]) / 10000000;
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.SPEED] = Number((Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.SPEED]) * 3.6 * 0.62).toFixed(2));
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.UNIT] = 'mph';
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.DIRECTION] = direction;
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.COLOR] = 'black';
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.TYPE] = CONSTANT.BSM;
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.SUBTYPE] = vehicleData[CONSTANT.SUBTYPE];
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID].replaceAll(' ', '');
      DashboardCalculation.dataTimeStampMap.set(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID], this.rightPanelDataObj.currentDate.getTime());
      if (!DashboardCalculation.allData.has(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID])) {
        vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.IS_OPEN] = false;
        DashboardCalculation.allData.set(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID], vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA]);
        DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle + 1;
      }
      else {
        if (DashboardCalculation.circleMarker && DashboardCalculation.allData.has(DashboardCalculation.circleMarker.id) && vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID] == DashboardCalculation.circleMarker.id) {
          DashboardCalculation.vehicleCircleArr.lat = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.LAT];
          DashboardCalculation.vehicleCircleArr.lng = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.LONG];
          if (this.rightPanelDataObj.checkedListIndex != -1 && (this.rightPanelDataObj.listVal == 1 || this.rightPanelDataObj.listVal == 0)) {
            this.mapDataObj.latitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).lat;
            this.mapDataObj.longitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).long;
            vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.IS_OPEN] = true;
            DashboardCalculation.vehicleCircleArr.color = '#0081F9';
          }
        }
        if (DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID]).type == CONSTANT.PSM) {
          if (DashboardCalculation.dataCount.pedestrian > 0) {
            DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian - 1;
          }
          if (DashboardCalculation.pedestrianCardData.has(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID])) {
            DashboardCalculation.pedestrianCardData.delete(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID]);
            DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID]).type = CONSTANT.BSM;
          }
          DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle + 1;

        } else if (DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID]).type == CONSTANT.EVA) {
          if (DashboardCalculation.emergencyCardData.has(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID])) {
            DashboardCalculation.emergencyCardData.delete(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID]);
            DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA][CONSTANT.ID]).type = CONSTANT.BSM;
          }
          if (DashboardCalculation.dataCount.emergency > 0) {
            DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency - 1;
          }
          DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle + 1;
        }
        this.setVehicleInfo(1, vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.BASIC_SAFETY_MESSAGE][CONSTANT.COREDATA]);
      }
      if (this.rightPanelDataObj.isDetailViewEnabled) {
        this.getPedChartData();
      }
  }

  setPSMDataOnMap(vehicleData: any){
    let direction = DashboardCalculation.convertToDirection(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.HEADING]);
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ICON] = this.mapDataObj.pedestrianUrl;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ICON_URL] = this.mapDataObj.pedestrianIcon;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.LAT] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.POSITION][CONSTANT.LAT] / 10000000);
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.LONG] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.POSITION][CONSTANT.LONG] / 10000000);
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.SPEED] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.SPEED]);
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE]['uiSpeed'] = `${vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.SPEED]}m/s`;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.UNIT] = 'm/s';
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.DIRECTION] = direction;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.COLOR] = 'green';
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.TYPE] = CONSTANT.PSM;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID].replaceAll(' ', '');
    DashboardCalculation.dataTimeStampMap.set(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID], this.rightPanelDataObj.currentDate.getTime());
    if (DashboardCalculation.allData.has(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID])) {
      if (DashboardCalculation.circleMarker && DashboardCalculation.allData.has(DashboardCalculation.circleMarker.id) && vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID] == DashboardCalculation.circleMarker.id) {
        DashboardCalculation.vehicleCircleArr.lat = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.LAT];
        DashboardCalculation.vehicleCircleArr.lng = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.LONG];
        if (this.rightPanelDataObj.checkedListIndex != -1 && (this.rightPanelDataObj.listVal == 3 || this.rightPanelDataObj.listVal == 0)) {
          this.mapDataObj.latitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).lat;
          this.mapDataObj.longitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).long;
          vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.IS_OPEN] = true;
          DashboardCalculation.vehicleCircleArr.color = '#3CE711';
        }
      }
      if (DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID]).type == CONSTANT.BSM) {
        if (DashboardCalculation.dataCount.vehicle > 0) {
          DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle - 1;
        }
        DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian + 1;
      } else if (DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID]).type == CONSTANT.EVA) {
        if (DashboardCalculation.emergencyCardData.has(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID])) {
          DashboardCalculation.emergencyCardData.delete(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID]);
          DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID]).type = CONSTANT.PSM;
        }
        if (DashboardCalculation.dataCount.emergency > 0) {
          DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency - 1;
        }
        DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian + 1;
      }
      this.setVehicleInfo(1, vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE]);
    } else {
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.IS_OPEN] = false;
      DashboardCalculation.allData.set(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE][CONSTANT.ID], vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.PERSONAL_SAFETY_MESSAGE]);
      DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian + 1;
    }
    if (this.rightPanelDataObj.isDetailViewEnabled) {
      this.getPedChartData();
    }
  }

  setEVADataOnMap(vehicleData: any){
    let direction = DashboardCalculation.convertToDirection(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]['rsaMsg'][CONSTANT.POSITION][CONSTANT.HEADING]);
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.DIRECTION] = direction;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ICON] = this.mapDataObj.emergencyVehicleUrl;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ICON_URL] = this.mapDataObj.evaIcon;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.LAT] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]['rsaMsg'][CONSTANT.POSITION][CONSTANT.LAT] / 10000000);
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.LONG] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]['rsaMsg'][CONSTANT.POSITION][CONSTANT.LONG] / 10000000);
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.HEADING] = Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]['rsaMsg'][CONSTANT.POSITION][CONSTANT.HEADING]);
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.SPEED] = Number((Number(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]['rsaMsg'][CONSTANT.POSITION][CONSTANT.SPEED][CONSTANT.SPEED]) * 3.6 * 0.62).toFixed(2));
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]['uiSpeed'] = `${vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.SPEED]}mph`;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.UNIT] = 'mph';
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.COLOR] = 'red';
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.TYPE] = CONSTANT.EVA;
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID] = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID].replaceAll(' ', '');
    vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]['location'] = '';
    DashboardCalculation.dataTimeStampMap.set(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID], this.rightPanelDataObj.currentDate.getTime());

    this.httpClient.get(`https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/reverseGeocode?f=pjson&featureTypes=&location=${vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.LONG]}%2C${vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.LAT]}`).subscribe((res: any) => {
      DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID]).location = res['address']['Match_addr'];
    })

    if (!DashboardCalculation.allData.has(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID])) {
      vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.IS_OPEN] = false;
      DashboardCalculation.allData.set(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID], vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]);
      DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency + 1;
    }
    else {
      if (DashboardCalculation.circleMarker && DashboardCalculation.allData.has(DashboardCalculation.circleMarker.id) && vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID] == DashboardCalculation.circleMarker.id) {
        DashboardCalculation.vehicleCircleArr.lat = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.LAT];
        DashboardCalculation.vehicleCircleArr.lng = vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.LONG];
        if (this.rightPanelDataObj.checkedListIndex != -1 && (this.rightPanelDataObj.listVal == 2 || this.rightPanelDataObj.listVal == 0)) {
          this.mapDataObj.latitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).lat;
          this.mapDataObj.longitude = DashboardCalculation.allData.get(DashboardCalculation.circleMarker.id).long;
          vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.IS_OPEN] = true;
          DashboardCalculation.vehicleCircleArr.color = '#FF0000';
        }
      }
      if (DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID]).type == CONSTANT.BSM) {
        DashboardCalculation.dataCount.vehicle = DashboardCalculation.dataCount.vehicle - 1;
        DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency + 1;
      } else if (DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID]).type == CONSTANT.PSM) {
        if (DashboardCalculation.pedestrianCardData.has(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID])) {
          DashboardCalculation.pedestrianCardData.delete(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID]);
          DashboardCalculation.allData.get(vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT][CONSTANT.ID]).type = CONSTANT.EVA;
        }
        DashboardCalculation.dataCount.emergency = DashboardCalculation.dataCount.emergency + 1;
        DashboardCalculation.dataCount.pedestrian = DashboardCalculation.dataCount.pedestrian - 1;
      }
      this.setVehicleInfo(1, vehicleData[CONSTANT.MESSAGE_FRAME][CONSTANT.VALUE][CONSTANT.EMERGENCY_VERHICLE_ALERT]);
    }
    if (this.rightPanelDataObj.isDetailViewEnabled) {
      this.getPedChartData();
    }
  }
}
