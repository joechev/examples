package com.example.batch.fixeddelay.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    static final Integer CHUNK_SIZE = 5;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fixedDelayJob(Step fixedDelayStep) {
        return jobBuilderFactory.get("fixedDelayJob")
                .start(fixedDelayStep)
                .build();
    }

    @Bean
    public Step fixedDelayStep() {
        return stepBuilderFactory.get("fixedDelayStep")
                .<String, String>chunk(CHUNK_SIZE)
                .reader(uuidReader())
                .writer(assertingWriter())
                .build();
    }

    @Bean
    @JobScope
    public ListItemReader<String> uuidReader() {
        return new ListItemReader<>(IntStream.range(0, CHUNK_SIZE * 2)
                .mapToObj(i -> UUID.randomUUID().toString())
                .collect(Collectors.toList()));
    }

    @Bean
    public ItemWriter<String> assertingWriter() {
        return list -> {
            assert list.size() <= CHUNK_SIZE;
        };
    }

}
