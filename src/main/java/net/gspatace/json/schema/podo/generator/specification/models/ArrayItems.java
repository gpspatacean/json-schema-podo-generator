package net.gspatace.json.schema.podo.generator.specification.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.deserializers.ItemsDeserializer;

import java.util.Optional;

/**
 * POJO that holds information of the items, in case
 * the property is of array type
 *
 * @author George Spătăcean
 */
@Data
@Builder
@JsonDeserialize(using = ItemsDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ArrayItems {
    private JsonDataTypes type;
    /**
     * In case this property is an object itself, these are its properties
     */
    @Builder.Default
    private Optional<Properties> properties = Optional.empty();

    @JsonCreator
    public ArrayItems(@JsonProperty("type") JsonDataTypes type,
                      @JsonProperty("properties") Optional<Properties> properties) {
        this.type = type;
        this.properties = properties;
    }
}
