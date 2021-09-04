package net.gspatace.json.schema.podo.generator.templating;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import net.gspatace.json.schema.podo.generator.templating.exceptions.TemplateExecutionException;
import net.gspatace.json.schema.podo.generator.templating.exceptions.TemplateNotLoadedException;
import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;

import java.util.HashMap;
import java.util.Map;

public class TemplateManager {
    private final Mustache.Compiler mustacheCompiler = Mustache.compiler();
    private final TemplateLoader templateLoader;
    private final Map<String, Template> compiledTemplates = new HashMap<>();

    public TemplateManager(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

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
