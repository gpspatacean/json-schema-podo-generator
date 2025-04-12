package com.spatacean.json.schema.podo.generator.core.templating;

import com.spatacean.json.schema.podo.generator.core.templating.loaders.EmbeddedTemplateLoader;
import com.spatacean.json.schema.podo.generator.core.templating.interfaces.TemplateLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for template loaders.
 *
 * @author George Spătăcean
 */
class TemplateLoadersTests {

    @Test
    void readEmbeddedResource() {
        final TemplateLoader templateLoader = new EmbeddedTemplateLoader("subdir");
        final String content = templateLoader.loadTemplate("simple.file");
        assertEquals("test", content);
    }
}
