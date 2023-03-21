package com.vrsu.subscriber.schedule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrsu.subscriber.utility.DecoderUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MyScheduledTask {

	@Autowired
	private Scheduler scheduler;
	@Autowired
	DecoderUtils decode;
	private String startExpression = "* * * * * *";
	private LocalTime end;

	public void scheduleJob() throws SchedulerException {
		log.info("Scheduling cron job with cron expression {}", startExpression);

		JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).build();

		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(startExpression))
				.build();

		// Create a job listener that prints a message when the job completes
		JobListener jobListener = new JobListener() {
			@Override
			public String getName() {
				return "PrintOnJobComplete";
			}

			@Override
			public void jobToBeExecuted(JobExecutionContext context) {
			}

			@Override
			public void jobExecutionVetoed(JobExecutionContext context) {
			}

			@Override
			public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
				// Check if the end time has been reached, and print a message if it has
				LocalTime now = LocalTime.now();
				Duration duration = Duration.between(now, end);
				if (duration.isNegative() || duration.isZero()) {
					decode.setTimeBasedFlag(false);
				}
			}
		};

		// Add the job listener to the scheduler
		scheduler.getListenerManager().addJobListener(jobListener);
		scheduler.scheduleJob(jobDetail, trigger);
	}

	public void getCronExpression(LocalTime startTime, LocalTime endTime) throws SchedulerException {

		Duration duration = Duration.between(startTime, endTime);
		this.end = endTime;
		// Calculate the minutes and hours of the duration
		int minutes = (int) duration.toMinutes() % 60;
		int hours = (int) duration.toHours();

		// Create the cron expression
		this.startExpression = String.format("* %d-%d %d-%d * * ?", startTime.getMinute(),endTime.getMinute(), startTime.getHour(),
				endTime.getHour());
		scheduler.clear();
		scheduleJob();

	}

}
