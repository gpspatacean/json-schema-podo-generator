package net.gspatace.json.schema.podo.generator.specification.deserializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author George Spătăcean
 */
@Slf4j
public class DeserializeUtils {
    private DeserializeUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns an optional complex Object that is serialized
     * as a standalone object from its respective JSON Node
     *
     * @param jsonNode      node that contains the desired information
     * @param propertyName  name of the target property
     * @param propertyClass Class of the object
     * @param <T>           Class of the object
     * @return optional Complex object
     */
    public static <T> Optional<T> getOptionalComplexProperty(final JsonNode jsonNode, final String propertyName, final Class<T> propertyClass) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        final Optional<String> propertyAsString = Optional.ofNullable(jsonNode.get(propertyName)).map(JsonNode::toString);
        if (propertyAsString.isPresent()) {
            try {
                return Optional.of(objectMapper.readValue(propertyAsString.get(), propertyClass));
            } catch (JsonProcessingException ex) {
                log.error("Failed converting [{}] to object of type `{}`", propertyAsString.get(), propertyClass, ex);
            }
        }
        return Optional.empty();
    }
}
