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

@Data
@Builder
@JsonSerialize(using = PropertySerializer.class)
@JsonDeserialize(using = PropertyDeserializer.class)
public class Property {
    private String propertyName;
    private String description;
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
