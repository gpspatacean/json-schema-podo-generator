package net.gspatace.json.schema.podo.generator.core.templating;

import net.gspatace.json.schema.podo.generator.core.templating.loaders.EmbeddedTemplateLoader;
import net.gspatace.json.schema.podo.generator.core.templating.interfaces.TemplateLoader;
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
