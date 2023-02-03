package expression;

import java.math.BigDecimal;
import java.util.List;

/**
 * Three-argument arithmetic expression over integers.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@SuppressWarnings("ClassReferencesSubclass")
public interface TripleExpression extends ToMiniString {

    int evaluate(int x, int y, int z);

    double evaluate(double x, double y, double z);

    BigDecimal evaluate(BigDecimal x, BigDecimal y, BigDecimal z);

}
