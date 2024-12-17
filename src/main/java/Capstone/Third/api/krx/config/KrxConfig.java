package Capstone.Third.api.krx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "krx")
@Data
public class KrxConfig {

    private String serviceKey;
}
