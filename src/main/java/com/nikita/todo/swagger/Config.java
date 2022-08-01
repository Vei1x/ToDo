package com.nikita.todo.swagger;

import springfox.documentation.service.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Config {

    @Bean
    public Docket swaggerConfiguration() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/error").negate())
                .build();
        docket.useDefaultResponseMessages(false);
        return appendTags(docket);

    }

    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag("ToDo list project",
                        "Used to get, create, update and delete todo tasks"));
    }
    }

