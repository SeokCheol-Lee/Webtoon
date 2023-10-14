package com.example.webtoon.global.batch;

import com.example.webtoon.global.batch.tasklet.StarScoreTasklet;
import com.example.webtoon.global.batch.tasklet.ViewCountTasklet;
import com.example.webtoon.global.batch.tasklet.UserDeleteTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final ViewCountTasklet viewCountTasklet;
    private final StarScoreTasklet starScoreTasklet;
    private final UserDeleteTasklet userDeleteTasklet;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    //Batch Job 생성
    @Bean
    public Job weeklyJob(){
        return jobBuilderFactory.get("weeklyJob")
            .start(weeklyStep())
            .build();
    }

    //Batch Step 생성
    @Bean
    @JobScope //Job 실행시점에 Bean이 생성됨
    public Step weeklyStep() {
        return stepBuilderFactory.get("weeklyStep")
            .tasklet(userDeleteTasklet)
            .build();
    }
    @Bean
    public Job fiveMinutesJob(){
        return jobBuilderFactory.get("fiveMinutesJob")
            .start(fiveMinutesStep())
            .build();
    }

    //Batch Step 생성
    @Bean
    @JobScope //Job 실행시점에 Bean이 생성됨
    public Step fiveMinutesStep() {
        return stepBuilderFactory.get("fiveMinutesStep")
            .tasklet(viewCountTasklet)
            .build();
    }

    //Batch Job 생성
    @Bean
    public Job dailyJob(){
        return jobBuilderFactory.get("dailyJob")
            .start(dailyStep())
            .build();
    }

    //Batch Step 생성
    @Bean
    @JobScope //Job 실행시점에 Bean이 생성됨
    public Step dailyStep() {
        return stepBuilderFactory.get("dailyStep")
            .tasklet(starScoreTasklet)
            .build();
    }
}
