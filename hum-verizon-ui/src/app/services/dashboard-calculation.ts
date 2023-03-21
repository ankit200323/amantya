import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class DashboardCalculation {

    static vehicleCircleArr = { lat: 0, lng: 0, color: '#3CE711' }
    static directionArr: any = ["North", "North East", "East", "South East", "South", "South West", "West", "North West"]
    static allData = new Map();
    static circleMarker: any;
    static dataCount: any = {
      vehicle: 0,
      pedestrian: 0,
      emergency: 0
    }
    static pedestrianCardData = new Map();
    static emergencyCardData = new Map();
    static dataTimeStampMap = new Map();
    static timeCheckInterval: any;
    static truckType: number = 0;
    static psgrType: number = 0;
    static evType: number = 0;
    static spatData = new Map();
    static mapData = new Map();
    static leftSpatMenuOpen: boolean = false;
    static selectedSpatId:any = null;
    static spatDataTimeStampMap = new Map();

  static getPedChartData(){
    let pedestrianMovingCountObj = {
        moving: 0,
        notMoving: 0
    }
    DashboardCalculation.psgrType = 0;
    DashboardCalculation.truckType = 0;
    DashboardCalculation.evType = 0;
    DashboardCalculation.allData.forEach((x: any, i: any) => {
      if (x.type == 'PSM') {
        if (x.speed == 0) {
            pedestrianMovingCountObj.moving = pedestrianMovingCountObj.moving + 1;
        }else{
            pedestrianMovingCountObj.notMoving = pedestrianMovingCountObj.notMoving + 1;
        }
      }else if(x.type == 'BSM'){
        if(x['subtype'] == 'PSGR'){
          DashboardCalculation.psgrType = DashboardCalculation.psgrType + 1;
        }else if(x['subtype'] == 'TRUCK'){
          DashboardCalculation.truckType = DashboardCalculation.truckType + 1;
        }else if(x['subtype'] == 'EV'){
          DashboardCalculation.evType = DashboardCalculation.evType + 1;
        }
      }
    })
    return pedestrianMovingCountObj;
  }

    static convertToDirection(degree: any) {
    let val = Math.floor((degree / 45) + 0.5);
    return DashboardCalculation.directionArr[(val % 8)];
  }
}
