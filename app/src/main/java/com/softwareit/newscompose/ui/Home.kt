package com.softwareit.newscompose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.softwareit.newscompose.data.HackerNewsAPI
import com.softwareit.newscompose.data.NewsItem
import com.softwareit.newscompose.data.RetrofitInstance

class Home {

    @Composable
    fun HomePage() {
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
                    0, 1 -> {
                        NewsList()
                    }
                    2 -> {
                        Text(text = "tab 3 content")
                    }
                }
            }
        }
    }


    @Composable
    fun NewsList() {

        val api = RetrofitInstance.retrofit.create(HackerNewsAPI::class.java)
        val items = remember { mutableStateListOf<NewsItem>() }

        LaunchedEffect(Unit) {
            items.add(api.getItem("8863"))
            items.add(api.getItem("192327"))
        }

        LazyColumn {
            items(items) { item ->
                Card(
                    elevation = 4.dp,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(text = item.title, style = MaterialTheme.typography.h6)
                        Text(text = "By ${item.author}", style = MaterialTheme.typography.subtitle1)
                        Text(
                            text = "Score: ${item.score}",
                            style = MaterialTheme.typography.subtitle2
                        )
                        Text(text = item.url, style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
    }

}