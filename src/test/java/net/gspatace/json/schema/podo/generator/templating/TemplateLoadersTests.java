package net.gspatace.json.schema.podo.generator.templating;

import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;
import net.gspatace.json.schema.podo.generator.templating.loaders.EmbeddedTemplateLoader;
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
