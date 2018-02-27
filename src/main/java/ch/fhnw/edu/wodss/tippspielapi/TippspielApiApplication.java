package ch.fhnw.edu.wodss.tippspielapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class TippspielApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TippspielApiApplication.class, args);
	}
}
