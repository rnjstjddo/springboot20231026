package org.example.springboot231026;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        System.out.println("메인Application run() 진입");
        SpringApplication.run(Application.class, args);
    }

}
