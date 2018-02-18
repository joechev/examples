package com.example.batch.csvreaderdbwriter.config;

import com.example.batch.csvreaderdbwriter.model.data.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TrackingJobListener trackingJobListener;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public Job csvJob(Step csvStep) {
        return jobBuilderFactory.get("csvJob")
                .start(csvStep)
                .listener(trackingJobListener)
                .build();
    }

    @Bean
    public Step csvStep(ItemProcessor<String[], Data> mappingProcessor) {
        return stepBuilderFactory.get("csvStep")
                .<String[], Data>chunk(100)
                .reader(csvReader())
                .processor(mappingProcessor)
                .writer(dbWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<String[]> csvReader() {
        final FlatFileItemReader<String[]> reader = new FlatFileItemReader<>();
        reader.setEncoding("UTF-8");
        reader.setResource(new ClassPathResource("data.csv"));
        reader.setLineMapper((s, i) -> s.split(","));
        return reader;
    }

    @Bean
    @JobScope
    public ItemProcessor<String[], Data> mappingProcessor(@Value("#{jobExecutionContext[trackingId]}") Long trackingId) {
        return s -> {
            final Data data = new Data();
            data.setFkTrkId(trackingId);
            data.setProduct(s[0]);
            data.setVersion(s[1]);
            data.setServer(s[2]);
            return data;
        };
    }

    @Bean
    public JpaItemWriter<Data> dbWriter() {
        final JpaItemWriter<Data> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
