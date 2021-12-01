package net.gspatace.json.schema.podo.generator.specification.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.ArrayItems;
import net.gspatace.json.schema.podo.generator.specification.models.Properties;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import static net.gspatace.json.schema.podo.generator.specification.deserializers.DeserializeUtils.getOptionalComplexProperty;

/**
 * Deserializer for "items" node of a property.
 * See also {@link ArrayItems}
 *
 * @author George Spătăcean
 */
@Slf4j
public class ItemsDeserializer extends JsonDeserializer<ArrayItems> {
    @Override
    public ArrayItems deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode node = jsonParser.readValueAsTree();
        if (node.isObject()) {
            final String itemType = node.get("type").asText();
            final JsonDataTypes jsonType = JsonDataTypes.valueOf(itemType.toUpperCase(Locale.ROOT));
            final Optional<Properties> properties = getOptionalComplexProperty(node, "properties", Properties.class);
            return ArrayItems.builder()
                    .type(jsonType)
                    .properties(properties)
                    .build();
        }
        return ArrayItems.builder().build();
    }
}
