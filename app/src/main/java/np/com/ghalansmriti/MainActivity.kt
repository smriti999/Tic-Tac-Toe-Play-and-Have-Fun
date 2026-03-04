package np.com.ghalansmriti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import np.com.ghalansmriti.ui.screens.GameBoardWireframe
import np.com.ghalansmriti.ui.screens.GameMode
import np.com.ghalansmriti.ui.screens.GameModeScreen
import np.com.ghalansmriti.ui.screens.HomeScreen
import np.com.ghalansmriti.ui.screens.HowToPlayWireframe
import np.com.ghalansmriti.ui.theme.TicTacToeTheme
 
enum class AppScreen { Home, GameMode, Board, HowToPlay }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                var current by remember { mutableStateOf(AppScreen.Home) }
                var mode by remember { mutableStateOf(GameMode.PlayerVsPlayer) }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                            onStart = { current = AppScreen.Board },
                            onBack = { current = AppScreen.Home },
                            modifier = Modifier.padding(innerPadding)
                        )
                        AppScreen.Board -> GameBoardWireframe(
                            modifier = Modifier.padding(innerPadding),
                            mode = mode
                        )
                        AppScreen.HowToPlay -> HowToPlayWireframe(
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
