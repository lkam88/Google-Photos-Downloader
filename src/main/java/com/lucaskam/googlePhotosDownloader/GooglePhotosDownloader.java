package com.lucaskam.googlePhotosDownloader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.lucaskam.googlePhotosDownloader.exceptions.ServiceException;
import com.lucaskam.googlePhotosDownloader.models.Album;
import com.lucaskam.googlePhotosDownloader.models.MediaItem;
import com.lucaskam.googlePhotosDownloader.models.RuntimeConfigurations;
import com.lucaskam.googlePhotosDownloader.services.GoogleAuthorizationService;
import com.lucaskam.googlePhotosDownloader.services.GooglePhotosService;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GooglePhotosDownloader {
    private final static Logger LOG = LogManager.getLogger();

    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/photoslibrary");

    private RuntimeConfigurations runtimeConfigurations;

    public GooglePhotosDownloader(RuntimeConfigurations runtimeConfigurations) {
        this.runtimeConfigurations = runtimeConfigurations;
    }

    public void run() throws ServiceException {
        try {
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            DataStoreFactory dataStoreFactory = new FileDataStoreFactory(new File(runtimeConfigurations.getFileDataStorePath()));

            InputStreamReader credentialInputStream = new InputStreamReader(new FileInputStream(runtimeConfigurations.getGoogleCredentialsPath()));

            GoogleAuthorizationService googleAuthorizationService = new GoogleAuthorizationService(jsonFactory, httpTransport, dataStoreFactory);

            Credential credential = googleAuthorizationService.generateCredential(credentialInputStream, SCOPES);

            GooglePhotosService googlePhotosService = new GooglePhotosService(httpTransport.createRequestFactory(credential),
                    new JsonObjectParser(jsonFactory),
                    runtimeConfigurations.getPhotosDownloadWidth(),
                    runtimeConfigurations.getPhotosDownloadHeight());

            LOG.debug("Finding album with name {}", runtimeConfigurations.getAlbumName());
            List<Album> allAlbums = googlePhotosService.getAllAlbums();

            String albumId = null;
            for (Album album : allAlbums) {
                if (runtimeConfigurations.getAlbumName().equalsIgnoreCase(album.getTitle())) {
                    albumId = album.getId();
                }
            }

            if (albumId == null) {
                throw new ServiceException("Album with name " + runtimeConfigurations.getAlbumName() + " not found");
            }

            File photosFolder = new File(runtimeConfigurations.getPhotosPath());
            if (!photosFolder.exists()) {
                photosFolder.mkdir();
            }

            List<MediaItem> mediaAlreadyDownloaded = new ArrayList<>();
            for (File file : photosFolder.listFiles()) {
                if ("jpeg".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()))) {
                    MediaItem mediaItem = new MediaItem();
                    mediaItem.setId(file.getName().substring(0, file.getName().length() - 5));
                    mediaAlreadyDownloaded.add(mediaItem);
                }
            }

            LOG.debug("Total Photos already downloaded: {}", mediaAlreadyDownloaded.size());

            List<MediaItem> allMediaInAlbum = googlePhotosService.getAlMediaItemsInAlbum(albumId);

            LOG.debug("Total photos in album: {}", allMediaInAlbum.size());

            List<MediaItem> mediaItemsToBeDownloaded = new ArrayList<>();

            for (MediaItem mediaItem : allMediaInAlbum) {
                if (!mediaAlreadyDownloaded.contains(mediaItem)) {
                    mediaItemsToBeDownloaded.add(mediaItem);
                }
            }

            LOG.debug("Total photos to be downloaded: {}", mediaItemsToBeDownloaded.size());

            List<MediaItem> mediaItemsToBeDeleted = new ArrayList<>();
            for (MediaItem mediaItem : mediaAlreadyDownloaded) {
                if (!allMediaInAlbum.contains(mediaItem)) {
                    mediaItemsToBeDeleted.add(mediaItem);
                }
            }

            LOG.debug("Total photos to be deleted: {}", mediaItemsToBeDeleted.size());

            LOG.info("Deleting photos");
            for (MediaItem mediaItemToBeDeleted : mediaItemsToBeDeleted) {
                File file = new File(runtimeConfigurations.getPhotosPath() + File.separator + mediaItemToBeDeleted.getId() + ".jpeg");
                file.delete();
            }

            LOG.info("Downloading photos");
            mediaItemsToBeDownloaded.forEach(mediaItem -> {
                try (FileOutputStream file = new FileOutputStream(runtimeConfigurations.getPhotosPath() + File.separator + mediaItem.getId() + ".jpeg")) {
                    googlePhotosService.downloadPhotoOutputStream(file, mediaItem);
                } catch (ServiceException | IOException e) {
                    LOG.error("Unable to download media item: %s", mediaItem.getId());
                }
            });
        } catch (Exception e) {
            throw new ServiceException("Error occurred while downloading photos", e);
        }
    }

}
