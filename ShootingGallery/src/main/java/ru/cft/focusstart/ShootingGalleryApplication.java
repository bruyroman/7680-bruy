package ru.cft.focusstart;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ShootingGalleryApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ShootingGalleryApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}