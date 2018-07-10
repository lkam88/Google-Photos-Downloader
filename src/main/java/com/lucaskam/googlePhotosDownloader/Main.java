package com.lucaskam.googlePhotosDownloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucaskam.googlePhotosDownloader.models.RuntimeConfigurations;
import org.apache.commons.io.FileUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Main {

    public static void main(String[] args) throws Exception {
        String configurationFilePath = args[0];

        File configurationFile = new File(configurationFilePath);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = FileUtils.readFileToString(configurationFile, "UTF-8");

        RuntimeConfigurations runtimeConfigurations = objectMapper.readValue(json, RuntimeConfigurations.class);

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        Scheduler scheduler = schedulerFactory.getScheduler();

        scheduler.start();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("runtimeConfigurations", runtimeConfigurations);

        JobDetail googlePhotosDownloaderJob = newJob(GooglePhotosDownloaderJob.class)
                .withIdentity("googlePhotosDownloader")
                .usingJobData(jobDataMap)
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("googlePhotosDownloaderTrigger", "group")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(runtimeConfigurations.getDownloadIntervalInMinutes())
                                .repeatForever()
                )
                .build();

        scheduler.scheduleJob(googlePhotosDownloaderJob, trigger);
    }
}
