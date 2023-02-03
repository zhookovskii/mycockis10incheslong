package expression;

import java.math.BigDecimal;

import expression.NewExpression;

public class LeadNulls implements NewExpression {
    private final NewExpression expr;

    public LeadNulls(NewExpression expr) {
        this.expr = expr;
    }

    @Override
    public int evaluate(int x) {
        return Integer.numberOfLeadingZeros(expr.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return Integer.numberOfLeadingZeros(expr.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "l0(" + expr.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LeadNulls) {
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
