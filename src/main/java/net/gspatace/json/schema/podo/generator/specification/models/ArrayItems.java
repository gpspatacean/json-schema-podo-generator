package net.gspatace.json.schema.podo.generator.specification.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;

/**
 * POJO that holds information of the items, in case
 * the property is of array type
 *
 * @author George Spătăcean
 */
@Data
@Builder
public class ArrayItems {
    private JsonDataTypes type;

    @JsonCreator
    public ArrayItems(@JsonProperty("type") JsonDataTypes type) {
        this.type = type;
    }
}
