# Plain Old Data Objects generator from JSON Schemas

## Overview
Code generation tool that generates Plain Old Data Objects with Serialization/Deserialization capabilities.
This is a framework that uses [mustache](https://mustache.github.io/) templating, using [this](https://github.com/samskivert/jmustache) 
java library. Starting from a [JSON Schema](https://json-schema.org/) input file, it will generate models that can be
further used for interacting with a JSON that matches the specified schema. Heavily inspired from [OpenAPI generator](https://github.com/OpenAPITools/openapi-generator).

It is build as a framework, and new languages can be added. This will be detailed separately.

## Getting started
### Basic usage
Run `java -jar target/json-schema-podo-generator-0.0.1-SNAPSHOT.jar generate -g cpp -i <schema/input/file.json> -o <output/directory>`.
This will generate the models using `cpp` generator.

Also see `java -jar target/json-schema-podo-generator-0.0.1-SNAPSHOT.jar help`

### Advanced features
Generator specific properties are supported. The available custom properties of a generator can be seen by running
`java -jar .\target\json-schema-podo-generator-0.0.1-SNAPSHOT.jar config-help -g cpp`

For instance, `java -jar target/json-schema-podo-generator-0.0.1-SNAPSHOT.jar generate -g cpp -i <schema/input/file.json> -o <output/directory> -genprops -ns=my_namespace,-l=mylib`
will generate the models in the `my_namespace` namespace, and build `mylib` module.

### Samples
Samples can be found in [samples](samples) directory.

## Authors
* George Spătăcean <george.spatacean@gmail.com>
