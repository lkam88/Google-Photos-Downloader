package com.lucaskam.googlePhotosDownloader.models;

/**
 * https://developers.google.com/photos/library/reference/rest/v1/mediaItems
 * */
public class MediaItem {
    private String id;
    private String description;
    private String productUrl;
    private String baseUrl;
    private String mimeType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }


    @Override
    public String toString() {
        return "MediaItem{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", productUrl='" + productUrl + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaItem mediaItem = (MediaItem) o;

        return getId().equals(mediaItem.getId());
    }
}
