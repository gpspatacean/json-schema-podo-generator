package net.gspatace.json.schema.podo.generator.core.annotations;

import net.gspatace.json.schema.podo.generator.core.services.GeneratorsHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used on a specific code generator.
 * Used for:
 * <ul>
 *     <li> Automated registration of the annotated class
 *          as a code generator </li>
*      <li> Providing generic generator information </li>
 * </ul>
 *
 * Please see {@link GeneratorsHandler}
 *
 * @author George Spătăcean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SchemaGenerator {

    /**
     * The name of the generator, as it will appear in generator listing.
     * @return name
     */
    String name();

    /**
     * Name of the subdirectory of "src/main/resources/" where
     * the Mustache templates for this generator are located.
     * @return location
     */
    String embeddedResourceLocation();

    /**
     * Description of the generator, as it will appear in generator listing.
     * @return description
     */
    String description();
}
