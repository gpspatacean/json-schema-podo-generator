import { GeneratorInput } from './generator-input';

describe('GeneratorInput', () => {
  it('should create an instance', () => {
    expect(new GeneratorInput("java", {})).toBeTruthy();
  });
});
