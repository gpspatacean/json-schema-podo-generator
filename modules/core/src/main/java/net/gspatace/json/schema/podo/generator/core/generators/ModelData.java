package net.gspatace.json.schema.podo.generator.core.generators;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

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
    /**
     * Name of the model
     */
    private String modelName;

    /**
     * List with all the {@link MemberVariableData} this model has
     */
    private List<MemberVariableData> members;

    /**
     * Formatter of a dependency of this model. To be injected by the abstract
     * generation logic, with a custom generator implementation
     */
    @Builder.Default
    private UnaryOperator<String> dependencyFormatter = value -> value;

    /**
     * Additional generic properties that can be set by
     * concrete generator implementations and then be used
     * in Model templates.
     */
    @Builder.Default
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * Retrieve the dependencies of this module.
     * Used directly in the mustache templates
     *
     * @return List of maps containing a single identical key (used in
     * mustache templates )
     */
    public List<Map<String, String>> getDependencies() {
        final List<Map<String, String>> dependencies = new ArrayList<>();
        members.stream().forEach(memberVariableData -> {
            memberVariableData.getInnerModel().ifPresent(modelData -> {
                final Map<String, String> dependency = new HashMap<>();
                final String dependencyName = dependencyFormatter.apply(StringUtils.capitalize(modelData.getModelName()));
                dependency.put("dependency", dependencyName);
                dependencies.add(dependency);
            });
        });

        return dependencies;
    }

    /**
     * @return true if this model has any arrays, false otherwise
     */
    @SuppressWarnings("unused") // may be used directly from templates
    public boolean hasArrays() {
        return members.stream().anyMatch(MemberVariableData::isArray);
    }
}
