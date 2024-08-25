package com.spatacean.json.schema.podo.generator.core.specification.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.spatacean.json.schema.podo.generator.core.specification.JsonDataTypes;

import java.util.List;

/**
 * POJO of the JSON Schema representation of an object.
 * This is the root object that holds information from a
 * given JSON Schema
 *
 * @author George Spătăcean
 */
@Data
public class JsonSchema {

    @JsonProperty("$schema")
    private String schema;
    @JsonProperty("$id")
    private String id;
    private String title;
    private String description;
    private JsonDataTypes type;
    private Properties properties;
    private List<String> required;
}
