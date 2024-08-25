package com.spatacean.json.schema.podo.generator.rest.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI apiDetails(@Value("${project.properties.name}") final String name,
                              @Value("${project.properties.version}") final String version,
                              @Value("${project.properties.description}") final String description) {

        return new OpenAPI()
                .info(new Info()
                        .title(name)
                        .description(description)
                        .version(version))
                .externalDocs(new ExternalDocumentation()
                        .description("More info on GitHub!")
                        .url("https://github.com/gspatace/json-schema-podo-generator")
                );
    }
}
