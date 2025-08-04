package personal.mockstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // 추가!
public class MockstockApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockstockApplication.class, args);
    }

}