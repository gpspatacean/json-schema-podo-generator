package com.spatacean.json.schema.podo.generator.core.generators;

import lombok.Builder;
import lombok.Data;
import com.spatacean.json.schema.podo.generator.core.specification.JsonDataTypes;

import java.util.Optional;

/**
 * Generic Data Holder that will be used as context in the templating engine.
 * It represents a JSON property with details from both JSON Schema, and from
 * inferring and parsing of the JSON Schema.
 * It must be populated with all the information required for a certain
 * language template.
 *
 * @author George Spătăcean
 */
@Data
@Builder
public class MemberVariableData {
    private String name;
    private String description;
    private JsonDataTypes jsonDataTypes;
    private boolean isPrimitive;
    private boolean isArray;
    private String dataType;
    private String getterName;
    private String setterName;
    @Builder.Default
    private Optional<ModelData> innerModel = Optional.empty();

    public String getTypeAsString() {
        return jsonDataTypes.toString();
    }

    @SuppressWarnings("unused") //May be used directly in templates
    public boolean isString() {
        return JsonDataTypes.STRING == jsonDataTypes;
    }

    @SuppressWarnings("unused") //May be used directly in templates
    public boolean isInteger() {
        return JsonDataTypes.INTEGER == jsonDataTypes;
    }

    @SuppressWarnings("unused") //May be used directly in templates
    public boolean isNumber() {
        return JsonDataTypes.NUMBER == jsonDataTypes;
    }

    @SuppressWarnings("unused") //May be used directly in templates
    public boolean isBoolean() {
        return JsonDataTypes.BOOLEAN == jsonDataTypes;
    }

    @SuppressWarnings("unused") //May be used directly in templates
    public boolean isObject() {
        return JsonDataTypes.OBJECT == jsonDataTypes;
    }

    @SuppressWarnings("unused") //May be used directly in templates
    public boolean isSimpleArray() {
        return JsonDataTypes.ARRAY == jsonDataTypes &&
                !innerModel.isPresent();
    }

    @SuppressWarnings("unused") //May be used directly in templates
    public boolean isComplexArray() {
        return JsonDataTypes.ARRAY == jsonDataTypes &&
                innerModel.isPresent();
    }
}
