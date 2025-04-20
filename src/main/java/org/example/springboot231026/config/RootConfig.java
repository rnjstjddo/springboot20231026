package org.example.springboot231026.config;

import com.querydsl.core.annotations.Config;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper() {
        System.out.println("config클래스 RootConfig getMapper() 반환타입 ModelMapper 반환");
        ModelMapper mm = new ModelMapper();
        mm.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
            return mm;
    }


}
