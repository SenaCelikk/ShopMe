package com.example.shoppinglist.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.R
import com.example.shoppinglist.model.ShoppingItem
import com.example.shoppinglist.viewmodel.ShoppingListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel, modifier: Modifier = Modifier) {
    val shoppingList = viewModel.shoppingList
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ShopMe",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Handle menu click */ }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Handle profile click */ }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(paddingValues)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(
                                width = 2.dp, // Border width
                                color = Color(0xff04080F), // Border color
                                shape = MaterialTheme.shapes.medium // Optional: Shape of the border (e.g., rounded corners)
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.open_doodles_man_jumping_to_the_side),
                            contentDescription = "information icon",
                            modifier = Modifier
                                .size(100.dp, 70.dp)
                                .padding(10.dp)
                        )
                        Text(
                            text = "Swipe Left To Select Item",
                            color = Color(0xff04080F),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp, 10.dp, 6.dp, 0.dp)
                    ) {
                        items(shoppingList) { item ->
                            SwipeableItem(
                                item = item, onRemove = { viewModel.removeItem(item) },
                                viewModel = viewModel
                            )
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = Color(0xFFA1C6EA),
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Item")
                }

                if (showDialog) {
                    AddItemDialog(
                        onAdd = { item ->
                            viewModel.addItem(item)
                            showDialog = false
                        },
                        onDismiss = { showDialog = false }  // Dismiss dialog
                    )
                }
            }
        }
    )
}

@Composable
fun SwipeableItem(item: ShoppingItem, onRemove: () -> Unit, viewModel: ShoppingListViewModel) {
    val initialColor = try {
        Color(android.graphics.Color.parseColor(item.swipeColorHex))
    } catch (e: IllegalArgumentException) {
        println(e.message.toString())
        Color.Transparent
    }
    var backgroundColor by remember { mutableStateOf(initialColor) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        when {
                            dragAmount < -100 -> {
                                backgroundColor = Color(0xFF3E68A3)
                                viewModel.updateSwipeColor(item, backgroundColor)
                            }

                            dragAmount > 100 -> {
                                backgroundColor = Color(0xFFE0E9F6)
                                viewModel.updateSwipeColor(item, backgroundColor)
                            }
                        }
                    }
                )
            }
    ) {
        Text(
            text = "${item.name} -> ${item.quantity}",
            modifier = Modifier
                .weight(1f)
                .padding(16.dp, 5.dp, 0.dp, 0.dp)
                .align(Alignment.CenterVertically),
            color = Color(0xff04080F)
        )
        IconButton(
            onClick = {
                onRemove()
            },
            modifier = Modifier
                .padding(0.dp, 5.dp, 16.dp, 0.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "Remove Item"
            )
        }
    }
}