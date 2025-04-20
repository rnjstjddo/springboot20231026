package org.example.springboot231026.security.config;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebBCryptConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder(){
        //System.out.println("security-config클래스 WebBCryptConfiguration PasswordEncoder 객체반환 -순환참조문제로 분리시킴");
        return new BCryptPasswordEncoder();
    }
}
