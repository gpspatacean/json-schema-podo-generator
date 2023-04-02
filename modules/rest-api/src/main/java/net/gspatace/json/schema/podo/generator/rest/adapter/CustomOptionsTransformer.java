package net.gspatace.json.schema.podo.generator.rest.adapter;

import net.gspatace.json.schema.podo.generator.core.services.GeneratorsHandler;
import net.gspatace.json.schema.podo.generator.core.services.OptionDescription;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * Helper class for transforming custom options from
 * a POST Request to matching format for the Picocli format.
 *
 * @author George Spătăcean
 */
@Service
public class CustomOptionsTransformer {

    /**
     * Generator Handler reference for ease of access.
     */
    private final GeneratorsHandler genHandler = GeneratorsHandler.getInstance();

    /**
     * Return the corresponding String[] to be used by Picocli, in
     * order to populate specific generator custom options
     *
     * @param generatorName  the generator for which custom options
     *                       are given
     * @param allQueryParams HTTP Request query params to be matched to
     *                       custom options
     * @return String array of the identified options, e.g
     * ["-v","1.0.0-SNAPSHOT","--package","podo.generated"]
     * @see net.gspatace.json.schema.podo.generator.core.base.AbstractGenerator#parseCustomOptionsProperties(Object)
     */
    public String[] getCommandOptions(final String generatorName, final Map<String, String> allQueryParams) {
        List<String> commandString = new ArrayList<>();
        final Set<OptionDescription> optionDescriptions = genHandler.getSpecificGeneratorOptions(generatorName);
        optionDescriptions.parallelStream().forEach(optionDescription -> {
            final String optionName = getOptionParameterName(optionDescription.getName());
            if (allQueryParams.containsKey(optionName)) {
                commandString.add(buildConcreteOptionName(optionDescription.getName(), optionName));
                commandString.add(allQueryParams.get(optionName));
            }
        });
        return commandString.toArray(String[]::new);
    }

    /**
     * Match the recorded name of the custom Option , e.g. "-v;--version"
     * to the name sent as a query parameter ( which is without leading dashes )
     *
     * @param compoundOptionName full name as registered with Picocli @Option
     * @return the cleaned option name.
     * @remark Suppose an option was registered with names = {"-p", "--package"}
     * Algorithm is as follows:
     * <li> split the names based on ";"</li>
     * <li> check if there are long name values registered </li>
     * <ul>
     * <li> if yes, return the first one of those, stripping leading dashes ("package")</li>
     * <li> if no, return the first value, stripping leading dashes ("p)</li>
     * </ul>
     */
    private String getOptionParameterName(@NotNull final String compoundOptionName) {
        final String[] names = compoundOptionName.split(";");
        final Optional<String> longName = Arrays.stream(names).filter(name -> name.startsWith("--")).findFirst();
        final String name = longName.orElseGet(() -> names[0]);
        return StringUtils.stripStart(name, "-");
    }

    /**
     * Add corresponding leading dashes, to match the registered Option name.
     *
     * @param compoundOptionName name of the option, as registered in the generator
     * @param receivedName       name of the query parameter passed to the request.
     * @return a matching option name that can be parsed by Picocli
     */
    private String buildConcreteOptionName(final String compoundOptionName, final String receivedName) {
        return Arrays.stream(compoundOptionName.split(";")).filter(s -> s.contains(receivedName)).findFirst().orElseThrow(InvalidParameterException::new);
    }
}
