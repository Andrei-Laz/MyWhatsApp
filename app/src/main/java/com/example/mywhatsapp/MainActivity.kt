package com.example.mywhatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mywhatsapp.ui.theme.MyWhatsAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyWhatsAppTheme {

                val scrollBehavior =
                    TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(
                                    "Centered Top App Bar",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            actions = {
                                Row {
                                    IconButton(onClick = { /* do something */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                    IconButton(onClick = { /* do something */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Share,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                }
                            },
                            scrollBehavior = scrollBehavior,
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /* do something */ },
                            content = { Icon(Icons.Filled.Check, contentDescription = "Check")})
                    }
                ) { innerPadding ->
                    MyApp(innerPadding)

                    //FAB
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(innerPadding: PaddingValues) {

    var state by remember { mutableStateOf(0) }
    val titles = listOf("Chats", "Novedades", "Llamadas")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        PrimaryTabRow(selectedTabIndex = state,
            //Coloring is a maybe
//            containerColor = Color.Cyan,
//            contentColor = Color.White
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = {
                        Text(
                            text = title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        when(state) {
            0 -> {ChatScreen()}
            1 -> {NewsScreen()}
            2 -> {CallsScreen()}
        }
    }
}

@Composable
fun ChatScreen() {
    Text(
        modifier = Modifier
            .padding(top = 24.dp),
        text = "Chat tab  selected",
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun NewsScreen() {
    Text(
        modifier = Modifier
            .padding(top = 24.dp),
        text = "Novedades tab  selected",
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun CallsScreen() {
    Text(
        modifier = Modifier
            .padding(top = 24.dp),
        text = "Llamadas tab  selected",
        style = MaterialTheme.typography.bodyLarge,
    )
}