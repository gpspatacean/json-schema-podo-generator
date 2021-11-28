package net.gspatace.json.schema.podo.generator.generators;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Holder of information pertaining to a Model, such as its name
 * and list of its members. Will be used as context in the
 * templating engine.
 *
 * @author George Spătăcean
 */
@Data
@Builder
public class ModelData {
    private String modelName;
    private List<MemberVariableData> members;
}
