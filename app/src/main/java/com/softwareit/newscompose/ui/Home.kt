package com.softwareit.newscompose.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.softwareit.newscompose.data.HackerNewsAPI
import com.softwareit.newscompose.data.NewsItem
import com.softwareit.newscompose.data.RetrofitInstance
import kotlin.math.min

class Home {

    @Composable
    fun HomePage() {
        Column {
            MyHeader()
            MyTabs(tabTitles = listOf("Overview", "Top", "Stories"))
//            MyContent()
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
    fun MyTabs(tabTitles: List<String>) {
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
        ContainerBox(selectedTabIndex)
    }

    @Composable
    fun ContainerBox(selectedTabIndex: MutableState<Int>) {
        Column {
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
        var isLoading by remember { mutableStateOf(true) }
        val batchSize = 20 // Number of items to fetch at once
        var reachedEndOfList by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            try {
                val topStoryIds = api.getTopStories()
                loadBatch(topStoryIds, 0, batchSize, api, items)
            } catch (e: Exception) {
                // Handle exception if needed
            } finally {
                isLoading = false
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.wrapContentSize(Alignment.Center))
            }
        } else {
            LazyColumn {
                itemsIndexed(items) { index, item ->
                    if (index == items.size - 1 && !reachedEndOfList) {
                        LaunchedEffect(Unit) {
                            val topStoryIds = api.getTopStories()
                            val nextStartIndex = items.size
                            val nextEndIndex = nextStartIndex + batchSize
                            val hasMoreItems = loadBatch(
                                topStoryIds,
                                nextStartIndex,
                                nextEndIndex,
                                api,
                                items
                            )
                            if (!hasMoreItems) {
                                reachedEndOfList = true
                            }
                        }
                    }
                    Card(
                        elevation = 4.dp,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = item.title ?: "Unknown Title",
                                style = MaterialTheme.typography.h6
                            )
                            Text(
                                text = "By ${item.author ?: "Unknown"}",
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text(
                                text = "Score: ${item.score ?: 0}",
                                style = MaterialTheme.typography.subtitle2
                            )
                            Text(
                                text = item.url ?: "Unknown URL",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }
        }
    }

    suspend fun loadBatch(
        topStoryIds: List<String>,
        startIndex: Int,
        endIndex: Int,
        api: HackerNewsAPI,
        items: MutableList<NewsItem>
    ): Boolean {
        val finalIndex = min(endIndex, topStoryIds.size)
        if (startIndex >= finalIndex) return false

        for (i in startIndex until finalIndex) {
            items.add(api.getItem(topStoryIds[i]))
        }
        return finalIndex < topStoryIds.size
    }

}