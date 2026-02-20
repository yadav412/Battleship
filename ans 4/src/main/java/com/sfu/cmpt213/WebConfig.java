package com.sfu.cmpt213;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Spring configuration class for web resources.
 * Configures static resource handling to serve files from the /public folder.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Serve files from /public folder in project root
        String publicPath = new File("public").getAbsolutePath();
        registry.addResourceHandler("/**")
                .addResourceLocations("file:" + publicPath + "/", "classpath:/public/");
    }
}
