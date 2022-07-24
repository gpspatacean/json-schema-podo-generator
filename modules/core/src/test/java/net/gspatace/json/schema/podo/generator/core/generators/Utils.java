package net.gspatace.json.schema.podo.generator.core.generators;

/**
 * Utility class for easier manipulation/interaction
 * of models and members, starting from a
 *
 * @author George Spătăcean
 */
public class Utils {
    /**
     * Returns a certain member from a certain model
     *
     * @param modelData target Model
     * @param memberName name of the target member
     * @return member
     * @throws IllegalAccessError if no such member exists
     */
    public static MemberVariableData getMemberDataByName(final ModelData modelData, final String memberName) {
        return modelData.getMembers().stream().filter(member -> member.getName().equalsIgnoreCase(memberName)).findFirst().orElseThrow(IllegalAccessError::new);
    }

    /**
     * Returns a certain Model from the generator
     *
     * @param generatorData target generator
     * @param modelName name of target model
     * @return the model
     * @throws IllegalAccessError if no such model exists
     */
    public static ModelData getModelByName(final JsonSchemaGenData generatorData, final String modelName) {
        return generatorData.getModels().stream().filter(modelData -> modelData.getModelName().equalsIgnoreCase(modelName)).findFirst().orElseThrow(IllegalAccessError::new);
    }
}
