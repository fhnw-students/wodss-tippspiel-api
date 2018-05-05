package ch.fhnw.edu.wodss.tippspielapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class TippspielApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TippspielApiApplication.class, args);
    }
}
