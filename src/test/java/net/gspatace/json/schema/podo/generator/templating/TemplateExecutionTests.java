package net.gspatace.json.schema.podo.generator.templating;

import lombok.AllArgsConstructor;
import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;
import net.gspatace.json.schema.podo.generator.templating.loaders.EmbeddedTemplateLoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author George Spătăcean
 */
public class TemplateExecutionTests {

    @AllArgsConstructor
    static class SimpleClassTestBed {
        public final int age;
        public final String name;
        public final String occupation;
    }

    @Test
    public void executeSimpleTemplate() {
        final TemplateLoader templateLoader = new EmbeddedTemplateLoader("templates");
        final TemplateManager templateManager = new TemplateManager(templateLoader);
        final SimpleClassTestBed testBed = new SimpleClassTestBed(31, "George", "Software Engineer");
        final String expectedResult = "Hello, my name is George, I am a 31 years old Software Engineer.";
        final String actualResult = templateManager.executeTemplate("testBed.mustache", testBed);
        assertEquals(expectedResult,actualResult);
    }
}
