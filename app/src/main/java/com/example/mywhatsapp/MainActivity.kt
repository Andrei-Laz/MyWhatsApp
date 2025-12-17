package com.example.mywhatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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
                                    IconButton(onClick = { /* Implement later */ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                    IconButton(onClick = { /* Implement later */ }) {
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
                        val image =
                            AnimatedImageVector.animatedVectorResource(R.drawable.ad_animations)
                        var atEnd by remember { mutableStateOf(false) }

                        FloatingActionButton(
                            onClick = { atEnd = !atEnd },
                            content = {
                                Image(
                                    painter = rememberAnimatedVectorPainter(image, atEnd),
                                    contentDescription = "VectorDrawable",
                                    modifier = Modifier
                                        .padding(15.dp)
                                )
                            },
                            modifier = Modifier
                                .size(65.dp)
                        )
                    }
                ) { innerPadding ->
                    MyApp(innerPadding)

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MyApp(innerPadding: PaddingValues) {

    val titles = listOf("Chats", "Novedades", "Llamadas")

    val pagerState = rememberPagerState(pageCount = { titles.size })

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Cyan,
            contentColor = Color.Black
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when(page) {
                0 -> ChatScreen()
                1 -> PauseStartScreen()
                2 -> SmileScreen()
            }
        }
    }
}


@Composable
fun ContactSheet(
    contact: Contact,
    modifier: Modifier = Modifier,
    onLongPress: () -> Unit
) {
    Row(
        modifier = Modifier
            .pointerInput(contact) {
                detectTapGestures(onLongPress = { onLongPress() })
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = contact.contactImage),
            contentDescription = contact.name,
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Text(
            text = contact.name,
            fontSize = 24.sp,
            modifier = modifier
                .padding(15.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen() {
    val contactos = DataSource.contacts.groupBy { it.anime }

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
                    ContactSheet(
                        contacto,
                        modifier = Modifier,
                        onLongPress = {
                            expandedContactId = contacto.id
                        }
                    )

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
fun PauseStartScreen() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.ad_tranformation)
    var atEnd by remember { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = "VectorDrawable",
            modifier = Modifier
                .clickable { atEnd = !atEnd }
                .size(350.dp)
        )
    }
}

@Composable
fun SmileScreen() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.ad_emociones)
    var atEnd by remember { mutableStateOf(false) }

    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd),
        contentDescription = "VectorDrawable",
        modifier = Modifier
            .clickable { atEnd = !atEnd }
            .size(250.dp)
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
                /* Implement later */
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = { Text("Llamar") },
            onClick = {
                /* Implement later */
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = { Text("Eliminar Contacto") },
            onClick = {
                /* Implement later */
                onDismissRequest()
            }
        )
    }
}