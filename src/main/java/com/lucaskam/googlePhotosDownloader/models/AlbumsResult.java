package com.lucaskam.googlePhotosDownloader.models;

import java.util.List;

public class AlbumsResult {

    private List<Album> albums;

    private String nextPageToken;

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    @Override
    public String toString() {
        return "AlbumsResult{" +
                "albums=" + albums +
                ", nextPageToken='" + nextPageToken + '\'' +
                '}';
    }
}
