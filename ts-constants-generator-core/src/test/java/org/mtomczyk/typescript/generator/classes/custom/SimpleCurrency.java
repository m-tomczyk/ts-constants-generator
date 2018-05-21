package org.mtomczyk.typescript.generator.classes.custom;

public class SimpleCurrency {
    private final int whole;
    private final int decimal;

    private SimpleCurrency(int whole, int decimal) {
        this.whole = whole;
        this.decimal = decimal;
    }

    static SimpleCurrency of(final int whole, final int decimal) {
        return new SimpleCurrency(whole, decimal);
    }

    public int getWhole() {
        return whole;
    }

    public int getDecimal() {
        return decimal;
    }
}
