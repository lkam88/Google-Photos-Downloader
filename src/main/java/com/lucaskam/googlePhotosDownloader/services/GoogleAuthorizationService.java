package com.lucaskam.googlePhotosDownloader.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStoreFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class GoogleAuthorizationService {
    private JsonFactory jsonFactory;
    private HttpTransport httpTransport;
    private DataStoreFactory dataStoreFactory;

    public GoogleAuthorizationService(JsonFactory jsonFactory, HttpTransport httpTransport, DataStoreFactory dataStoreFactory) {
        this.jsonFactory = jsonFactory;
        this.httpTransport = httpTransport;
        this.dataStoreFactory = dataStoreFactory;
    }

    public Credential generateCredential(InputStreamReader credentialInputStream, List<String> scopes) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, credentialInputStream);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes).setDataStoreFactory(dataStoreFactory).build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public JsonFactory getJsonFactory() {
        return jsonFactory;
    }

    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public HttpTransport getHttpTransport() {
        return httpTransport;
    }

    public void setHttpTransport(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    public DataStoreFactory getDataStoreFactory() {
        return dataStoreFactory;
    }

    public void setDataStoreFactory(DataStoreFactory dataStoreFactory) {
        this.dataStoreFactory = dataStoreFactory;
    }

}
