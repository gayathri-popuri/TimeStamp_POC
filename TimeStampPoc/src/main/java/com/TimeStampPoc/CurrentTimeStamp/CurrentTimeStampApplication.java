package com.TimeStampPoc.CurrentTimeStamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableKafka

public class CurrentTimeStampApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrentTimeStampApplication.class, args);
    }

    }





