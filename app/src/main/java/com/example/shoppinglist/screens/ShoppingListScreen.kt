package com.example.shoppinglist.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.model.ShoppingItem
import com.example.shoppinglist.viewmodel.ShoppingListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel, modifier: Modifier = Modifier) {
    val shoppingList = viewModel.shoppingList
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.padding(bottom = 10.dp), content = { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
        ) {
            if (shoppingList.isEmpty()) {
                Card(
                    elevation = CardDefaults.cardElevation(18.dp),
                    modifier = Modifier
                        .padding(vertical = 80.dp, horizontal = 16.dp)
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
                    border = BorderStroke(1.dp, Color.Blue),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White, // Change color of the card when pressed
                    )
                ) {
                    Text(
                        text = "Couldn't Decide What To Get Yet?, Click Me!!",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .clickable(true, onClick = { showDialog = true }),
                    )
                }
                AnimatedPreloader(
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(shoppingList) { item ->
                        LazyColumnItem(
                            item = item,
                            onRemove = { viewModel.removeItem(item) },
                            viewModel = viewModel
                        )
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

//LazyColumn items
@Composable
fun LazyColumnItem(
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
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xff04080F),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Quantity: ${item.quantity}",
                        color = Color(0xff04080F),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 14.sp

                    )
                }
            }

            IconButton(
                onClick = { onRemove() },
                modifier = Modifier.align(Alignment.CenterVertically) // Align icon to center vertically
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

