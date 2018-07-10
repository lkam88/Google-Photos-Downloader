package com.lucaskam.googlePhotosDownloader;

import com.lucaskam.googlePhotosDownloader.models.RuntimeConfigurations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GooglePhotosDownloaderJob implements Job {
    private final static Logger LOG = LogManager.getLogger();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Initializing job");

        try {
            RuntimeConfigurations runtimeConfigurations = (RuntimeConfigurations) context.getJobDetail().getJobDataMap().get("runtimeConfigurations");

            LOG.info("Running job");
            new GooglePhotosDownloader(runtimeConfigurations).run();
            LOG.info("Job finished");
        } catch (Exception e) {
            LOG.error("Error running job", e);
            throw new JobExecutionException(e);
        }
    }
}
