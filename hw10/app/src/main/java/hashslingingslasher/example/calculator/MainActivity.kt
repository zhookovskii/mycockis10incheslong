package hashslingingslasher.example.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import expression.parser.*
import java.lang.ArithmeticException
import java.lang.Exception
import java.math.BigDecimal

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var result: TextView

    private var isNumericEntry = false
    private var isDecimal = false
    private var wasCalculated = false
    private var lastEntry = 0 // 0 stands for nothing, 1 - number, 2 - operation
    private var isLeadingZero = false

    private val symbols = setOf('+', '-', '*', '/')

    lateinit var zero: Button
    lateinit var one: Button
    lateinit var two: Button
    lateinit var three: Button
    lateinit var four: Button
    lateinit var five: Button
    lateinit var six: Button
    lateinit var seven: Button
    lateinit var eight: Button
    lateinit var nine: Button
    lateinit var equals: Button
    lateinit var plus: Button
    lateinit var minus: Button
    lateinit var mult: Button
    lateinit var div: Button
    lateinit var clear: Button
    lateinit var clearEntry: Button
    lateinit var dot: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        zero = findViewById(R.id.zero)
        zero.setOnClickListener(this)

        one = findViewById(R.id.one)
        one.setOnClickListener(this)

        two = findViewById(R.id.two)
        two.setOnClickListener(this)

        three = findViewById(R.id.three)
        three.setOnClickListener(this)

        four = findViewById(R.id.four)
        four.setOnClickListener(this)

        five = findViewById(R.id.five)
        five.setOnClickListener(this)

        six = findViewById(R.id.six)
        six.setOnClickListener(this)

        seven = findViewById(R.id.seven)
        seven.setOnClickListener(this)

        eight = findViewById(R.id.eight)
        eight.setOnClickListener(this)

        nine = findViewById(R.id.nine)
        nine.setOnClickListener(this)

        equals = findViewById(R.id.equals)
        equals.setOnClickListener(this)

        plus = findViewById(R.id.plus)
        plus.setOnClickListener(this)

        minus = findViewById(R.id.minus)
        minus.setOnClickListener(this)

        mult = findViewById(R.id.mult)
        mult.setOnClickListener(this)

        div = findViewById(R.id.div)
        div.setOnClickListener(this)

        clear = findViewById(R.id.C)
        clear.setOnClickListener(this)

        clearEntry = findViewById(R.id.CE)
        clearEntry.setOnClickListener(this)

        dot = findViewById(R.id.dot)
        dot.setOnClickListener(this)

        result = findViewById(R.id.result)
        result.text = ""
        result.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText("result", result.text))
            Toast.makeText(applicationContext, "Result copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(RES, result.text.toString())

        outState.putBoolean(ISDECIMAL, isDecimal)
        outState.putBoolean(WASCALCULATED, wasCalculated)
        outState.putBoolean(ISLEADINGZERO, isLeadingZero)
        outState.putBoolean(ISNUMERICENTRY, isNumericEntry)
        outState.putInt(LASTENTRY, lastEntry)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        result.text = savedInstanceState.getString(RES)

        isDecimal = savedInstanceState.getBoolean(ISDECIMAL)
        isNumericEntry = savedInstanceState.getBoolean(ISNUMERICENTRY)
        lastEntry = savedInstanceState.getInt(LASTENTRY)
        wasCalculated = savedInstanceState.getBoolean(WASCALCULATED)
        isLeadingZero = savedInstanceState.getBoolean(ISLEADINGZERO)
    }

    companion object {
        private const val RES = "fwfehufgeug.MainActivity.result"

        private const val ISDECIMAL = "ewirjeig.MainActivity.isDecimal"
        private const val ISNUMERICENTRY = "ewirjeig.MainActivity.isNumericEntry"
        private const val LASTENTRY = "ewrwwtd.MainActivity.lastEntry"
        private const val WASCALCULATED = "psodefef.MainActivity.wasCalculated"
        private const val ISLEADINGZERO = "ufefefdn.MainActivity.isLeadingZero"
    }

    override fun onClick(p0: View?) {
        if (wasCalculated) {
            result.text = ""
            wasCalculated = false
            isNumericEntry = false
            isDecimal = false
            lastEntry = 0
            isLeadingZero = false
        }
        if (p0 != null) {
            when (p0.id) {
                R.id.zero -> {
                    if (isLeadingZero && !isDecimal) {
                        // do nothing
                    } else if (lastEntry != 1) {
                        isLeadingZero = true
                        isNumericEntry = true
                        lastEntry = 1
                        result.text = result.text.toString().plus("0")
                    } else {
                        result.text = result.text.toString().plus("0")
                    }

                }
                R.id.one, R.id.two, R.id.three,
                R.id.four, R.id.five, R.id.six,
                R.id.seven, R.id.eight, R.id.nine -> {
                    isNumericEntry = true
                    lastEntry = 1
                    val currentView:Button = findViewById(p0.id)
                    val newResult = result.text.toString()
                    result.text = newResult.plus(currentView.text)
                }
                R.id.plus, R.id.minus, R.id.mult, R.id.div -> {
                    if (p0.id == R.id.minus) {
                        result.text = result.text.toString().plus(getString(R.string.minus))
                        lastEntry = 2
                    } else {
                        if (isNumericEntry && result.text.toString().last() != '.') {
                            isNumericEntry = false
                            lastEntry = 2
                            isDecimal = false
                            isLeadingZero = false
                            val newResult = result.text.toString()
                            val symbol:Button = findViewById(p0.id)
                            result.text = newResult.plus(symbol.text)
                        } else {
                            //do nothing
                        }
                    }
                }
                R.id.C -> {
                    isNumericEntry = false
                    isDecimal = false
                    lastEntry = 0
                    isLeadingZero = false
                    result.text = ""
                }
                R.id.CE -> {
                    if (result.text.toString() == "") {
                        // do nothing
                    } else {
                        val deletedSymbol = result.text.toString().last()
                        result.text = result.text.toString().substring(
                            0, result.text.toString().length - 1)
                        if (result.text.toString() == "") {
                            isNumericEntry = false
                            isDecimal = false
                            lastEntry = 0
                            isLeadingZero = false
                        } else if (deletedSymbol in symbols) {
                            isNumericEntry = true
                            lastEntry = 1
                            val currentNumber = result.text.toString().toCharArray()
                            var k = currentNumber.size - 1
                            val previousEntry = StringBuilder()
                            while (currentNumber[k] in '0'..'9' || currentNumber[k] == '.') {
                                if (k == 0) {
                                    break
                                }
                                if (currentNumber[k] == '.') {
                                    isDecimal = true
                                    break
                                } else {
                                    previousEntry.append(currentNumber[k])
                                    k--
                                }
                            }
                            if (previousEntry.equals(getString(R.string.zero))) {
                                isLeadingZero = true
                            }
                        } else if (deletedSymbol == '.') {
                            isDecimal = false
                            if (result.text.toString().last() == '0') {
                                isLeadingZero = true
                            }
                        } else if (result.text.toString().last() in symbols) {
                            lastEntry = 2
                            isNumericEntry = false
                            isLeadingZero = false
                        } else {
                            // do nothing
                        }
                    }
                }
                R.id.dot -> {
                    if (isDecimal) {
                        //do nothing
                    } else {
                        if (isNumericEntry) {
                            isDecimal = true
                            val newResult = result.text.toString()
                            result.text = newResult.plus(getString(R.string.dot))
                        } else {
                            //do nothing
                        }
                    }
                }
                R.id.equals -> {
                    if (lastEntry == 1) {
                        val parser = ExpressionParser()
                        try {
                            val res: BigDecimal = parser.parse(result.text.toString()).
                            evaluate(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
                            if (res.remainder(BigDecimal.ONE).stripTrailingZeros().equals(BigDecimal.ZERO)) {
                                result.text = String.format(res.toBigInteger().toString())
                            } else {
                                result.text = String.format(res.toString())
                            }
                        } catch (e: ArithmeticException) {
                            result.text = getString(R.string.NAN)
                        } catch (e: Exception) {
                            result.text = getString(R.string.WI)
                        } finally {
                            wasCalculated = true
                        }
                    } else {
                        // do nothing
                    }
                }
            }
        }
    }


}