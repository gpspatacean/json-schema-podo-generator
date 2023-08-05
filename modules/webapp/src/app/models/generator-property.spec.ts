import { GeneratorProperty } from './generator-property';

describe('GeneratorProperty', () => {
  it('should create an instance', () => {
    expect(new GeneratorProperty("testGenerator", "testDescription", "testDefaultValue")).toBeTruthy();
  });
});
