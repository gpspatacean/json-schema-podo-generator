package com.spatacean.json.schema.podo.generator.core.services;

import com.spatacean.json.schema.podo.generator.core.annotations.CustomProperties;
import com.spatacean.json.schema.podo.generator.core.annotations.SchemaGenerator;
import com.spatacean.json.schema.podo.generator.core.base.AbstractGenerator;
import com.spatacean.json.schema.podo.generator.core.base.GeneratorInput;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton that retrieves registered generators
 * and provides basic interaction with them, such as
 * <ul>
 *     <li> retrieval of registered generators</li>
 *     <li> instantiation of specific generator</li>
 *     <li> retrieval of generator specific custom options objects</li>
 *     <li> retrieval of details for all the specific options</li>
 * </ul>
 * <p>
 * Uses reflection
 *
 * @author George Spătăcean
 */
@Slf4j
public class GeneratorsHandler {
    private static final String GENERATOR_PACKAGE = "com.spatacean.json.schema.podo.generator.core.langs";
    private final Map<String, Class<?>> loadedGenerators = new ConcurrentHashMap<>();

    /**
     * List containing the descriptions of all the loaded generators
     */
    private final List<GeneratorDescription> generatorDescriptions = new ArrayList<>();

    /**
     * Private constructor
     * Uses reflection to parse all generators annotated with {@link SchemaGenerator}
     * and populates internal list of generators to be used further
     */
    private GeneratorsHandler() {
        final ClassGraph classGraph = new ClassGraph().enableAllInfo().acceptPackages(GENERATOR_PACKAGE);
        try (ScanResult scanResult = classGraph.scan()) {
            ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(SchemaGenerator.class);
            final List<Class<?>> generatorClasses = classInfoList.loadClasses();
            generatorClasses.forEach(clazz -> {
                final SchemaGenerator annotation = clazz.getAnnotation(SchemaGenerator.class);
                loadedGenerators.put(annotation.name(), clazz);
                log.trace("Found `{}` as a registered generator.", annotation.name());
            });
        }

        loadedGenerators.forEach((genName, genClazz) -> {
            final SchemaGenerator annotation = genClazz.getAnnotation(SchemaGenerator.class);
            generatorDescriptions.add(new GeneratorDescription(annotation.name(), annotation.description()));
            log.trace("Added generator description for `{}` generator", annotation.name());
        });
    }

    /**
     * Singleton accessor.
     *
     * @return a handler to the sole {@link GeneratorsHandler} instance.
     */
    public static GeneratorsHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Returns the list of descriptions for each registered generator
     * by retrieving everything annotated with {@link SchemaGenerator}
     *
     * @return the list
     */
    public List<GeneratorDescription> getAvailableGenerators() {
        return generatorDescriptions;
    }

    /**
     * Returns the description of a certain generator
     *
     * @param generator the target generator
     * @return the description
     */
    public GeneratorDescription getGeneratorDescription(final String generator) {
        return generatorDescriptions
                .stream()
                .filter(description -> description.name().equals(generator))
                .findFirst()
                .orElseThrow(() -> new GeneratorNotFoundException(String.format("Generator '%s' not found", generator)));
    }

    /**
     * Returns a Set of all the specific options of the given generator
     *
     * @param generatorName target generator for which options to be returned
     * @return Set containing all the options
     * @throws GeneratorNotFoundException          if the given generator was not found
     * @throws CustomOptionsInstantiationException if the {@link CustomProperties}
     *                                             object cannot be instantiated
     */
    public Set<OptionDescription> getSpecificGeneratorOptions(final String generatorName) {
        final Set<OptionDescription> options = new HashSet<>();
        final Optional<Object> generatorInstance = GeneratorsHandler.getInstance().getCustomOptionsCommand(generatorName);
        generatorInstance.ifPresent(theInstance -> {
            final CommandLine cmd = new CommandLine(theInstance);
            for (final CommandLine.Model.OptionSpec option : cmd.getCommandSpec().options()) {
                final OptionDescription optionDescription = OptionDescription.builder()
                        .name(String.join(";", option.names()))
                        .defaultValue(option.defaultValue())
                        .description(String.join(";", option.description()))
                        .build();
                options.add(optionDescription);
            }
        });

        return options;
    }

    /**
     * Instantiates a concrete generator that can be used
     * See cli commands.GenerateCommand#run()
     *
     * @param generatorInput input required by a generator
     * @return the concrete generator
     * @throws GeneratorNotFoundException      in case no such generator has been found
     * @throws GeneratorInstantiationException in case of instantiation failures.
     */
    public AbstractGenerator getGeneratorInstance(final GeneratorInput generatorInput) {
        if (!loadedGenerators.containsKey(generatorInput.getGeneratorName())) {
            final String errMsg = String.format("Generator `%s` was not found.", generatorInput.getGeneratorName());
            throw new GeneratorNotFoundException(errMsg);
        }

        final Class<?> clazz = loadedGenerators.get(generatorInput.getGeneratorName());
        try {
            final Constructor<?> constructor = clazz.getConstructor(GeneratorInput.class);
            return (AbstractGenerator) constructor.newInstance(generatorInput);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            final String errMsg = String.format("Failed to instantiate generator '%s'.", generatorInput.getGeneratorName());
            throw new GeneratorInstantiationException(errMsg, ex);
        }
    }

    /**
     * Retrieves an optional instance of a generator`s custom properties,
     * by searching a generator class for an inner class annotated with {@link CustomProperties}.
     * Used in cli commands.CustomConfigHelper#run()
     *
     * @param targetGenerator name of the generator
     * @return Optional instance of the custom properties
     * @throws GeneratorNotFoundException          if the given generator was not found
     * @throws CustomOptionsInstantiationException for instantiation failures of the
     *                                             {@link CustomProperties} pertaining object.
     */
    public Optional<Object> getCustomOptionsCommand(final String targetGenerator) {
        if (!loadedGenerators.containsKey(targetGenerator)) {
            throw new GeneratorNotFoundException(String.format("Generator '%s' was not found.", targetGenerator));
        }

        final Optional<Class<?>> optionsClass = getCustomPropertiesClass(targetGenerator);
        if (optionsClass.isPresent()) {
            try {
                final Constructor<?> constructor = optionsClass.get().getConstructor();
                return Optional.of(constructor.newInstance());
            } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException |
                           InvocationTargetException ex) {
                final String failureMessage = String.format("Failed to instantiate Custom Properties object of class `%s` for generator `%s`", optionsClass.get().getSimpleName(), targetGenerator);
                throw new CustomOptionsInstantiationException(failureMessage, ex);
            }
        }
        log.debug("CustomProperties class for generator `{}` not found", targetGenerator);
        return Optional.empty();
    }

    /**
     * Retrieves the {@link CustomProperties} annotated class of a generator
     *
     * @param generatorName the name for which the properties class is searched
     * @return Optional class of the Custom properties class
     */
    private Optional<Class<?>> getCustomPropertiesClass(final String generatorName) {
        final Class<?> generatorClazz = loadedGenerators.get(generatorName);
        final Class<?>[] declaredClasses = generatorClazz.getDeclaredClasses();
        return Arrays
                .stream(declaredClasses)
                .filter(clazz -> clazz.isAnnotationPresent(CustomProperties.class))
                .findFirst();
    }

    /**
     * Simple Singleton Holder of the parent class
     *
     * @author George Spătăcean
     */
    private static class SingletonHolder {
        private static final GeneratorsHandler INSTANCE = new GeneratorsHandler();
    }
}
