package hamburg.foo.nim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NimApplication {

    public static void main(String[] args) {
        SpringApplication.run(NimApplication.class, args);
    }

}
