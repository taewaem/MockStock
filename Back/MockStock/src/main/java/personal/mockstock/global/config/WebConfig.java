package personal.mockstock.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
public class WebConfig implements WebMvcConfigurer {

    /**
     * CORS 설정 - React 개발 서버와 통신 허용
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")       // Vite 기본 포트
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                  .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // 세션 쿠키 허용


    }

}
