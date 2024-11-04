package com.example.demo.bookstore.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.io.IOException;

@Data
@Component
@ConfigurationProperties("app")
@RefreshScope
public class BookstoreProperties {

    @Data
    public static class FileProperties {
        private String directory;
    }

    private FileProperties file;

    private String rules;

    @Transient
    public String getRelativePath() throws IOException {
        ResourceLoader resourceLoader = new ClassRelativeResourceLoader(this.getClass());
        Resource resource = resourceLoader.getResource("classpath:" + file.getDirectory());
        return resource.getURI().getPath();
    }
}