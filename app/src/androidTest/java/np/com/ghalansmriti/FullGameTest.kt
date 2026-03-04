package np.com.ghalansmriti

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FullGameTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun xWinsTopRow_navigatesToResult() {
        composeRule.onNodeWithTag("play_game_btn").performClick()
        composeRule.onNodeWithTag("mode_pvp").performClick()
        composeRule.onNodeWithTag("start_btn").performClick()

        composeRule.onNodeWithTag("cell_0").performClick() // X
        composeRule.onNodeWithTag("cell_3").performClick() // O
        composeRule.onNodeWithTag("cell_1").performClick() // X
        composeRule.onNodeWithTag("cell_4").performClick() // O
        composeRule.onNodeWithTag("cell_2").performClick() // X wins

        composeRule.onNodeWithTag("result_message").assertTextContains("Winner")
    }

    @Test
    fun drawGame_showsDrawResult() {
        composeRule.onNodeWithTag("play_game_btn").performClick()
        composeRule.onNodeWithTag("mode_pvp").performClick()
        composeRule.onNodeWithTag("start_btn").performClick()

        val sequence = listOf(0, 1, 2, 4, 5, 3, 7, 6, 8)
        sequence.forEach { idx ->
            composeRule.onNodeWithTag("cell_$idx").performClick()
        }

        composeRule.onNodeWithTag("result_message").assertTextContains("Draw")
    }
}
