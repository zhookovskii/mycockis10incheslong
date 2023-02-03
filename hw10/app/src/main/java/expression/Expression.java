package expression;

import java.math.BigDecimal;
import java.util.List;

/**
 * One-argument arithmetic expression over integers.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@SuppressWarnings("ClassReferencesSubclass")
public interface Expression extends ToMiniString {

    int evaluate(int x);

    double evaluate(double x, double y, double z);

    BigDecimal evaluate(BigDecimal x, BigDecimal y, BigDecimal z);

    private static Const c(final int c) {
        return new Const(c);
    }


}
