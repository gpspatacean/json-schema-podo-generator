package net.gspatace.json.schema.podo.generator.templating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a template that is being registered and will be
 * executed against the built context for a given generator.
 *
 * @author George Spătăcean
 */
@Getter
@Setter
@AllArgsConstructor
public class TemplateFile {
    private String templateName;
    private String fileExtension;
}
