package com.example.batch.fixeddelay;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class FixedDelayBatchJobApplication {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job fixedDelayJob;

	public static void main(String[] args) {
		SpringApplication.run(FixedDelayBatchJobApplication.class, args);
	}

	@Scheduled(fixedDelay = 1000L)
    public void runJob() {
	    try {
            jobLauncher.run(fixedDelayJob, new JobParameters(Collections.singletonMap("timest", new JobParameter(new Date()))));
        } catch(JobExecutionException je) {
	        //handle error
        }
    }

}
