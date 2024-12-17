package Capstone.Third.api.kis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kis")
@Data
public class KisConfig {

    private String appKey;
    private String appSecret;

}
