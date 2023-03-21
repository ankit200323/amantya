import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { NgApexchartsModule } from 'ng-apexcharts';
import { CUSTOM_ELEMENTS_SCHEMA, DebugElement } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { DashboardCalculation } from 'src/app/services/dashboard-calculation';
import { WebSocketShareService } from 'src/app/services/web-socket-share.service';
import { of } from 'rxjs';

describe('DashboardComponent', () => {
  let bsmDemoData = {"MessageFrame":{"messageId":"20","value":{"BasicSafetyMessage":{"coreData":{"elev":"-4096","accuracy":{"semiMajor":"255","semiMinor":"255","orientation":"65535"},"long":"-1345110400","speed":"8000","brakes":{"wheelBrakes":"00000","traction":{"unavailable":""},"abs":{"unavailable":""},"scs":{"unavailable":""},"brakeBoost":{"unavailable":""},"auxBrakes":{"unavailable":""}},"size":{"width":"1000","length":"4000"},"msgCnt":"120","secMark":"2415","lat":"582813000","accelSet":{"lat":"2001","vert":"-127","yaw":"0","long":"2001"},"id":"00 00 D8 7F","transmission":{"unavailable":""},"heading":"28000","angle":"127"}}}},"type":"BSM","subtype":"BSM","topicType":"JER"};
  let psmDemoData = {"MessageFrame":{"messageId":"32","value":{"PersonalSafetyMessage":{"speed":"8000","heading":"20000","basicType":{"aPEDESTRIAN":""},"secMark":"20000","msgCnt":"100","id":"00 00 D8 7F","position":{"lat":"582813000","long":"-1345110400"},"accuracy":{"semiMajor":"255","semiMinor":"255","orientation":"65535"}}}},"type":"PSM","subtype":"PSM","topicType":"JER"};
  let evaDemoData = {"MessageFrame":{"messageId":"22","value":{"EmergencyVehicleAlert":{"id":"0000073B","rsaMsg":{"msgCnt":"0","typeEvent":"9729","position":{"long":"770615581","lat":"283872144","elevation":"0","heading":"0","speed":{"transmisson":{"unavailable":""},"speed":"0"}}}}}},"type":"EVA","subtype":"EVA","topicType":"JER"};
  let decodedEVAData = {"id":"0000073B","rsaMsg":{"msgCnt":0,"typeEvent":9729,"position":{"long":770615628,"lat":283872122,"elevation":0,"heading":260,"speed":{"transmisson":"unavailable","speed":0}}},"type":"EVA"};
  let decodedPSMData = {"basicType":"aPEDESTRIAN","secMark":20000,"msgCnt":100,"id":"0000D87F","position":{"lat":582813000,"long":-1345110400},"accuracy":{"semiMajor":255,"semiMinor":255,"orientation":65535},"speed":8000,"heading":20000,"type":"PSM"};
  let decodedBSMData = {"coreData":{"msgCnt":120,"id":"0000D87F","secMark":2415,"lat":582813000,"long":-1345110400,"elev":-4096,"accuracy":{"semiMajor":255,"semiMinor":255,"orientation":65535},"transmission":"unavailable","speed":8000,"heading":28000,"angle":127,"accelSet":{"long":2001,"lat":2001,"vert":-127,"yaw":0},"brakes":{"wheelBrakes":"00","traction":"unavailable","abs":"unavailable","scs":"unavailable","brakeBoost":"unavailable","auxBrakes":"unavailable"},"size":{"width":1000,"length":4000}},"type":"BSM","subtype":"BSM"};
  let spatDemoData = {"MessageFrame":{"value":{"SPAT":{"timeStamp":"123456","name":"M-City Intersections","intersections":{"IntersectionState":[{"id":{"id":"123"},"revision":"0","status":"1010000010110000","moy":"527040","timeStamp":"5000","states":{"MovementState":[{"signalGroup":"1","state-time-speed":{"MovementEvent":{"eventState":{"permissive-clearance":""},"timing":{"minEndTime":"12345","maxEndTime":"12500"}}}},{"signalGroup":"2","state-time-speed":{"MovementEvent":{"eventState":{"stop-And-Remain":""},"timing":{"minEndTime":"12345","maxEndTime":"12500"}}}}]}},{"id":{"id":"123"},"revision":"0","status":"1010000010110000","moy":"527040","timeStamp":"5000","states":{"MovementState":[{"signalGroup":"1","state-time-speed":{"MovementEvent":{"eventState":{"permissive-clearance":""},"timing":{"minEndTime":"12345","maxEndTime":"12500"}}}},{"signalGroup":"2","state-time-speed":{"MovementEvent":{"eventState":{"stop-And-Remain":""},"timing":{"minEndTime":"12345","maxEndTime":"12500"}}}}]}}]}}},"messageId":"19"},"type":"SPAT"};
  let decodedSpatData = {"timeStamp":123456,"name":"M-City Intersections","intersections":[{"id":{"id":123},"revision":0,"status":"A0B0","moy":527040,"timeStamp":5000,"states":[{"signalGroup":1,"state-time-speed":[{"eventState":"permissive-clearance","timing":{"minEndTime":12345,"maxEndTime":12500}}]},{"signalGroup":2,"state-time-speed":[{"eventState":"stop-And-Remain","timing":{"minEndTime":12345,"maxEndTime":12500}}]}]},{"id":{"id":123},"revision":0,"status":"A0B0","moy":527040,"timeStamp":5000,"states":[{"signalGroup":1,"state-time-speed":[{"eventState":"permissive-clearance","timing":{"minEndTime":12345,"maxEndTime":12500}}]},{"signalGroup":2,"state-time-speed":[{"eventState":"stop-And-Remain","timing":{"minEndTime":12345,"maxEndTime":12500}}]}]}],"type":"SPAT"};
  let decodedMapData = {"timeStamp":453417,"msgIssueRevision":1,"layerType":"intersectionData","layerID":1,"intersections":[{"id":{"id":30297},"revision":1,"refPoint":{"lat":304107858,"long":-843050520,"elevation":140},"laneWidth":366,"laneSet":[{"laneID":1,"ingressApproach":1,"laneAttributes":{"directionalUse":"80","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843050895,"lat":304106661}}},{"delta":{"node-LatLon":{"lon":-843051251,"lat":304105886}}},{"delta":{"node-LatLon":{"lon":-843052625,"lat":304103121}}},{"delta":{"node-LatLon":{"lon":-843054932,"lat":304098079}}},{"delta":{"node-LatLon":{"lon":-843056836,"lat":304093799}}}]},"connectsTo":[{"connectingLane":{"lane":2,"maneuver":"2400"},"signalGroup":6},{"connectingLane":{"lane":5,"maneuver":"8000"},"signalGroup":6},{"connectingLane":{"lane":7,"maneuver":"4000"},"signalGroup":6}]},{"laneID":2,"ingressApproach":2,"laneAttributes":{"directionalUse":"80","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843049373,"lat":304107204}}},{"delta":{"node-LatLon":{"lon":-843048508,"lat":304106903}}},{"delta":{"node-LatLon":{"lon":-843046671,"lat":304106348}}},{"delta":{"node-LatLon":{"lon":-843045035,"lat":304105805}}},{"delta":{"node-LatLon":{"lon":-843045035,"lat":304105805}}},{"delta":{"node-LatLon":{"lon":-843044914,"lat":304105753}}}]}},{"laneID":3,"ingressApproach":3,"laneAttributes":{"directionalUse":"80","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843049212,"lat":304107487}}},{"delta":{"node-LatLon":{"lon":-843048428,"lat":304107216}}},{"delta":{"node-LatLon":{"lon":-843046597,"lat":304106632}}},{"delta":{"node-LatLon":{"lon":-843045001,"lat":304106094}}}]},"connectsTo":[{"connectingLane":{"lane":10,"maneuver":"4000"},"signalGroup":7}]},{"laneID":4,"ingressApproach":3,"laneAttributes":{"directionalUse":"80","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843049098,"lat":304107794}}},{"delta":{"node-LatLon":{"lon":-843048253,"lat":304107528}}},{"delta":{"node-LatLon":{"lon":-843046523,"lat":304106927}}},{"delta":{"node-LatLon":{"lon":-843044880,"lat":304106366}}}]},"connectsTo":[{"connectingLane":{"lane":7,"maneuver":"8000"},"signalGroup":4},{"connectingLane":{"lane":5,"maneuver":"2400"},"signalGroup":4}]},{"laneID":7,"egressApproach":6,"laneAttributes":{"directionalUse":"40","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843051646,"lat":304108664}}},{"delta":{"node-LatLon":{"lon":-843052096,"lat":304108849}}},{"delta":{"node-LatLon":{"lon":-843052478,"lat":304108971}}},{"delta":{"node-LatLon":{"lon":-843053068,"lat":304108374}}},{"delta":{"node-LatLon":{"lon":-843054201,"lat":304107119}}},{"delta":{"node-LatLon":{"lon":-843056334,"lat":304104435}}},{"delta":{"node-LatLon":{"lon":-843059378,"lat":304101041}}},{"delta":{"node-LatLon":{"lon":-843064850,"lat":304098733}}},{"delta":{"node-LatLon":{"lon":-843065520,"lat":304098282}}},{"delta":{"node-LatLon":{"lon":-843066606,"lat":304096229}}}]}},{"laneID":10,"egressApproach":8,"laneAttributes":{"directionalUse":"40","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843051358,"lat":304106809}}},{"delta":{"node-LatLon":{"lon":-843051693,"lat":304106034}}},{"delta":{"node-LatLon":{"lon":-843052974,"lat":304103236}}},{"delta":{"node-LatLon":{"lon":-843055442,"lat":304098251}}},{"delta":{"node-LatLon":{"lon":-843057386,"lat":304094133}}}]}},{"laneID":5,"egressApproach":4,"laneAttributes":{"directionalUse":"40","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843049749,"lat":304109056}}},{"delta":{"node-LatLon":{"lon":-843049011,"lat":304110571}}},{"delta":{"node-LatLon":{"lon":-843048126,"lat":304112399}}},{"delta":{"node-LatLon":{"lon":-843047321,"lat":304114076}}}]}},{"laneID":6,"ingressApproach":5,"laneAttributes":{"directionalUse":"80","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843050171,"lat":304109195}}},{"delta":{"node-LatLon":{"lon":-843049413,"lat":304110704}}},{"delta":{"node-LatLon":{"lon":-843048548,"lat":304112543}}},{"delta":{"node-LatLon":{"lon":-843047730,"lat":304114209}}}]},"connectsTo":[{"connectingLane":{"lane":10,"maneuver":"8000"},"signalGroup":2},{"connectingLane":{"lane":7,"maneuver":"2400"},"signalGroup":2},{"connectingLane":{"lane":2,"maneuver":"4000"},"signalGroup":2}]},{"laneID":8,"ingressApproach":7,"laneAttributes":{"directionalUse":"80","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843051774,"lat":304108351}}},{"delta":{"node-LatLon":{"lon":-843052203,"lat":304108547}}},{"delta":{"node-LatLon":{"lon":-843052565,"lat":304108715}}},{"delta":{"node-LatLon":{"lon":-843052981,"lat":304108299}}},{"delta":{"node-LatLon":{"lon":-843054121,"lat":304107038}}},{"delta":{"node-LatLon":{"lon":-843056226,"lat":304104366}}},{"delta":{"node-LatLon":{"lon":-843059311,"lat":304100948}}},{"delta":{"node-LatLon":{"lon":-843064776,"lat":304098647}}},{"delta":{"node-LatLon":{"lon":-843065413,"lat":304098207}}},{"delta":{"node-LatLon":{"lon":-843066479,"lat":304096183}}}]},"connectsTo":[{"connectingLane":{"lane":5,"maneuver":"4000"},"signalGroup":3}]},{"laneID":9,"ingressApproach":7,"laneAttributes":{"directionalUse":"80","sharedWith":"0000","laneType":{"vehicle":{"value":"00","length":8}}},"nodeList":{"nodes":[{"delta":{"node-LatLon":{"lon":-843051911,"lat":304108074}}},{"delta":{"node-LatLon":{"lon":-843052387,"lat":304108242}}},{"delta":{"node-LatLon":{"lon":-843052676,"lat":304108352}}},{"delta":{"node-LatLon":{"lon":-843052991,"lat":304108080}}},{"delta":{"node-LatLon":{"lon":-843054003,"lat":304106912}}},{"delta":{"node-LatLon":{"lon":-843056116,"lat":304104252}}},{"delta":{"node-LatLon":{"lon":-843059220,"lat":304100794}}},{"delta":{"node-LatLon":{"lon":-843064679,"lat":304098521}}},{"delta":{"node-LatLon":{"lon":-843065228,"lat":304098093}}},{"delta":{"node-LatLon":{"lon":-843066194,"lat":304096121}}}]},"connectsTo":[{"connectingLane":{"lane":2,"maneuver":"8000"},"signalGroup":8},{"connectingLane":{"lane":10,"maneuver":"2400"},"signalGroup":8}]}]}],"type":"MAP"};
  let mapDemoData = {"MessageFrame":{"messageId":"18","value":{"MapData":{"timeStamp":"64953","msgIssueRevision":"2","intersections":{"IntersectionGeometry":{"laneWidth":"287","speedLimits":{"RegulatorySpeedLimit":{"type":{"vehicleMaxSpeed":""},"speed":"347"}},"laneSet":{"GenericLane":[{"connectsTo":{"Connection":{"signalGroup":"8","connectingLane":{"lane":"0","maneuver":"001000000000"}}},"laneID":"1","ingressApproach":"1","laneAttributes":{"directionalUse":"10","sharedWith":"0000000000","laneType":{"vehicle":"00000000"}},"maneuvers":"001000000000","nodeList":{"nodes":{"NodeXY":[{"delta":{"node-XY3":{"y":"-741","x":"-1504"}},"attributes":{"dWidth":"63"}},{"delta":{"node-XY4":{"x":"-3810","y":"-2741"}}},{"delta":{"node-XY4":{"x":"-3073","y":"-1825"}}}]}}},{"maneuvers":"001000000000","nodeList":{"nodes":{"NodeXY":[{"delta":{"node-XY3":{"y":"-1018","x":"-1295"}},"attributes":{"dWidth":"-7"}},{"delta":{"node-XY4":{"x":"-3842","y":"-2726"}}},{"delta":{"node-XY4":{"x":"-3091","y":"-1841"}}}]}},"connectsTo":{"Connection":{"signalGroup":"8","connectingLane":{"lane":"0","maneuver":"001000000000"}}},"laneID":"2","ingressApproach":"1","laneAttributes":{"directionalUse":"10","sharedWith":"0000000000","laneType":{"vehicle":"00000000"}}},{"ingressApproach":"2","laneAttributes":{"directionalUse":"10","sharedWith":"0000000000","laneType":{"vehicle":"00000000"}},"maneuvers":"100000000000","nodeList":{"nodes":{"NodeXY":[{"delta":{"node-XY3":{"x":"-719","y":"1791"}},"attributes":{"dWidth":"-17"}},{"delta":{"node-XY5":{"x":"-5510","y":"7297"}}},{"delta":{"node-XY5":{"x":"-4353","y":"5761"}}},{"delta":{"node-XY6":{"x":"-6368","y":"8365"}}},{"delta":{"node-XY5":{"x":"-4464","y":"5976"}}}]}},"connectsTo":{"Connection":{"connectingLane":{"lane":"0","maneuver":"100000000000"},"signalGroup":"2"}},"laneID":"3"},{"laneID":"4","ingressApproach":"2","laneAttributes":{"directionalUse":"10","sharedWith":"0000000000","laneType":{"vehicle":"00000000"}},"maneuvers":"100000000000","nodeList":{"nodes":{"NodeXY":[{"delta":{"node-XY3":{"y":"1580","x":"-988"}},"attributes":{"dWidth":"-27"}},{"delta":{"node-XY5":{"y":"7297","x":"-5520"}}},{"delta":{"node-XY5":{"x":"-4352","y":"5761"}}},{"delta":{"node-XY6":{"x":"-6369","y":"8365"}}},{"delta":{"node-XY5":{"x":"-4504","y":"6031"}}}]}},"connectsTo":{"Connection":{"connectingLane":{"maneuver":"100000000000","lane":"0"},"signalGroup":"2"}}},{"nodeList":{"nodes":{"NodeXY":[{"delta":{"node-XY3":{"x":"-1268","y":"1369"}},"attributes":{"dWidth":"-17"}},{"delta":{"node-XY5":{"y":"7296","x":"-5520"}}},{"delta":{"node-XY5":{"x":"-4352","y":"5760"}}},{"delta":{"node-XY6":{"x":"-6369","y":"8366"}}},{"delta":{"node-XY5":{"x":"-4498","y":"6036"}}}]}},"connectsTo":{"Connection":{"connectingLane":{"lane":"0","maneuver":"100000000000"},"signalGroup":"2"}},"laneID":"5","ingressApproach":"2","laneAttributes":{"directionalUse":"10","sharedWith":"0000000000","laneType":{"vehicle":"00000000"}},"maneuvers":"100000000000"},{"laneID":"6","ingressApproach":"2","laneAttributes":{"directionalUse":"10","sharedWith":"0000000000","laneType":{"vehicle":"00000000"}},"maneuvers":"101000000000","nodeList":{"nodes":{"NodeXY":[{"delta":{"node-XY3":{"x":"-1524","y":"1155"}},"attributes":{"dWidth":"3"}},{"delta":{"node-XY5":{"y":"7300","x":"-5544"}}},{"delta":{"node-XY5":{"y":"5759","x":"-4351"}}},{"delta":{"node-XY6":{"x":"-6370","y":"8366"}}},{"delta":{"node-XY5":{"x":"-4461","y":"6039"}}}]}},"connectsTo":{"Connection":[{"connectingLane":{"lane":"0","maneuver":"001000000000"},"signalGroup":"2"},{"connectingLane":{"maneuver":"100000000000","lane":"0"},"signalGroup":"2"}]}}]},"name":"LEESBURG  PIKE @ MARSHALLS DR","id":{"id":"176","region":"1003"},"revision":"2","refPoint":{"lat":"389208651","long":"-772340251"}}}}}},"type":"MAP"};
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let webSocketService: WebSocketShareService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardComponent],
      imports: [
        ReactiveFormsModule,
        FormsModule,
        MatExpansionModule,
        MatFormFieldModule,
        MatInputModule,
        Ng2SearchPipeModule,
        NgApexchartsModule,
        MatSelectModule,
        MatDialogModule,
        MatMenuModule,
        MatProgressSpinnerModule,
        HttpClientTestingModule,
        MatSnackBarModule
      ],
      providers: [HttpClientModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
      .compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    webSocketService = TestBed.get(WebSocketShareService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should render Map on dashboard ', () => {
      const bannerDe: DebugElement = fixture.debugElement;
      fixture.detectChanges();
      const paragraphDe = bannerDe.query(By.css('.map-form-light'));
      const p: HTMLElement = paragraphDe.nativeElement;
      expect(p.textContent).toBeTruthy();
    });

  it(
    'should render detailed view on dashboard ', () => {
      const bannerDe: DebugElement = fixture.debugElement;
      component.rightPanelDataObj.isDetailViewEnabled = true;
      fixture.detectChanges();
      const paragraphDe = bannerDe.query(By.css('.main-box'));
      const p: HTMLElement = paragraphDe.nativeElement;
      expect(p.textContent).toBeTruthy();
    });

    it(
      'Hide/Show right panel ', () => {
        const bannerDe: DebugElement = fixture.debugElement;
        component.rightPanelDataObj.isRightPanelOpen = true;
        fixture.detectChanges();
        const paragraphDe = bannerDe.query(By.css('.map-details'));
        const p: HTMLElement = paragraphDe.nativeElement;
        expect(p.textContent).toBeTruthy();
      });

      it('should call clearDashboardData method to reset local variables', () => {
        spyOn(component, 'clearDashboardData').and.callThrough();
        component.ngOnInit();
        expect(component.clearDashboardData).toHaveBeenCalled();
        expect(DashboardCalculation.dataCount.pedestrian).toEqual(0);
        expect(DashboardCalculation.dataCount.vehicle).toEqual(0);
        expect(DashboardCalculation.dataCount.emergency).toEqual(0);
        expect(DashboardCalculation.psgrType).toEqual(0);
        expect(DashboardCalculation.truckType).toEqual(0);
        expect(DashboardCalculation.evType).toEqual(0);
      });

      it('should call encodedValueReceived method for bsm data', () =>{
        expect(component.encodedValueReceived(bsmDemoData)).toBeUndefined();
      })

      it('should call encodedValueReceived method for psm data', () =>{
        expect(component.encodedValueReceived(psmDemoData)).toBeUndefined();
      })

      it('should call encodedValueReceived method for eva data', () =>{
        expect(component.encodedValueReceived(evaDemoData)).toBeUndefined();
      })

      // it('should call encodedValueReceived method for spat/map data', () =>{
      //   let spatMockData = {'icon': component.mapDataObj.spatStationURL, id: '123', lat: 13.1223212, long: 79.12324, name: 'location', type:'red signal', isOpen: true}
      //   let mockMapObj = {name: 'location', id: '123', lat: 13.2323456, long: 79.1232567, isOpen: false};
      //   DashboardCalculation.spatData.set('123', spatMockData);
      //   DashboardCalculation.mapData.set(mockMapObj.id, mockMapObj);
      //   expect(component.encodedValueReceived(spatDemoData)).toBeUndefined();
      // })

      it('should call decodedValueReceived method for bsm data', () =>{
        expect(component.decodedValueReceived(decodedBSMData)).toBeUndefined();
      })

      it('should call decodedValueReceived method for psm data', () =>{
        expect(component.decodedValueReceived(decodedPSMData)).toBeUndefined();
      })

      it('should call decodedValueReceived method for eva data', () =>{
        expect(component.decodedValueReceived(decodedEVAData)).toBeUndefined();
      })

      it('should call decodedValueReceived method for spat/map data', () =>{
        let spatMockData = {'icon': component.mapDataObj.spatStationURL, id: 123, lat: 13.1223212, long: 79.12324, name: 'location', type:'red signal', isOpen: true}
        let mockMapObj = {name: 'location', id: 123, lat: 13.2323456, long: 79.1232567, isOpen: false};
        DashboardCalculation.spatData.set(123, spatMockData);
        DashboardCalculation.selectedSpatId = 123;
        DashboardCalculation.mapData.set(mockMapObj.id, mockMapObj);
        expect(component.decodedValueReceived(decodedSpatData)).toBeUndefined();
      })

      it('should call patch method', () =>{
        let dataObj = {
          head_by: 100,
          latitude_car: 13.0000123,
          longitude_car: 79.1236009,
          speed_car: 20,
          direction: 11,
          unit: 'mps',
          vin_car: '11029384'
        }
        expect(component.patch(dataObj)).toBeUndefined();
      })

      it('should call patchSpatData method', () =>{
        expect(component.patchSpatData({id:1, name: 'noida', type: 'BSM'})).toBeUndefined();
      })

      it('should call carsLocation method', () =>{
        let mockObj = {lat:13.0987635, long: 79.0009890};
        component.carsLocation(mockObj);
        expect(component.mapDataObj.latitude).toBe(mockObj.lat);
        expect(component.mapDataObj.longitude).toBe(mockObj.long);
        expect(component.mapDataObj.zoom).toBe(17);
      })

      it('should call changeMapMode method', () =>{
        component.changeMapMode('OSM Roadmap');
        expect(component.mapDataObj.mapMode).toBe('OSM Roadmap');
        expect(component.rightPanelDataObj.isMapVisible).toBe(false);
      })

      it('should call resetVehicleId method', () =>{
        expect(component.resetVehicleId()).toBeUndefined();
      })

      it('should call changeTab method', () =>{
        expect(component.changeTab()).toBeUndefined();
      })

      it('should call showHideMapMode method', () =>{
        component.showHideMapMode();
        expect(component.rightPanelDataObj.isMapVisible).toBe(true);
      })

      it('should call ngOnInit method', () =>{
        expect(component.ngOnInit()).toBeUndefined();
      })

      it('should call trackby method', () =>{
        expect(component.trackBy({key:1})).toBe(1);
      })

      // it('should call onNewValueReceive method', () =>{
      //   component.onNewValueReceive();
      //   webSocketService.getNewValue().subscribe((res: any) => {
      //     res = bsmDemoData;
      //     expect(res).toEqual(bsmDemoData);
      //   })
      // })

      it('should call trackByDetails method', () =>{
        expect(component.trackByDetails({key:1})).toBe(1);
      })

      it('should call trackByMarker method', () =>{
        expect(component.trackByMarker({key:1})).toBe(1);
      })

      it('should call ngOnDestroy method', () =>{
        expect(component.ngOnDestroy).toBeTruthy();
      })

      it('should call enableDProccessedDataView method', () =>{
        component.enableDProccessedDataView();
        expect(component.rightPanelDataObj.isProccessedDataEnabled).toBe(true);
      })

      it('should call changeProccessedVal method', () =>{
        component.changeProccessedVal(true);
        expect(component.rightPanelDataObj.isProccessedDataEnabled).toBe(true);
      })

      it('should call selectSpatLocationCar method for condition true', () =>{
        let mockVal = {id:'11223344',lat:13.8989765,long:79.2343212, isOpen:true};
        let spatMockData = {'icon': component.mapDataObj.spatStationURL, id: '11223344', lat: 13.1223212, long: 79.12324, name: 'location', type:'red signal', isOpen: true}
        DashboardCalculation.spatData.set('11223344', spatMockData);
        component.selectSpatLocationCar(1, true, mockVal);
        expect(component.mapDataObj.zoom).toBe(10);
        expect(component.mapDataObj.latitude).toBe(0);
        expect(component.mapDataObj.longitude).toBe(0);
        expect(DashboardCalculation.selectedSpatId).toBe(mockVal.id);
        expect(DashboardCalculation.leftSpatMenuOpen).toBe(true);
        expect(DashboardCalculation.vehicleCircleArr.color).toBe('green');
        component.patchSpatData(mockVal);
        expect(DashboardCalculation.circleMarker).toBe(mockVal);
      })

      it('should call selectSpatLocationCar method for condition false', () =>{
        let mockVal = {id:'11223344',lat:13.8989765,long:79.2343212, isOpen:true};
        let spatMockData = {'icon': component.mapDataObj.spatStationURL, id: '11223344', lat: 13.1223212, long: 79.12324, name: 'location', type:'red signal', isOpen: true};
        DashboardCalculation.spatData.set('11223344', spatMockData);
        expect(component.selectSpatLocationCar(1, false, mockVal));
        expect(DashboardCalculation.selectedSpatId).toBeNull();
        expect(DashboardCalculation.leftSpatMenuOpen).toBe(false);
        expect(DashboardCalculation.vehicleCircleArr.color).toBe('transparent');
        expect(DashboardCalculation.circleMarker).toBe(mockVal);
      })

      it('show call setAllDataInfo method', () =>{
        expect(component.setAllDataInfo({id:'11112345',lat:13.2323456,long: 79.123234345})).toBeUndefined();
      })

      it('show call setVehicleInfo method', () =>{
        expect(component.setVehicleInfo(1, {lat: 13.092334343, long: 79.3454543})).toBeUndefined();
      })

      it('show call resetSpatId method', () =>{
        expect(component.resetSpatId()).toBeUndefined();
      })

      it('show call selectLocationCar method for if condition BSM data', () =>{
        let mockVal = {head_by:100,lat:13.8989765,long:79.2343212, speed_car:100,direction:45,unit: 'mps', id: '11223344', type:'BSM'};
        let mockData = {'icon': component.mapDataObj.vehicleUrl, id: '11223344', 
        lat: 13.1223212, long: 79.12324, name: 'location', isOpen: true, type:'BSM',
        speed: 100, unit: 'mph', color: 'black', }
        DashboardCalculation.allData.set('11223344', mockData);
        component.rightPanelDataObj.selectedCar = mockVal;
        component.selectLocationCar(1, true, mockVal);
        expect(component.mapDataObj.zoom).toBe(10);
        expect(component.rightPanelDataObj.checkedListIndex).toBe(1);
        expect(DashboardCalculation.vehicleCircleArr.color).toBe('#0081F9');
        expect(DashboardCalculation.vehicleCircleArr.lat).toBe(component.rightPanelDataObj.selectedCar.lat);
        expect(DashboardCalculation.vehicleCircleArr.lng).toBe(component.rightPanelDataObj.selectedCar.long);
        component.patch(mockVal);
        expect(DashboardCalculation.circleMarker).toBe(mockVal);
      })

      it('show call selectLocationCar method for if condition EVA data', () =>{
        let mockVal = {head_by:100,lat:13.8989765,long:79.2343212, speed_car:100,direction:45,unit: 'mps', id: '11223344', type:'EVA'};
        let mockData = {'icon': component.mapDataObj.vehicleUrl, id: '11223344', 
        lat: 13.1223212, long: 79.12324, name: 'location', isOpen: true, type:'EVA',
        speed: 100, unit: 'mph', color: 'black', }
        DashboardCalculation.allData.set('11223344', mockData);
        component.rightPanelDataObj.selectedCar = mockVal;
        component.selectLocationCar(1, true, mockVal);
        expect(component.mapDataObj.zoom).toBe(10);
        expect(component.rightPanelDataObj.checkedListIndex).toBe(1);
        expect(DashboardCalculation.vehicleCircleArr.color).toBe('#FF0000');
        expect(DashboardCalculation.vehicleCircleArr.lat).toBe(component.rightPanelDataObj.selectedCar.lat);
        expect(DashboardCalculation.vehicleCircleArr.lng).toBe(component.rightPanelDataObj.selectedCar.long);
        component.patch(mockVal);
        expect(DashboardCalculation.circleMarker).toBe(mockVal);
      })

      it('show call selectLocationCar method for if condition PSM data', () =>{
        let mockVal = {head_by:100,lat:13.8989765,long:79.2343212, speed_car:100,direction:45,unit: 'mps', id: '11223344', type:'PSM'};
        let mockData = {'icon': component.mapDataObj.vehicleUrl, id: '11223344', 
        lat: 13.1223212, long: 79.12324, name: 'location', isOpen: true, type:'PSM',
        speed: 100, unit: 'mps', color: 'black', }
        DashboardCalculation.allData.set('11223344', mockData);
        component.rightPanelDataObj.selectedCar = mockVal;
        component.selectLocationCar(1, true, mockVal);
        expect(component.mapDataObj.zoom).toBe(10);
        expect(component.rightPanelDataObj.checkedListIndex).toBe(1);
        expect(DashboardCalculation.vehicleCircleArr.color).toBe('#3CE711');
        expect(DashboardCalculation.vehicleCircleArr.lat).toBe(component.rightPanelDataObj.selectedCar.lat);
        expect(DashboardCalculation.vehicleCircleArr.lng).toBe(component.rightPanelDataObj.selectedCar.long);
        component.patch(mockVal);
        expect(DashboardCalculation.circleMarker).toBe(mockVal);
      })

      it('show call selectLocationCar method for else condition', () =>{
        let mockVal = {head_by:100,lat:13.8989765,long:79.2343212, speed_car:100,direction:45,unit: 'mps', id: '11223344'};
        let mockData = {'icon': component.mapDataObj.vehicleUrl, id: '11223344', 
        lat: 13.1223212, long: 79.12324, name: 'location', isOpen: true, type:'BSM',
        speed: 100, unit: 'mps', color: 'black', }
        DashboardCalculation.allData.set('11223344', mockData);
        component.rightPanelDataObj.selectedCar = mockVal;
        component.selectLocationCar(1, false, mockVal);
        expect(component.mapDataObj.zoom).toBe(10);
        expect(component.rightPanelDataObj.checkedListIndex).toBe(-1);
        expect(DashboardCalculation.vehicleCircleArr.lat).toBe(component.rightPanelDataObj.selectedCar.lat);
        expect(DashboardCalculation.vehicleCircleArr.lng).toBe(component.rightPanelDataObj.selectedCar.long);
        expect(DashboardCalculation.vehicleCircleArr.color).toBe('transparent');
        component.patch(mockVal);
        expect(DashboardCalculation.circleMarker).toBe(mockVal);
      })

      it('show call changeTab method for if condition', () =>{
        component.rightPanelDataObj.tabType = 'vehicle';
        component.changeTab();
        expect(component.rightPanelDataObj.tabType).toBe('details');
        expect(component.mapDataObj.mapMode).toBe('Google Roadmap');
      })

      it('show call changeTab method for else condition', () =>{
        component.rightPanelDataObj.tabType = 'details';
        component.changeTab();
        expect(component.rightPanelDataObj.tabType).toBe('vehicle');
        expect(component.mapDataObj.mapMode).toBe('Google Hybrid');
      })

      it('should call openRightPanel method for if condition', () =>{
        component.openRightPanel('vehicle');
        expect(component.rightPanelDataObj.tabType).toBe('vehicle');
        expect(component.rightPanelDataObj.isRightPanelOpen).toBe(true);
      })

      it('should call openRightPanel method for else condition', () =>{
        component.openRightPanel('details');
        expect(component.rightPanelDataObj.tabType).toBe('details');
        expect(component.rightPanelDataObj.isRightPanelOpen).toBe(true);
      })

      it('show call enableDetailedView method', () =>{
        component.rightPanelDataObj.isDetailViewEnabled = true;
        component.enableDetailedView();
        expect(component.rightPanelDataObj.isDetailViewEnabled).toBe(false);
      })

  it('testing function onVehicleChange else block with value: 0',()=>{
    component.rightPanelDataObj.listVal = 0;
    component.onVehicleChange(0);
    expect(component.rightPanelDataObj.listVal).toBe(-1);
  })

  it('testing function onVehicleChange if block with value: 0',()=>{
    component.rightPanelDataObj.listVal = 1;
    component.onVehicleChange(0);
    expect(component.rightPanelDataObj.listVal).toBe(0);
    expect(component.filterType).toBe('ALL');
  })

  it('testing function onVehicleChange with value: 1',()=>{
    component.rightPanelDataObj.listVal = 0;
    component.onVehicleChange(1);
    expect(component.rightPanelDataObj.listVal).toBe(1);
    expect(component.filterType).toBe('BSM');
  })

  it('testing function onVehicleChange with value: 2',()=>{
    component.rightPanelDataObj.listVal = 0;
    component.onVehicleChange(2);
    expect(component.rightPanelDataObj.listVal).toBe(2);
    expect(component.filterType).toBe('EVA');
  })

  it('testing function onVehicleChange with value: 3',()=>{
    component.rightPanelDataObj.listVal = 0;
    component.onVehicleChange(3);
    expect(component.rightPanelDataObj.listVal).toBe(3);
    expect(component.filterType).toBe('PSM');
  })

  it('testing function setBSMDataOnMap for if condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000D87F",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "BSM", uiSpeed: "0m/s", unit: "m/s"};
    expect(component.setBSMDataOnMap(bsmDemoData)).toBeUndefined();
  })

  it('testing function setBSMDataOnMap for else condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000D87F",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "BSM", uiSpeed: "0m/s", unit: "m/s"};
    DashboardCalculation.allData.set(DashboardCalculation.circleMarker.id, DashboardCalculation.circleMarker);
    component.rightPanelDataObj.listVal = 1;
    component.rightPanelDataObj.checkedListIndex = 0;
    expect(component.setBSMDataOnMap(bsmDemoData)).toBeUndefined();
  })

  it('testing function setPSMDataOnMap for if condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000D87F",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "BSM", uiSpeed: "0m/s", unit: "m/s"};
    expect(component.setPSMDataOnMap(psmDemoData)).toBeUndefined();
  })

  it('testing function setPSMDataOnMap for else condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000D87F",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "PSM", uiSpeed: "0m/s", unit: "m/s"};
    DashboardCalculation.allData.set(DashboardCalculation.circleMarker.id, DashboardCalculation.circleMarker);
    component.rightPanelDataObj.listVal = 3;
    component.rightPanelDataObj.checkedListIndex = 0;
    expect(component.setPSMDataOnMap(psmDemoData)).toBeUndefined();
  })

  it('testing function setEVADataOnMap for if condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000073B",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "EVA", uiSpeed: "0m/s", unit: "m/s"};
    expect(component.setEVADataOnMap(evaDemoData)).toBeUndefined();
  })

  it('testing function setEVADataOnMap for else condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000073B",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "EVA", uiSpeed: "0m/s", unit: "m/s"};
    DashboardCalculation.allData.set(DashboardCalculation.circleMarker.id, DashboardCalculation.circleMarker);
    component.rightPanelDataObj.listVal = 2;
    component.rightPanelDataObj.checkedListIndex = 0;
    expect(component.setEVADataOnMap(evaDemoData)).toBeUndefined();
  })

  it('testing function setDecodedBSMDataOnMap for if case',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000D87F",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "BSM", uiSpeed: "0m/s", unit: "m/s"};
    expect(component.setDecodedBSMDataOnMap(decodedBSMData)).toBeUndefined();
  })

  it('testing function setDecodedBSMDataOnMap for else case',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000D87F",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "BSM", uiSpeed: "0m/s", unit: "m/s"};
    DashboardCalculation.allData.set(DashboardCalculation.circleMarker.id, DashboardCalculation.circleMarker);
    component.rightPanelDataObj.listVal = 1;
    component.rightPanelDataObj.checkedListIndex = 0;
    expect(component.setDecodedBSMDataOnMap(decodedBSMData)).toBeUndefined();
  })

  it('testing function setDecodedPSMDataOnMap for if condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000D87F",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "PSM", uiSpeed: "0m/s", unit: "m/s"};
    expect(component.setDecodedPSMDataOnMap(decodedPSMData)).toBeUndefined();
  })

  it('testing function setDecodedPSMDataOnMap for else condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000D87F",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "BSM", uiSpeed: "0m/s", unit: "m/s"};
    DashboardCalculation.allData.set(DashboardCalculation.circleMarker.id, DashboardCalculation.circleMarker);
    component.rightPanelDataObj.listVal = 3;
    component.rightPanelDataObj.checkedListIndex = 0;
    expect(component.setDecodedPSMDataOnMap(decodedPSMData)).toBeUndefined();
  })

  it('testing function setDecodedEVADataOnMap for if condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000073B",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "EVA", uiSpeed: "0m/s", unit: "m/s"};
    expect(component.setDecodedEVADataOnMap(decodedEVAData)).toBeUndefined();
  })

  it('testing function setDecodedEVADataOnMap for else condition',()=>{
    DashboardCalculation.circleMarker = {color: "green",direction: "North East", 
    heading: "45", icon: "../assets/pad.svg",iconurl: "../../assets/ped_group_icon.svg",
    id: "0000073B",isOpen: false,lat: 28.4434383,long:77.0550462,
    speed: 0,type: "EVA", uiSpeed: "0m/s", unit: "m/s"};
    DashboardCalculation.allData.set(DashboardCalculation.circleMarker.id, DashboardCalculation.circleMarker);
    component.rightPanelDataObj.listVal = 2;
    component.rightPanelDataObj.checkedListIndex = 0;
    expect(component.setDecodedEVADataOnMap(decodedEVAData)).toBeUndefined();
  })

  it('testing function encodedSPATValueReceived for SPAT data',()=>{
    let mockMapObj = {name: 'location', id: '123', lat: 13.2323456, long: 79.1232567, isOpen: false};
    let mockSpatObj = {name: 'location', id: '123', lat: 13.2323456, long: 79.1232567, isOpen: false, icon: component.mapDataObj.spatStationURL};
    DashboardCalculation.spatData.set(mockSpatObj.id, mockSpatObj);
    DashboardCalculation.mapData.set(mockMapObj.id, mockMapObj);
    DashboardCalculation.selectedSpatId = '123';
    expect(component.encodedSPATValueReceived(spatDemoData)).toBeUndefined();
  })

  it('testing function encodedSPATValueReceived for MAP data',()=>{
    expect(component.encodedSPATValueReceived(mapDemoData)).toBeUndefined();
  })

  it('testing function decodedSPATValueReceived for SPAT data',()=>{
    let mockObj = {name: 'location', id: 123, lat: 13.2323456, long: 79.1232567, isOpen: false};
    let mockSpatObj = {name: 'location', id: 123, lat: 13.2323456, long: 79.1232567, isOpen: false, icon: component.mapDataObj.spatStationURL};
    DashboardCalculation.spatData.set(mockSpatObj.id, mockSpatObj);
    DashboardCalculation.mapData.set(mockObj.id, mockObj);
    DashboardCalculation.selectedSpatId = 123;
    expect(component.decodedSPATValueReceived(decodedSpatData)).toBeUndefined();
  })

  it('testing function decodedSPATValueReceived for MAP data',()=>{
    expect(component.decodedSPATValueReceived(decodedMapData)).toBeUndefined();
  })
});
