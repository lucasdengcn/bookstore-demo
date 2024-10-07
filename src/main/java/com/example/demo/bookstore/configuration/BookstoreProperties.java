package com.example.demo.bookstore.configuration;

import lombok.Data;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Data
public class BookstoreProperties {

    @Data
    public static class FileProperties {
        private String directory;
    }

    FileProperties file;


    public String getRelativePath() throws IOException {
        ResourceLoader resourceLoader = new ClassRelativeResourceLoader(this.getClass());
        Resource resource = resourceLoader.getResource("classpath:" + file.getDirectory());
        return resource.getURI().getPath();
    }
}
