package net.gspatace.json.schema.podo.generator.specification.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.ArrayItems;
import net.gspatace.json.schema.podo.generator.specification.models.Properties;
import net.gspatace.json.schema.podo.generator.specification.models.Property;

import java.io.IOException;
import java.util.*;

/**
 * Deserializer for a "property", which is a child node of the
 * "properties" node of an Object, as specified by JSON Schema
 *
 * @author George Spătăcean
 */
@Slf4j
public class PropertyDeserializer extends JsonDeserializer<Property> {
    @Override
    public Property deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode node = jsonParser.readValueAsTree();
        if (node.isObject()) {
            final Iterator<Map.Entry<String, JsonNode>> fieldIterator = node.fields();
            if (fieldIterator.hasNext()) {
                Map.Entry<String, JsonNode> property = fieldIterator.next();

                //if at this point there is another element, throw, this is bad
                if (fieldIterator.hasNext()) {
                    throw new IOException("Yes, Rico, Ka-Boom!");
                }
                return getPropertyFromJson(property);
            }
        }
        log.debug("No property built for JSON Node [{}]", node);
        return Property.builder().build();
    }

    /**
     * Returns a usable {@link Property} object obtained
     * from a JSON Schema property node.
     *
     * @param property the property name and node
     * @return usable Property object
     */
    private Property getPropertyFromJson(Map.Entry<String, JsonNode> property) {
        final String propertyName = property.getKey();
        final JsonNode propBody = property.getValue();
        final String propDesc = Optional.ofNullable(propBody.get("description")).map(JsonNode::asText).orElse("");
        final String propType = Optional.ofNullable(propBody.get("type")).map(JsonNode::asText).orElse("");

        final JsonDataTypes type = !propType.isEmpty() ?
                JsonDataTypes.valueOf(propType.toUpperCase(Locale.ROOT)) : JsonDataTypes.NOT_SET;

        final Optional<ArrayItems> items = getOptionalComplexProperty(propBody, "items", ArrayItems.class);
        final Optional<Properties> properties = getOptionalComplexProperty(propBody, "properties", Properties.class);

        final JsonNode requiredFieldsNode = propBody.get("required");
        final List<String> required = requiredFieldsNode != null ?
                getRequiredFields(requiredFieldsNode) : new ArrayList<>();

        return Property.builder()
                .propertyName(propertyName)
                .description(propDesc)
                .type(type)
                .items(items)
                .properties(properties)
                .required(required)
                .build();
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
    private <T> Optional<T> getOptionalComplexProperty(final JsonNode jsonNode, final String propertyName, final Class<T> propertyClass) {
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

    /**
     * Retrieves the elements in the JSON "required" array
     * as a list of strings
     *
     * @param requiredNode "required" JSON Node which is an array
     * @return list with names of required properties
     */
    private List<String> getRequiredFields(final JsonNode requiredNode) {
        final List<String> requiredFields = new ArrayList<>();
        requiredNode.elements().forEachRemaining(jsonNode -> requiredFields.add(jsonNode.asText()));
        return requiredFields;
    }
}
