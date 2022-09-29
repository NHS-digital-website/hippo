const macroSelectors = {
    card: {
        basic: '#BasicCard',
        button: '#ButtonCard',
        image: '#ImageCard',
        author: '#AuthorCard',
    }
};

export default macroSelectors;
export type MacroSelectors = typeof macroSelectors;
export const isVariant = (macro: keyof MacroSelectors, variant: string): variant is keyof MacroSelectors[keyof MacroSelectors] => Object.keys(macroSelectors[macro]).includes(variant);
