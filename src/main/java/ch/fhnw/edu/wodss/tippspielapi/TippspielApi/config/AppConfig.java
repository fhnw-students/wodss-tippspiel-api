package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application-${spring.profiles.active}.properties")
public class AppConfig {

}
