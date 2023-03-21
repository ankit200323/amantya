import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { DashboardService } from 'src/app/services/dashboard.service';
import * as _moment from 'moment';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { WebsocketapiService } from 'src/app/services/websocketapi.service';
import * as URL from '../../../urls/url';
@Component({
  selector: 'app-processed-dashboard-data',
  templateUrl: './processed-dashboard-data.component.html',
  styleUrls: ['./processed-dashboard-data.component.scss']
})
export class ProcessedDashboardDataComponent implements OnInit {
  @Output() setProccessedVal = new EventEmitter<boolean>();
  tableData: any = [];
  dataSource = new MatTableDataSource(this.tableData);
  startDate: any;
  endDate: any;
  dataLength = 0;
  pageIndex: any = 0;
  requestDataObj: any = {
    'pageNo': 0,
    'pageSize': 10,
    'startDate': '',
    'endDate': '',
    'dataObj': {}
  }
  currentDate = new Date();
  @ViewChild('paginator')
  paginator!: MatPaginator;
  displayedColumns: string[] = ['publisherId', 'speed', 'latitude', 'longitude', 'messageType', 'timestamp', 'topic'];
  isMsgTypeModalOpen: boolean = false;
  isTopicModalOpen: boolean = false;
  topicFilter: string = '';
  topicFilterText: string = '';
  msgTypeFilter: string = '';

  constructor(private dashboardService: DashboardService,
    public websocketService: WebsocketapiService) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  disableProccessedView() {
    this.setProccessedVal.emit(false);
  }

  getServerData(event: any) {
    this.getDataFromDababase(event.pageIndex, 10, false);
  }

  getDataFromDababase(pageNo: any, pageSize: any, isFromBtnClick: any) {
    if (isFromBtnClick) {
      this.dataSource.paginator?.firstPage();
    }
    this.isMsgTypeModalOpen = false;
    this.isTopicModalOpen = false;
    this.websocketService.loading = true;
    let startDate = _moment(this.startDate).format('YYYY-MM-DDTHH:mm:ss.SS');
    let endDate = _moment(this.endDate).format('YYYY-MM-DDTHH:mm:ss.SS');
    this.requestDataObj = {
        'messageType': this.msgTypeFilter,
        'topicHas': this.topicFilterText
      }
    let url = `${URL.getProccessedData}?page=${pageNo}&size=${pageSize}&startDate=${startDate}&endDate=${endDate}`;
    this.dashboardService.fetchData('post', url, this.requestDataObj).subscribe((result: any) => {
      this.websocketService.loading = false;
      if (result) {
        this.tableData = result['content'];
        this.dataSource = new MatTableDataSource(this.tableData);
        this.dataLength = result['totalElements'];
        this.pageIndex = result['number'];
      }
    }, (error: any) => {
      this.websocketService.loading = false;
    })
  }

  getPublisherId(val: any){
    return val.replace(/\s/g, "");
  }

}
