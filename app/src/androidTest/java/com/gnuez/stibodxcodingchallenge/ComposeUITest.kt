package com.gnuez.stibodxcodingchallenge

import android.content.Context
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.core.app.ApplicationProvider
import com.gnuez.stibodxcodingchallenge.activities.MainActivity
import org.junit.Rule
import org.junit.Test


private val timeout = 10_000L
class ComposeUITest {
    @JvmField
    @Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun goIntoPokemonDetails() {
        composeTestRule.onNodeWithText("Enter pokémon name")
            .performTextInput("mew")
        composeTestRule.onNodeWithContentDescription("Search")
            .assertHasClickAction().performClick()

        composeTestRule.waitUntil(timeout) {
            composeTestRule.onNodeWithText("Mew", useUnmergedTree = true).isDisplayed()
        }

        composeTestRule.onNodeWithText("Mew").assertExists().performClick()

        composeTestRule.onNodeWithText("Mew").assertExists()

        composeTestRule.onNodeWithTag("moveList").assertExists().performClick()

        composeTestRule.waitUntil (timeout){
            composeTestRule.onNodeWithText("Pound", useUnmergedTree = true).isDisplayed()
        }
    }


    @Test
    fun noPokemonFound() {
        composeTestRule.onNodeWithText("Enter pokémon name")
            .performTextInput("wrongName")
        composeTestRule.onNodeWithContentDescription("Search")
            .assertHasClickAction().performClick()
        val notFoundText = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.pokemon_not_found)
        composeTestRule.waitUntil(timeout) {
            composeTestRule.onNodeWithText(notFoundText, useUnmergedTree = true).isDisplayed()
        }

        composeTestRule.onNodeWithText(notFoundText).assertExists()
    }

    @Test
    fun appNavigation() {
        composeTestRule.waitUntil(timeout) {
            composeTestRule.onNodeWithText("Bulbasaur", useUnmergedTree = true).isDisplayed()
        }

        composeTestRule.onNodeWithTag("gridList")
            .performTouchInput {
                swipeUp()
            }

        val nextArrow = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.nextPage)

        val prevousArrow = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.previousPage)

        composeTestRule.onNodeWithContentDescription(nextArrow)
            .assertHasClickAction().performClick()

        composeTestRule.waitUntil (timeout){
            composeTestRule.onNodeWithText("Pikachu", useUnmergedTree = true).isDisplayed()
        }

        composeTestRule.onNodeWithContentDescription(prevousArrow)
            .assertHasClickAction().performClick()

        
        composeTestRule.waitUntil(timeout) {
            composeTestRule.onNodeWithText("Bulbasaur", useUnmergedTree = true).isDisplayed()
        }

    }
}