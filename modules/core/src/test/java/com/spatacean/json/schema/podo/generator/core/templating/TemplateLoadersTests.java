package com.spatacean.json.schema.podo.generator.core.templating;

import com.spatacean.json.schema.podo.generator.core.templating.loaders.EmbeddedTemplateLoader;
import com.spatacean.json.schema.podo.generator.core.templating.interfaces.TemplateLoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TemplateLoadersTests {

    @Test
    public void readEmbeddedResource(){
        final TemplateLoader templateLoader = new EmbeddedTemplateLoader("subdir");
        final String content = templateLoader.loadTemplate("simple.file");
        assertEquals("test", content);
    }
}
