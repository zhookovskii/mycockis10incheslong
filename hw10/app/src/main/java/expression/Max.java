package expression;

import java.math.BigDecimal;

public class Max extends AbstractOperation implements NewExpression {

    public Max(NewExpression first, NewExpression second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x) {
        return Math.max(first.evaluate(x), second.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return Math.max(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return toString("max");
    }

    @Override
    public double evaluate(double x, double y, double z) {
        return 0;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x, BigDecimal y, BigDecimal z) {
        return null;
    }
}
