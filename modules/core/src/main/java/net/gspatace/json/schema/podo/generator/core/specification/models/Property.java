package net.gspatace.json.schema.podo.generator.core.specification.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import net.gspatace.json.schema.podo.generator.core.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.core.specification.deserializers.PropertyDeserializer;
import net.gspatace.json.schema.podo.generator.core.specification.serializers.PropertySerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * In case the property is an array, this holds information
     * of the objects the array holds
     */
    @Builder.Default private Optional<ArrayItems> items = Optional.empty();

    /**
     * In case this property is an object itself, these are its properties
     */
    @Builder.Default private Optional<Properties> properties = Optional.empty();

    /**
     * In case this property is an object itself, this is a list of
     * properties that are required (mandatory) in the JSON representation
     */
    @Builder.Default private List<String> required = new ArrayList<>();

    @JsonCreator
    public Property(@JsonProperty("propertyName") String propertyName,
                    @JsonProperty("description") String description,
                    @JsonProperty("type") JsonDataTypes type,
                    @JsonProperty("items") Optional<ArrayItems> items,
                    @JsonProperty("properties") Optional<Properties> properties,
                    @JsonProperty("required") List<String> required) {
        this.propertyName = propertyName;
        this.description = description;
        this.type = type;
        this.items = items;
        this.properties = properties;
        this.required = required;
    }
}
