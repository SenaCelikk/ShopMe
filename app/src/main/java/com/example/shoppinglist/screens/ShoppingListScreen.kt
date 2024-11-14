package com.example.shoppinglist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.R
import com.example.shoppinglist.model.ShoppingItem
import com.example.shoppinglist.viewmodel.ShoppingListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel, modifier: Modifier = Modifier) {
    val shoppingList = viewModel.shoppingList
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                text = "ShopMe",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge
            )
        }, navigationIcon = {
            IconButton(onClick = { /* TODO: Handle menu click */ }) {
                Icon(
                    Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black
                )
            }
        }, actions = {
            Icon(
                painter = painterResource(R.drawable.open_doodles_man_jumping_to_the_side),
                contentDescription = "Profile",
                tint = Color.Black,
                modifier = Modifier
                    .padding(5.dp)
                    .size(100.dp, 70.dp)
            )

        }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
        )
    }, content = { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(shoppingList) { item ->
                        SwipeableItem(
                            item = item,
                            onRemove = { viewModel.removeItem(item) },
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
                AddItemDialog(onAdd = { item ->
                    viewModel.addItem(item)
                    showDialog = false
                }, onDismiss = { showDialog = false }  // Dismiss dialog
                )
            }
        }
    })
}

@Composable
fun SwipeableItem(
    item: ShoppingItem,
    onRemove: () -> Unit,
    viewModel: ShoppingListViewModel
) {
    Box(modifier = Modifier.padding(10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA1C6EA), RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Space items to push icon to the end
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Item Info Section with Checkbox and Texts
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(
                    checked = item.isSelected,
                    onCheckedChange = { viewModel.toggleItemSelection(item) }
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "${item.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xff04080F)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Quantity: ${item.quantity}",
                        color = Color(0xff04080F)
                    )
                }
            }

            IconButton(
                onClick = { onRemove() },
                modifier = Modifier.align(Alignment.CenterVertically) // Align icon to center vertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

