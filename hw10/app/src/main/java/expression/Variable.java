package expression;

import java.math.BigDecimal;
import java.util.Objects;

public class Variable implements NewExpression {
    private final String var;

    public Variable(String var) {
        this.var = var;
    }

    public int evaluate(int x) {
        return new Const(x).evaluate(x);
    }

    public int evaluate(int x, int y, int z) {
        switch (var) {
            case "x":
                return new Const(x).evaluate(x, y, z);
            case "y":
                return new Const(y).evaluate(x, y, z);
            case "z":
                return new Const(z).evaluate(x, y, z);
            default:
                throw new AssertionError("Unknown variable");
        }
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Variable) {
            return hashCode() == o.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(var);
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
