package com.lucaskam.googlePhotosDownloader.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.*;
import com.google.api.client.util.ObjectParser;
import com.lucaskam.googlePhotosDownloader.exceptions.ServiceException;
import com.lucaskam.googlePhotosDownloader.models.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GooglePhotosService {
    private HttpRequestFactory httpRequestFactory;
    private ObjectParser objectParser;
    private Integer photosDownloadWidth;
    private Integer photosDownloadHeight;

    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public GooglePhotosService(HttpRequestFactory httpRequestFactory, ObjectParser objectParser, Integer photosDownloadWidth, Integer photosDownloadHeight) {
        this.httpRequestFactory = httpRequestFactory;
        this.objectParser = objectParser;
        this.photosDownloadWidth = photosDownloadWidth;
        this.photosDownloadHeight = photosDownloadHeight;
    }

    public AlbumsResult getAlbums() throws ServiceException {
        return getAlbums(null);
    }


    public AlbumsResult getAlbums(String pageToken) throws ServiceException {
        try {
            String encodedUrl = "https://photoslibrary.googleapis.com/v1/albums";
            if (pageToken != null) {
                encodedUrl += "?pageToken=" + pageToken;
            }

            HttpRequest httpRequest = httpRequestFactory.buildGetRequest(new GenericUrl(encodedUrl));

            return parseResponse(httpRequest.execute(), AlbumsResult.class);
        } catch (Exception e) {
            throw new ServiceException("Unable to get albums", e);
        }
    }

    public Album getAlbum(String albumId) throws ServiceException {
        String encodedUrl = "https://photoslibrary.googleapis.com/v1/albums/" + albumId;

        HttpRequest httpRequest = null;
        try {
            httpRequest = httpRequestFactory.buildGetRequest(new GenericUrl(encodedUrl));
            return parseResponse(httpRequest.execute(), Album.class);
        } catch (IOException e) {
            throw new ServiceException("Unable to get album: " + albumId, e);
        }
    }

    public List<Album> getAllAlbums() throws ServiceException {
        List<Album> results = new ArrayList<>();

        String nextPageToken = null;
        do {
            AlbumsResult albumsResult = getAlbums(nextPageToken);
            results.addAll(albumsResult.getAlbums());
            nextPageToken = albumsResult.getNextPageToken();

        } while (nextPageToken != null);

        return results;
    }

    public List<MediaItem> getAlMediaItemsInAlbum(String albumId) throws ServiceException {
        List<MediaItem> results = new ArrayList<>();

        String nextPageToken = null;
        do {
            MediaItemsResults mediaItemsResults = getMediaItems(albumId, nextPageToken);
            results.addAll(mediaItemsResults.getMediaItems());
            nextPageToken = mediaItemsResults.getNextPageToken();

        } while (nextPageToken != null);

        return results;
    }

    public MediaItemsResults getMediaItems(String albumId, String pageToken) throws ServiceException {
        String encodedUrl = "https://photoslibrary.googleapis.com/v1/mediaItems:search";
        MediaItemsSearchRequest mediaItemsSearchRequest = new MediaItemsSearchRequest();
        mediaItemsSearchRequest.setAlbumId(albumId);

        if (pageToken != null) {
            mediaItemsSearchRequest.setPageToken(pageToken);
        }

        try {
            String requestBody = objectMapper.writeValueAsString(mediaItemsSearchRequest);

            HttpRequest httpRequest = httpRequestFactory.buildPostRequest(new GenericUrl(encodedUrl), ByteArrayContent.fromString("application.json", requestBody));


            return parseResponse(httpRequest.execute(), MediaItemsResults.class);
        } catch (Exception e) {
            throw new ServiceException("Unable to parse response", e);
        }
    }

    private <T> T parseResponse(HttpResponse httpResponse, Class<T> clazz) throws ServiceException {
        T result;
        try {
            if (httpResponse.isSuccessStatusCode()) {
                String stringResult = httpResponse.parseAsString();
                ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                result = objectMapper.readValue(stringResult, clazz);
            } else {
                throw new ServiceException("Request came back with response code " + httpResponse.getStatusCode());
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException("Unable to parse response", e);
        }
    }

    public void downloadPhotoOutputStream(OutputStream outputStream, MediaItem mediaItem) throws ServiceException {
        try {
            MediaHttpDownloader downloader = new MediaHttpDownloader(httpRequestFactory.getTransport(), httpRequestFactory.getInitializer());
            downloader.setProgressListener(new GoogleDownloadProgressListener());
            downloader.download(new GenericUrl(mediaItem.getBaseUrl() + "=w" + photosDownloadWidth + "-h" + photosDownloadHeight), outputStream);
        } catch (Exception e) {
            throw new ServiceException("Unable to download photo", e);
        }
    }

    public HttpRequestFactory getHttpRequestFactory() {
        return httpRequestFactory;
    }

    public void setHttpRequestFactory(HttpRequestFactory httpRequestFactory) {
        this.httpRequestFactory = httpRequestFactory;
    }

    public ObjectParser getObjectParser() {
        return objectParser;
    }

    public void setObjectParser(ObjectParser objectParser) {
        this.objectParser = objectParser;
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
}


