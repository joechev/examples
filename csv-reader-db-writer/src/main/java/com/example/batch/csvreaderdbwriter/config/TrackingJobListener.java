package com.example.batch.csvreaderdbwriter.config;

import com.example.batch.csvreaderdbwriter.model.tracking.Tracking;
import com.example.batch.csvreaderdbwriter.model.tracking.TrackingRepository;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackingJobListener implements JobExecutionListener {

    @Autowired
    private TrackingRepository trackingRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        final Tracking tracking = trackingRepository.save(new Tracking());
        jobExecution.getExecutionContext().putLong("trackingId", tracking.getId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        trackingRepository.updateEndTime(jobExecution.getExecutionContext().getLong("trackingId"));
    }

}
