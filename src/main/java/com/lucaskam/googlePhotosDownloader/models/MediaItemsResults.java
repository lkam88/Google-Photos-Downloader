package com.lucaskam.googlePhotosDownloader.models;

import java.util.List;

public class MediaItemsResults {
    private List<MediaItem> mediaItems;
    private String nextPageToken;

    public List<MediaItem> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(List<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
