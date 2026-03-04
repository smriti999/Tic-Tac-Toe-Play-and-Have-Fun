package np.com.ghalansmriti.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import np.com.ghalansmriti.ui.theme.TicTacToeTheme
 
enum class GameMode { PlayerVsPlayer, PlayerVsComputer }

private val winLines = listOf(
    listOf(0, 1, 2),
    listOf(3, 4, 5),
    listOf(6, 7, 8),
    listOf(0, 3, 6),
    listOf(1, 4, 7),
    listOf(2, 5, 8),
    listOf(0, 4, 8),
    listOf(2, 4, 6)
)

private fun computeWinner(board: List<String>): String? {
    for (line in winLines) {
        val (a, b, c) = line
        val v = board[a]
        if (v.isNotEmpty() && v == board[b] && v == board[c]) return v
    }
    return null
}

@Composable
fun GameBoardScreen(
    mode: GameMode,
    onHome: () -> Unit,
    onFinished: (String?) -> Unit = {},
    restartToken: Int = 0,
    modifier: Modifier = Modifier
) {
    var board by remember(restartToken) { mutableStateOf(List(9) { "" }) }
    var current by remember(restartToken) { mutableStateOf("X") }
    var winner by remember(restartToken) { mutableStateOf<String?>(null) }
    var isDraw by remember(restartToken) { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Turn: Player $current",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = when (mode) {
                    GameMode.PlayerVsPlayer -> "Mode: Player vs Player"
                    GameMode.PlayerVsComputer -> "Mode: Player vs Computer"
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(3) { r ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { c ->
                        val idx = r * 3 + c
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .border(
                                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    RoundedCornerShape(10.dp)
                                )
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable(enabled = winner == null && !isDraw) {
                                    if (board[idx].isEmpty() && winner == null && !isDraw) {
                                        val next = board.toMutableList()
                                        next[idx] = current
                                        board = next
                                        val w = computeWinner(next)
                                        if (w != null) {
                                            winner = w
                                            onFinished(w)
                                        } else if (next.none { it.isEmpty() }) {
                                            isDraw = true
                                            onFinished(null)
                                        } else {
                                            current = if (current == "X") "O" else "X"
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = board[idx],
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    }
                }
            }
        }
        if (winner != null || isDraw) {
            val message = winner?.let { "Winner: Player $it" } ?: "It's a draw"
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onHome) { Text("Home") }
            Button(onClick = {
                board = List(9) { "" }
                current = "X"
                winner = null
                isDraw = false
            }) { Text("Restart") }
        }
    }
}

@Composable
fun ResultScreen(
    winner: String?,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = winner?.let { "Winner: Player $it" } ?: "It's a Draw",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onPlayAgain) { Text("Play Again") }
            OutlinedButton(onClick = onHome) { Text("Home") }
        }
    }
}

@Composable
fun GameModeScreen(
    selected: GameMode,
    onSelect: (GameMode) -> Unit,
    onStart: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose Game Mode",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = selected == GameMode.PlayerVsPlayer,
                    onClick = { onSelect(GameMode.PlayerVsPlayer) }
                )
                Text("Player vs Player")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = selected == GameMode.PlayerVsComputer,
                    onClick = { onSelect(GameMode.PlayerVsComputer) }
                )
                Text("Player vs Computer")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onStart) { Text("Start") }
            OutlinedButton(onClick = onBack) { Text("Back") }
        }
    }
}
 
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPlay: () -> Unit = {},
    onHowToPlay: () -> Unit = {},
    onExit: () -> Unit = {}
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Tic Tac Toe",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Play and Have Fun!!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = onPlay, modifier = Modifier.fillMaxWidth()) { Text("Play Game") }
            OutlinedButton(onClick = onHowToPlay, modifier = Modifier.fillMaxWidth()) { Text("How to Play") }
            TextButton(onClick = onExit) { Text("Exit") }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun HomePageWireframe(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tic Tac Toe",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {}) { Text("Play Game") }
            OutlinedButton(onClick = {}) { Text("How to Play") }
            OutlinedButton(onClick = {}) { Text("Exit") }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun GameModeWireframe(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose Game Mode",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = {}) { Text("Player vs Player") }
            OutlinedButton(onClick = {}) { Text("Player vs Computer") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {}) { Text("Start") }
            OutlinedButton(onClick = {}) { Text("Back") }
        }
    }
}

@Composable
fun GameBoardWireframe(modifier: Modifier = Modifier, mode: GameMode? = null) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Turn: Player X",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
        if (mode != null) {
            Text(
                text = when (mode) {
                    GameMode.PlayerVsPlayer -> "Mode: Player vs Player"
                    GameMode.PlayerVsComputer -> "Mode: Player vs Computer"
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(3) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .border(
                                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    RoundedCornerShape(8.dp)
                                )
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = {}) { Text("Home") }
            Button(onClick = {}) { Text("Restart") }
        }
    }
}

@Composable
fun ResultWireframe(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Winner: Player X",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {}) { Text("Play Again") }
            OutlinedButton(onClick = {}) { Text("Home") }
        }
    }
}

@Composable
fun HowToPlayWireframe(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "How to Play",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text("• Game is played on a 3×3 grid")
            Text("• Players take turns placing X and O")
            Text("• First to get 3 in a row wins")
            Text("• If all boxes are filled, it is a draw")
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Back") }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    TicTacToeTheme { HomePageWireframe() }
}

@Preview(showBackground = true)
@Composable
private fun GameModePreview() {
    TicTacToeTheme { GameModeWireframe() }
}

@Preview(showBackground = true)
@Composable
private fun GameBoardPreview() {
    TicTacToeTheme { GameBoardWireframe(mode = GameMode.PlayerVsPlayer) }
}

@Preview(showBackground = true)
@Composable
private fun ResultPreview() {
    TicTacToeTheme { ResultWireframe() }
}

@Preview(showBackground = true)
@Composable
private fun HowToPlayPreview() {
    TicTacToeTheme { HowToPlayWireframe() }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    TicTacToeTheme { HomeScreen() }
}

@Preview(showBackground = true)
@Composable
private fun GameModeScreenPreview() {
    var sel by remember { mutableStateOf(GameMode.PlayerVsPlayer) }
    TicTacToeTheme {
        GameModeScreen(
            selected = sel,
            onSelect = { sel = it },
            onStart = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameBoardScreenPreview() {
    TicTacToeTheme {
        GameBoardScreen(
            mode = GameMode.PlayerVsPlayer,
            onHome = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultScreenPreview() {
    TicTacToeTheme {
        ResultScreen(
            winner = "X",
            onPlayAgain = {},
            onHome = {}
        )
    }
}

