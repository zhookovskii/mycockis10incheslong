package expression;

import java.math.BigDecimal;

public class TailNulls implements NewExpression {
    private final NewExpression expr;

    public TailNulls(NewExpression expr) {
        this.expr = expr;
    }

    @Override
    public int evaluate(int x) {
        return Integer.numberOfTrailingZeros(expr.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return Integer.numberOfTrailingZeros(expr.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "t0(" + expr.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TailNulls) {
            return expr.equals(o);
        }
        return false;
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

