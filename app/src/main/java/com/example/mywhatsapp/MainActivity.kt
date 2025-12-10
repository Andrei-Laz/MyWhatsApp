package com.example.mywhatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywhatsapp.data.DataSource
import com.example.mywhatsapp.model.Contact
import com.example.mywhatsapp.ui.theme.MyWhatsAppTheme
import kotlinx.coroutines.launch

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
//val pagerState = rememberPagerState(pageCount = {
//    3
//})
//
//HorizontalPager(state = pagerState) { page ->
//    ChatScreen()
//    Text(
//        text = "Page: $page",
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(100.dp)
//    )
//}
//
//// scroll to page
//val coroutineScope = rememberCoroutineScope()
//Button(onClick = {
//    coroutineScope.launch {
//        // Call scroll to on pagerState
//        pagerState.animateScrollToPage(2)
//    }
//}, modifier = Modifier.align(Alignment.BottomCenter)) {
//    Text("Jump to Page 2")
//}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MyApp(innerPadding: PaddingValues) {

    val titles = listOf("Chats", "Novedades", "Llamadas")

    // 1. Pager State (3 páginas = las tabs)
    val pagerState = rememberPagerState(pageCount = { titles.size })

    // 2. Necesario para animar el scroll del pager
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        // 3. LAS TABS
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Cyan,
            contentColor = Color.Black
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        // Al tocar la tab → saltar a esa página del pager
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) }
                )
            }
        }

        // 4. EL HORIZONTAL PAGER (REEMPLAZA AL "when(state)")
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when(page) {
                0 -> ChatScreen()
                1 -> NewsScreen()
                2 -> CallsScreen()
            }
        }
    }
}


@Composable
fun ContactCard(
    contact: Contact,
    modifier: Modifier = Modifier,
    onLongPress: () -> Unit
) {
    Row (modifier = Modifier
        .pointerInput(contact) {
            detectTapGestures(onLongPress = { onLongPress() })
        }) {
        Image(
            painter = painterResource(id = contact.contactImage),
            contentDescription = contact.name,
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Text(
            text = contact.name,
            fontSize = 24.sp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen() {
    val contactos = DataSource.contacts.groupBy { it.anime }

    // State to track which contact's dropdown is open
    var expandedContactId by remember { mutableStateOf<Int?>(null) }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        contactos.forEach { (anime, contact) ->
            stickyHeader {
                Text(
                    text = anime,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    fontSize = 32.sp
                )
            }
            items(contact) { contacto ->
                // Create a Box to position the dropdown relative to the contact card
                Box {
                    ContactCard(
                        contacto,
                        modifier = Modifier,
                        onLongPress = {
                            expandedContactId = contacto.id
                        }
                    )

                    // Show dropdown menu for this specific contact if it's the one that was long-pressed
                    if (expandedContactId == contacto.id) {
                        ContactDropDownMenu(
                            expanded = true,
                            onDismissRequest = { expandedContactId = null }
                        )
                    }
                }
            }
        }
    }
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

@Composable
fun ContactDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.padding(16.dp)
    ) {
        DropdownMenuItem(
            text = { Text("Añadir a favoritos") },
            onClick = {
                /* Do something... */
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = { Text("Llamar") },
            onClick = {
                /* Do something... */
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = { Text("Eliminar Contacto") },
            onClick = {
                /* Do something... */
                onDismissRequest()
            }
        )
    }
}