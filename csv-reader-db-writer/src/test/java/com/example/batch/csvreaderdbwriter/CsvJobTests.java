package com.example.batch.csvreaderdbwriter;

import com.example.batch.csvreaderdbwriter.model.data.Data;
import com.example.batch.csvreaderdbwriter.model.data.DataRepository;
import com.example.batch.csvreaderdbwriter.model.tracking.Tracking;
import com.example.batch.csvreaderdbwriter.model.tracking.TrackingRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class CsvJobTests extends CsvReaderDbWriterApplicationTests {

    @Autowired
    private Job csvJob;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private TrackingRepository trackingRepository;

    private JobLauncherTestUtils jobLauncherTestUtils;

    @Before
    public void init() {
        jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(csvJob);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);
    }

    @Test
    public void runJob() throws Exception {
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder().addDate("timest", new Date()).toJobParameters());
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        final Tracking tracking = trackingRepository.findAll().get(0);
        assertNotNull(tracking);
        assertTrue(tracking.getStartTime().compareTo(tracking.getEndTime()) < 0);
        final List<Data> dataList = dataRepository.findAll();
        assertEquals(3, dataList.size());
        dataList.forEach(d -> assertEquals(tracking.getId(), d.getFkTrkId()));
    }

}
