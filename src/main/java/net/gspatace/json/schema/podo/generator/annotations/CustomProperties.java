package net.gspatace.json.schema.podo.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used on classes that hold generator specific properties.
 * These are usually defined as Inner classes in the parent generator.
 *
 * These properties are listed in the config-help
 * {@link net.gspatace.json.schema.podo.generator.cli.commands.CustomConfigHelper}
 * subcommand, and then <a href="https://picocli.info/">Picocli</a> is
 * used to infer and populate the field values in these properties classes.
 *
 * @author George Spătăcean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomProperties {
}
