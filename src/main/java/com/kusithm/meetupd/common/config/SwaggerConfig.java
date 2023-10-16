package com.kusithm.meetupd.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("큐시즘 28기 Wanteam API Document")
                .description("큐시즘 28기 Wanteam Server")
                .version("1.0.0");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }



}
