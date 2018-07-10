package com.lucaskam.googlePhotosDownloader.models;

public class Album {
    private String id;
    private String title;
    private String productUrl;
    private String coverPhotoBaseUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getCoverPhotoBaseUrl() {
        return coverPhotoBaseUrl;
    }

    public void setCoverPhotoBaseUrl(String coverPhotoBaseUrl) {
        this.coverPhotoBaseUrl = coverPhotoBaseUrl;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", productUrl='" + productUrl + '\'' +
                ", coverPhotoBaseUrl='" + coverPhotoBaseUrl + '\'' +
                '}';
    }
}
