package expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class Const implements NewExpression {
    private final String c;

    public Const(String c) {
        this.c = c;
    }

    public Const(int c)
    {
        this.c = Integer.toString(c);
    }

    public Const(double c) {this.c = Double.toString(c);}

    public int evaluate(int x) {
        return BigInteger.valueOf(Long.parseLong(c)).intValue();
    }

    public int evaluate(int x, int y, int z) {
        return BigInteger.valueOf(Long.parseLong(c)).intValue();
    }

    public double evaluate(double x, double y, double z) {
        return Double.parseDouble(c);
    }

    @Override
    public BigDecimal evaluate(BigDecimal x, BigDecimal y, BigDecimal z) {
        return BigDecimal.valueOf(Double.parseDouble(c));
    }

    /*public int evaluate(int x) {
        return Integer.parseInt(c);
    }

    public int evaluate(int x, int y, int z) {
        return Integer.parseInt(c);
    }*/

    @Override
    public String toString() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Const) {
            return o.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(Integer.parseInt(c));
    }
}
