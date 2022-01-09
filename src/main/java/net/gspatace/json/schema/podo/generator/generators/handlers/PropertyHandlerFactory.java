package net.gspatace.json.schema.podo.generator.generators.handlers;

import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.generators.JsonTypePropertyHandler;
import net.gspatace.json.schema.podo.generator.specification.models.Property;

/**
 * @author George Spătăcean
 *
 * Factory that returns concrete implementations of Handlers,
 * based on JSON Schema property types.
 */
@Slf4j
public class PropertyHandlerFactory {
    private PropertyHandlerFactory() {
        throw new IllegalStateException("Factory method class");
    }

    /**
     * Factory method that returns a concrete implementation of {@link JsonTypePropertyHandler},
     * based on the type of the passed property
     *
     * @param property target property for which a handler is requested
     * @return concrete implementation
     */
    public static JsonTypePropertyHandler getForProperty(final Property property) {
        switch (property.getType()) {
            case BOOLEAN:
            case INTEGER:
            case NUMBER:
            case STRING:
                return new SimplePropertyHandler();
            case ARRAY:
                return new ArrayPropertyHandler();
            case OBJECT:
                return new ObjectPropertyHandler();
            case NOT_SET:
            case NULL: {
                log.error("Property {} has incorrect type {}", property.getPropertyName(), property.getType().toString());
            }
        }

        throw new IllegalStateException("Property with invalid type.");
    }
}
