package net.gspatace.json.schema.podo.generator.webapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Property Holder for specific Webapp properties
 * from standard Spring `application.yaml` file.
 *
 * @author George Spătăcean
 */
@Component
@ConfigurationProperties(prefix = "webapp")
@Getter
public class WebappProperties {

    @Value("${webapp.backend-location}")
    private String backendLocation;

    public String toJson() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
