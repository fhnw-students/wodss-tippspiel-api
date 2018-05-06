package ch.fhnw.edu.wodss.tippspielapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:/application-${spring.profiles.active}.properties")
public class AppConfig {

}
