package expression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Divide extends AbstractOperation implements NewExpression {

    public Divide(NewExpression first, NewExpression second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x) {
        return first.evaluate(x) / second.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return first.evaluate(x, y, z) / second.evaluate(x, y, z);
    }

    public double evaluate(double x, double y, double z) {
        if (second.evaluate(x, y, z) == 0D) {
            throw new IllegalArgumentException();
        }
        return first.evaluate(x, y, z) / second.evaluate(x, y, z);
    }

    @Override
    public BigDecimal evaluate(BigDecimal x, BigDecimal y, BigDecimal z) {
        return first.evaluate(x, y, z).divide(second.evaluate(x, y, z), 9, RoundingMode.CEILING);
    }

    @Override
    public String toString() {
        return toString("/");
    }
}
