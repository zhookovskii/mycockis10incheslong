package hashslingingslasher.example.calculator

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun displaysNumberOnClick() {
        press(R.id.four)
        matchesResult("4")
    }

    @Test
    fun keepsSingeDecimalPoint() {
        press(R.id.zero)
        press(R.id.dot)
        press(R.id.four)
        press(R.id.dot)
        matchesResult("0.4")
    }

    @Test
    fun keepsSingleLeadingZero() {
        repeat(4) {
            press(R.id.zero)
        }
        matchesResult("0")
    }

    @Test
    fun allowsTrailingZeroes() {
        press(R.id.zero)
        press(R.id.dot)
        repeat(4) {
            press(R.id.zero)
        }
        matchesResult("0.0000")
    }

    @Test
    fun clearsResultField() {
        repeat(4) {
            press(R.id.seven)
        }
        press(R.id.C)
        matchesResult("")
    }

    @Test
    fun deletesLastEntry() {
        repeat(4) {
            press(R.id.four)
        }
        press(R.id.CE)
        matchesResult("444")
    }

    @Test
    fun keepsSingeOperationSymbol() {
        press(R.id.four)
        repeat(4) {
            press(R.id.div)
        }
        matchesResult("4/")
    }

    @Test
    fun calculatesOnClick() {
        press(R.id.six)
        press(R.id.mult)
        press(R.id.four)
        press(R.id.equals)
        matchesResult("24")
    }

    @Test
    fun ignoresOperationWithoutArguments() {
        repeat(4) {
            press(R.id.plus)
        }
        matchesResult("")
    }

    @Test
    fun handlesUnaryMinus() {
        repeat(5) {
            press(R.id.minus)
        }
        press(R.id.four)
        matchesResult("-----4")
    }


    @Test
    fun resultSurvivesRotation() {
        press(R.id.four)
        press(R.id.plus)
        tilt()
        press(R.id.six)
        straighten()
        press(R.id.equals)
        matchesResult("10")
    }

    @Test
    fun resultIsCopiedToClipboard() {
        press(R.id.nine)
        press(R.id.mult)
        press(R.id.nine)
        press(R.id.equals)
        repeat(2) {
            press(R.id.result)  // clickable textview may be stubborn at times
        }
        matchesResult(clipboardData())
    }

    private fun press(viewId: Int) = onView(withId(viewId)).perform(click())

    private fun tilt() {
        activityRule.scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        }
    }

    private fun clipboardData(): String {
        var res = ""
        activityRule.scenario.onActivity {
            val clipboard = it.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboard.primaryClip
            if (clipData != null) {
                res = clipData.getItemAt(0).text.toString()
            }
        }
        return res
    }

    private fun straighten() {
        activityRule.scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        }
    }

    private fun matchesResult(expected: String) =
        onView(withId(R.id.result)).check(matches(withText(expected)))
}