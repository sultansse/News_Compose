package com.softwareit.newscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softwareit.newscompose.ui.theme.NewsComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen() {
    Column {
        MyHeader()
        MyContent()
    }
}


@Composable
fun MyHeader() {
    TopAppBar(
        title = { Text("Hacker News") },
        navigationIcon = {
            IconButton(onClick = { /* Handle menu button click */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /* Handle search button click */ }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        backgroundColor = Color.White,
        elevation = AppBarDefaults.TopAppBarElevation
    )
}

@Composable
fun MyContent() {
    Column(modifier = Modifier.fillMaxSize()) {
// Tab layout
        val tabTitles = listOf("Overview", "Top", "Stories")
        val selectedTabIndex = remember { mutableStateOf(0) }
        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            backgroundColor = MaterialTheme.colors.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = { selectedTabIndex.value = index },
                    content = {
/*
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_newspaper_24),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.button
                            )
                        }
*/
                        Text(
                            text = title,
                            style = MaterialTheme.typography.button,
                            modifier = Modifier
                                .padding(12.dp)
                        )
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            when (selectedTabIndex.value) {
                0, 1, 2 -> {
                    LazyNews()
                }
            }
        }
    }
}

@Composable
fun LazyNews() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(20) { index ->
            Text(text = "Item $index")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsComposeTheme {
        MyHeader()
        MyContent()
    }
}