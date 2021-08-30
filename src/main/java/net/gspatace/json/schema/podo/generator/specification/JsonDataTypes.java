package net.gspatace.json.schema.podo.generator.specification;

public enum JsonDataTypes {
    NOT_SET(""),
    ARRAY("array"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    NULL("null"),
    NUMBER("number"),
    OBJECT("object"),
    STRING("string");

    private final String stringValue;

    JsonDataTypes() {
        stringValue = "";
    }

    JsonDataTypes(String value) {
        stringValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
