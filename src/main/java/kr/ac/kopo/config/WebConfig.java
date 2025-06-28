package kr.ac.kopo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
          .addMapping("/**")
          .allowedOriginPatterns("http://localhost:8888", "http://my.domain.com")
          .allowCredentials(true)
          .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 파일시스템의 uploads 폴더를 /uploads/** 로 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
    
    
}
