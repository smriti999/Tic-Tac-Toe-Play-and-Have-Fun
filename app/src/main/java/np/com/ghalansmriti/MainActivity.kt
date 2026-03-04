package np.com.ghalansmriti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import np.com.ghalansmriti.ui.screens.GameBoardScreen
import np.com.ghalansmriti.ui.screens.GameMode
import np.com.ghalansmriti.ui.screens.GameModeScreen
import np.com.ghalansmriti.ui.screens.HomeScreen
import np.com.ghalansmriti.ui.screens.HowToPlayWireframe
import np.com.ghalansmriti.ui.screens.ResultScreen
import np.com.ghalansmriti.ui.theme.TicTacToeTheme
 
enum class AppScreen { Home, GameMode, Board, HowToPlay, Result }

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                var current by remember { mutableStateOf(AppScreen.Home) }
                var mode by remember { mutableStateOf(GameMode.PlayerVsPlayer) }
                var boardSession by remember { mutableStateOf(0) }
                var resultWinner by remember { mutableStateOf<String?>(null) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val title = when (current) {
                            AppScreen.Home -> "Tic Tac Toe"
                            AppScreen.GameMode -> "Select Mode"
                            AppScreen.Board -> "Game Board"
                            AppScreen.HowToPlay -> "How to Play"
                            AppScreen.Result -> "Result"
                        }
                        CenterAlignedTopAppBar(
                            title = { Text(title) },
                            navigationIcon = {
                                if (current != AppScreen.Home) {
                                    TextButton(onClick = {
                                        current = when (current) {
                                            AppScreen.GameMode -> AppScreen.Home
                                            AppScreen.Board -> AppScreen.GameMode
                                            AppScreen.HowToPlay -> AppScreen.Home
                                            AppScreen.Result -> AppScreen.Home
                                            else -> AppScreen.Home
                                        }
                                    }) {
                                        Text("Back")
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    when (current) {
                        AppScreen.Home -> HomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            onPlay = { current = AppScreen.GameMode },
                            onHowToPlay = { current = AppScreen.HowToPlay },
                            onExit = { finish() }
                        )
                        AppScreen.GameMode -> GameModeScreen(
                            selected = mode,
                            onSelect = { mode = it },
                            onStart = { 
                                resultWinner = null
                                boardSession++
                                current = AppScreen.Board 
                            },
                            onBack = { current = AppScreen.Home },
                            modifier = Modifier.padding(innerPadding)
                        )
                        AppScreen.Board -> GameBoardScreen(
                            modifier = Modifier.padding(innerPadding),
                            mode = mode,
                            onHome = { current = AppScreen.Home },
                            onBack = { current = AppScreen.GameMode },
                            onFinished = { w ->
                                resultWinner = w
                                current = AppScreen.Result
                            },
                            restartToken = boardSession
                        )
                        AppScreen.HowToPlay -> HowToPlayWireframe(
                            modifier = Modifier.padding(innerPadding),
                            onBack = { current = AppScreen.Home }
                        )
                        AppScreen.Result -> ResultScreen(
                            winner = resultWinner,
                            onPlayAgain = { 
                                boardSession++
                                current = AppScreen.Board 
                            },
                            onHome = { current = AppScreen.Home },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                } 
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TicTacToeTheme {
        Greeting("Android")
    }
}
