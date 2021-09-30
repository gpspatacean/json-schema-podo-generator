package net.gspatace.json.schema.podo.generator.templating;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import net.gspatace.json.schema.podo.generator.templating.exceptions.TemplateExecutionException;
import net.gspatace.json.schema.podo.generator.templating.exceptions.TemplateNotLoadedException;
import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * This class uses loads (if not present) then executes compiled
 * Mustache templates, using a configured {@link TemplateLoader}
 *
 * @author George Spătăcean
 */
public class TemplateManager {
    private final Mustache.Compiler mustacheCompiler = Mustache.compiler();
    private final TemplateLoader templateLoader;
    private final Map<String, Template> compiledTemplates = new HashMap<>();

    /**
     * Public Constructor
     *
     * @param templateLoader handler for loading templates
     */
    public TemplateManager(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    /**
     * Executes a compiled template against the given context
     *
     * @param templateName the name of the target template
     * @param context      Object that can be used by the
     *                     underlying Mustache Engine.
     * @return the final populated file.
     * @throws TemplateExecutionException if the target template cannot be executed
     */
    public String executeTemplate(final String templateName, final Object context) {
        Template tmpl = compiledTemplates.computeIfAbsent(templateName, key -> {
            try {
                String template = templateLoader.loadTemplate(key);
                return mustacheCompiler.compile(template);
            } catch (TemplateNotLoadedException e) {
                throw new TemplateExecutionException("Failed to execute template.", e);
            }
        });

        return tmpl.execute(context);
    }
}
