package net.gspatace.json.schema.podo.generator.core.base;

import net.gspatace.json.schema.podo.generator.core.generators.MemberVariableData;

/**
 * @author George Spătăcean
 */
@FunctionalInterface
public interface CollectionTypeBuilder {
    /**
     * For a given member, if it is a collection, return the language specific construct.
     * This can be C++ templates, Java/C# generics, etc.
     *
     * For instance, for C++, the representation of a collection of "Product"s, would translate to
     * {@code std::vector<Product> products{};}
     *
     * @param memberVariableData the property that is a collection
     * @return String containing the language specific construct.
     */
    String getCollectionDataType(final MemberVariableData memberVariableData);
}
