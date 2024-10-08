package com.spatacean.json.schema.podo.generator.core.specification.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spatacean.json.schema.podo.generator.core.specification.deserializers.PropertiesDeserializer;
import com.spatacean.json.schema.podo.generator.core.specification.serializers.PropertiesSerializer;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * POJO class that holds all the properties
 * of an Object, as specified by JSON Schema
 *
 * Please see {@link Property}
 * @author George Spătăcean
 */
@Data
@JsonSerialize(using = PropertiesSerializer.class)
@JsonDeserialize(using = PropertiesDeserializer.class)
public class Properties {

    /**
     * List of the all the properties an Object holds.
     * Please see {@link Property}
     */
    private List<Property> propertyList;

    /**
     * Return a property by name wrapped in an Optional
     * @param propName target property
     * @return the property
     */
    public Optional<Property> getPropertyByName(final String propName) {
        return propertyList.stream()
                .filter(property -> property.getPropertyName().equalsIgnoreCase(propName))
                .findFirst();
    }
}
