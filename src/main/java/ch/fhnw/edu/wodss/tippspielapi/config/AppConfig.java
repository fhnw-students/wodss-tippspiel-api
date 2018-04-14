package ch.fhnw.edu.wodss.tippspielapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application-${spring.profiles.active}.properties")
public class AppConfig {

    @Value("${env.name}")
    public String envName;

    @Bean
    public Worker getEnvName() {
        return new Worker(this.envName);
    }

    class Worker {
        public String envName;
        Worker(String envName){
            this.envName = envName;
        }

        public String print(){
            return this.envName;
        }
    }

    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Worker w = context.getBean(Worker.class);
        System.out.println("test" + w.print());
    }

}
