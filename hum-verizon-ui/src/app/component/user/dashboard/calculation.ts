export class Calculation {
  static directionArr: any = ["North", "North East", "East", "South East", "South", "South West", "West", "North West"]
      static checkConnectionStability(dataObj: any){
        let currentTimeStamp = 0;
        const timestamp = Math.max(Math.round(Date.now() / 1000), currentTimeStamp + 1);
        currentTimeStamp = timestamp;
        dataObj.dataTimeStampMap.forEach((x: any, i: number) => {
          if ((currentTimeStamp - x) >= 8) {
            if(dataObj.circleMarker && i == dataObj.circleMarker.id){
              dataObj.vehicleCircleArr.color = 'transparent';
            }
            if(dataObj.allData.get(i)?.type == 'BSM'){
              dataObj.dataCount.vehicle = dataObj.dataCount.vehicle - 1;
              if(dataObj.allData.get(i)['subtype'] == 'PSGR'){
                dataObj.psgrType = dataObj.psgrType - 1;
              }else if(dataObj.allData.get(i)['subtype'] == 'TRUCK'){
                dataObj.truckType = dataObj.truckType - 1;
              }else if(dataObj.allData.get(i)['subtype'] == 'EV'){
                dataObj.evType = dataObj.evType - 1;
              }
            }else if(dataObj.allData.get(i)?.type == 'PSM'){
              dataObj.dataCount.pedestrian = dataObj.dataCount.pedestrian - 1;
            }else if(dataObj.allData.get(i)?.type == 'EVA'){
              dataObj.dataCount.emergency = dataObj.dataCount.emergency - 1;
            }
            dataObj.pedestrianCardData.delete(i);
            dataObj.emergencyCardData.delete(i);
            dataObj.allData.delete(i);
            dataObj.dataTimeStampMap.delete(i);
          } else {
            dataObj.dataTimeStampMap.set(i, currentTimeStamp);
          }
        })
        return dataObj;
      }

      static convertToDirection(degree: any) {
        let val = Math.floor((degree / 45) + 0.5);
        return Calculation.directionArr[(val % 8)];
      }
}