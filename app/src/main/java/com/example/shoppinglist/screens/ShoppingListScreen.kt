package com.example.shoppinglist.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.model.ShoppingItem
import com.example.shoppinglist.viewmodel.ShoppingListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel) {
    val shoppingList = viewModel.shoppingList
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ShoppingListTopAppBar(showTitle = shoppingList.isNotEmpty())
        },
        floatingActionButton = {
            if (shoppingList.isNotEmpty()){
                FloatingAddButton(
                    onClick = { showDialog = true })
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (shoppingList.isEmpty()) {
                EmptyState { showDialog = true }
            } else {
                ShoppingListContent(
                    shoppingList = shoppingList,
                    onRemove = viewModel::removeItem,
                    onToggleSelection = viewModel::toggleItemSelection
                )
            }

            if (showDialog) {
                AddItemDialog(
                    onAdd = { item ->
                        viewModel.addItem(item)
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListTopAppBar(showTitle: Boolean) {
    if (showTitle) {
        CenterAlignedTopAppBar(
            title = { Text("ShopMe", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.tertiary) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            )
        )
    }
}

@Composable
fun FloatingAddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.tertiary
    ) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Item")
    }
}

@Composable
fun EmptyState(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 80.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(18.dp),
            modifier = Modifier
                .clickable(onClick = onClick)
                .background(MaterialTheme.colorScheme.tertiary),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Couldn't Decide What To Get Yet?, Click Me!!",
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        AnimatedPreloader(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun ShoppingListContent(
    shoppingList: List<ShoppingItem>,
    onRemove: (ShoppingItem) -> Unit,
    onToggleSelection: (ShoppingItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(shoppingList) { item ->
            LazyColumnItem(
                item = item,
                onRemove = { onRemove(item) },
                onToggleSelection = { onToggleSelection(item) }
            )
        }
    }
}

@Composable
fun LazyColumnItem(
    item: ShoppingItem,
    onRemove: () -> Unit,
    onToggleSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            .background(
                color = if (item.isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.isSelected, onCheckedChange = { onToggleSelection() })
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = "${item.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.background,
                    textDecoration = if (item.isSelected) TextDecoration.LineThrough else null
                )
                Text(
                    text = "Quantity: ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.background,
                    textDecoration = if (item.isSelected) TextDecoration.LineThrough else null
                )
            }
        }
        IconButton(onClick = onRemove) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


