package com.vrsu.subscriber.schedule;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vrsu.subscriber.utility.DecoderUtils;

public class ScheduleJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJob.class);
    @Autowired
    DecoderUtils decode;
    @Override
    public void execute(JobExecutionContext context) {
      decode.setTimeBasedFlag(true);
    }

}