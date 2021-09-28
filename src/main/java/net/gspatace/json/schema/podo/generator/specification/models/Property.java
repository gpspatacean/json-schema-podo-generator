package net.gspatace.json.schema.podo.generator.specification.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import net.gspatace.json.schema.podo.generator.specification.deserializers.PropertyDeserializer;
import net.gspatace.json.schema.podo.generator.specification.serializers.PropertySerializer;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;

/**
 * POJO that represents a property of an Object
 * as described by the JSON Schema
 *
 * @author George Spătăcean
 */
@Data
@Builder
@JsonSerialize(using = PropertySerializer.class)
@JsonDeserialize(using = PropertyDeserializer.class)
public class Property {

    /**
     * The name of the property, as it will appear in the
     * generated code.
     */
    private String propertyName;

    /**
     * The description of the property, useful for
     * human interpretation
     */
    private String description;

    /**
     * The type of the property, as regulated by
     * JSON Schema Specification
     *
     * Please see {@link JsonDataTypes} for the list
     * of the values
     */
    private JsonDataTypes type;

    @JsonCreator
    public Property(@JsonProperty("propertyName") String propertyName,
                    @JsonProperty("description") String description,
                    @JsonProperty("type") JsonDataTypes type) {
        this.propertyName = propertyName;
        this.description = description;
        this.type = type;
    }
}
