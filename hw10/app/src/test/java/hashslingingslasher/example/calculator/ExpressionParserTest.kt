package hashslingingslasher.example.calculator

import expression.parser.ExpressionParser
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.IllegalArgumentException
import java.math.BigDecimal

@RunWith(JUnit4::class)
class ExpressionParserTest {
    @Test
    fun additionIsCorrect() {
        val x = evaluate("0.1 + 0.2")
        assertEquals(0.3.toBigDecimal(), x)
    }

    @Test
    fun subtractionIsCorrect() {
        val x = evaluate("-1--------1")
        assertEquals(BigDecimal.ZERO, x.setScale(0))
    }

    @Test
    fun throwsOnInfinity() {
        assertThrows(
            ArithmeticException::class.java,
        ) {
            evaluate("8 / 0")
        }
    }

    @Test
    fun calculationOrderIsCorrect() {
        val x = evaluate("2 + 2 * 2")
        assertEquals(BigDecimal(6), x.setScale(0))
    }

    @Test
    fun largeNumbersAreCalculated() {
        val x = evaluate("888888888 * 999999999")
        assertEquals(BigDecimal(888888887111111112), x)
    }

    @Test
    fun smallNumbersAreCalculated() {
        val x = evaluate("0.0000000001 + 0.0000000002")
        assertEquals(0.0000000003.toBigDecimal(), x)
    }

    @Test
    fun largeExpressionIsCalculated() {
        val expr = StringBuilder("1")
        for (i in 2 until 51) {
            expr.append("*")
            expr.append("$i")
        }
        val x = evaluate(expr.toString())
        assertEquals(
            BigDecimal("30414093201713378043612608166064768844377641568960512000000000000"),
            x.setScale(0)
        )
    }

    @Test
    fun emptyInputReturnsZero() {
        val x = evaluate("")
        assertEquals(BigDecimal.ZERO, x.setScale(0))
    }

    @Test
    fun throwsOnIncorrectInput() {
        assertThrows(
            IllegalArgumentException::class.java
        ) {
            evaluate("//////////*****2+////")
        }
    }

    private fun evaluate(str: String): BigDecimal {
        return ExpressionParser()
            .parse(str)
            .evaluate(
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
            )
    }
}