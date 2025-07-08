package com.happypill.application.event.outbox;

import org.springframework.boot.task.SimpleAsyncTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class MessageRelayConfig {
    public static final String MESSAGE_RELAY_PUBLISH_EVENT_EXECUTOR = "messageRelayPublishEventExecutor";
    public static final String MESSAGE_RELAY_PUBLISH_PENDING_EVENT_EXECUTOR = "messageRelayPublishPendingEventExecutor";

    @Bean(MESSAGE_RELAY_PUBLISH_EVENT_EXECUTOR)
    public Executor messageRelayPublishEventExecutor() {
        return Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("m-relay-v-").factory()
        );
    }

    @Bean(MESSAGE_RELAY_PUBLISH_PENDING_EVENT_EXECUTOR)
    public TaskScheduler messageRelayPublishPendingEventExecutor() {
        return new SimpleAsyncTaskSchedulerBuilder()
                .virtualThreads(true)
                .threadNamePrefix("m-scheduler-")
                .build();
//        return Executors.newSingleThreadScheduledExecutor();
    }

}
