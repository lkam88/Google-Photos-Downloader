package com.lucaskam.googlePhotosDownloader.services;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GoogleDownloadProgressListener implements MediaHttpDownloaderProgressListener {
    private final static Logger LOG = LogManager.getLogger();

    @Override
    public void progressChanged(MediaHttpDownloader downloader) throws IOException {
        switch (downloader.getDownloadState()) {
            case MEDIA_IN_PROGRESS:
                LOG.debug("Download in progress" + downloader.getProgress());
                break;
            case MEDIA_COMPLETE:
                LOG.debug("Photo downloaded");
        }
    }
}
