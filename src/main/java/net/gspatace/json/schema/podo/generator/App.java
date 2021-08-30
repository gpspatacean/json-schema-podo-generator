package net.gspatace.json.schema.podo.generator;

import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        JsonDataTypes type = JsonDataTypes.ARRAY;
        String str = type.toString();
        String s = JsonDataTypes.INTEGER.toString();
        JsonDataTypes type2 = JsonDataTypes.valueOf("BOOLEAN");
    }
}
