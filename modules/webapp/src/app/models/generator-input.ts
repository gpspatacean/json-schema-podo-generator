export class GeneratorInput {
    constructor(
        public name: string,
        public payload: object,
        public options?: Map<string, string>
    ) {
    }
}
