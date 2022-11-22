package com.stargazerstudios.existence.eligius.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Configuration
@ConfigurationProperties("file")
public class FileStorageProperties {
    @NotBlank
    private String uploadPath;

    @NotBlank
    private String downloadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }
}
