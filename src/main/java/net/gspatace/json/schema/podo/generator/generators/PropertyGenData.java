package net.gspatace.json.schema.podo.generator.generators;

import lombok.Builder;
import lombok.Data;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;

@Data
@Builder
public class PropertyGenData {
    private final String name;
    private final String description;
    private final JsonDataTypes jsonDataTypes;

    public String getTypeAsString() {
        return jsonDataTypes.toString();
    }
}
