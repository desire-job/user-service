package com.gmail.apachdima.desirejob.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;

@Configuration
public class SSLTrustStoreConfig {

    @Value("${app.ssl.trust-store.password}")
    private String trustStorePassword;
    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;

    @PostConstruct
    public void init() {
        String path = System.getProperty("user.dir");
        System.setProperty("javax.net.ssl.trustStore", path + File.separator + "user-service" + File.separator + "app-trust-store.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);

        System.setProperty("javax.net.ssl.keyStore", path + File.separator + "user-service" + File.separator + "app-keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);

        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}
