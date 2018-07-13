package com.lucaskam.googlePhotosDownloader.models;

public class RuntimeConfigurations {
    private String albumName;
    private String googleCredentialsPath;
    private String photosPath;
    private Integer photosDownloadWidth;
    private Integer photosDownloadHeight;
    private Integer downloadIntervalInMinutes;
    private String fileDataStorePath;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getGoogleCredentialsPath() {
        return googleCredentialsPath;
    }

    public void setGoogleCredentialsPath(String googleCredentialsPath) {
        this.googleCredentialsPath = googleCredentialsPath;
    }

    public String getPhotosPath() {
        return photosPath;
    }

    public void setPhotosPath(String photosPath) {
        this.photosPath = photosPath;
    }

    public Integer getPhotosDownloadWidth() {
        return photosDownloadWidth;
    }

    public void setPhotosDownloadWidth(Integer photosDownloadWidth) {
        this.photosDownloadWidth = photosDownloadWidth;
    }

    public Integer getPhotosDownloadHeight() {
        return photosDownloadHeight;
    }

    public void setPhotosDownloadHeight(Integer photosDownloadHeight) {
        this.photosDownloadHeight = photosDownloadHeight;
    }

    public Integer getDownloadIntervalInMinutes() {
        return downloadIntervalInMinutes;
    }

    public void setDownloadIntervalInMinutes(Integer downloadIntervalInMinutes) {
        this.downloadIntervalInMinutes = downloadIntervalInMinutes;
    }

    public String getFileDataStorePath() {
        return fileDataStorePath;
    }

    public void setFileDataStorePath(String fileDataStorePath) {
        this.fileDataStorePath = fileDataStorePath;
    }
}