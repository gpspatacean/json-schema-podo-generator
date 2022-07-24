## Plain Old Data Objects - Java generated sources usage example

### Contents
Multi-module Maven project with:
- [generated](generated) code
- [test-drive](test-drive) simple test app with usage example

Command used to generate contents of [generated](generated) directory:

```java -jar .\modules\cli-wrapper\target\podo-generator-cli-0.0.1-SNAPSHOT.jar generate -g java -i samples\schemas\ComplexSchema.json -o samples\java\generated```

### Build
Run `mvn clean package` - it will build everything and run the simple test.