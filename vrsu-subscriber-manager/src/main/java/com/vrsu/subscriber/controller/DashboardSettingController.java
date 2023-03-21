package com.vrsu.subscriber.controller;

import java.time.LocalTime;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vrsu.subscriber.model.DashboardSetting;
import com.vrsu.subscriber.schedule.MyScheduledTask;
import com.vrsu.subscriber.service.DashboardSettingService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/api/setting")
public class DashboardSettingController {

	@Autowired
	DashboardSettingService dashServ;
	@Autowired
	MyScheduledTask scheduleService;
	@CrossOrigin
	@PostMapping(value = "/data-vault-setting")
	public ResponseEntity<String> saveDataVaultSetting(@RequestBody DashboardSetting ds) {
		try {
			dashServ.saveDataVaultSetting(ds);
			return new ResponseEntity<String>("saved", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("500", HttpStatus.OK);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/get-data-vault-setting")
	public ResponseEntity<DashboardSetting> getDataVaultSetting(@RequestParam String type) {
		try {
			return new ResponseEntity<DashboardSetting>(dashServ.getDataVaultSetting(type), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<DashboardSetting>( HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	  @GetMapping("/cron-expression")
	    public void changeCronExpression() throws SchedulerException {
		  DashboardSetting d = dashServ.getDataVaultSetting("data-vault");
	      scheduleService.getCronExpression( LocalTime.parse(d.getStartTimeStamp()), LocalTime.parse(d.getEndTimeStamp()));
	    }

}
