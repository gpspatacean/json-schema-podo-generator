package net.gspatace.json.schema.podo.generator.specification.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import net.gspatace.json.schema.podo.generator.specification.deserializers.PropertiesDeserializer;
import net.gspatace.json.schema.podo.generator.specification.serializers.PropertiesSerializer;

import java.util.List;

@Data
@JsonSerialize(using = PropertiesSerializer.class)
@JsonDeserialize(using = PropertiesDeserializer.class)
public class Properties {
    private List<Property> propertyList;
}
