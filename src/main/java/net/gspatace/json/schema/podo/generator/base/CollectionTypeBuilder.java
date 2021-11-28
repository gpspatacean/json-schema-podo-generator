package net.gspatace.json.schema.podo.generator.base;

import net.gspatace.json.schema.podo.generator.generators.MemberVariableData;

/**
 * @author George Spătăcean
 */
@FunctionalInterface
public interface CollectionTypeBuilder {
    /**
     * For a given member, if it is a collection, return the language specific construct.
     * This can be C++ templates, Java/C# generics, etc.
     *
     * For instance, for C++, the representation of a collection of "Product"s, could translate to
     * @<code> std::vector<Product> products{};</code>
     *
     * @param memberVariableData the property that is a collection
     * @return String containing the language specific construct.
     */
    String getCollectionDataType(final MemberVariableData memberVariableData);
}
