import { Generator } from './generator';

describe('Generator', () => {
  it('should create an instance', () => {
    expect(new Generator("name", "description")).toBeTruthy();
  });
});
