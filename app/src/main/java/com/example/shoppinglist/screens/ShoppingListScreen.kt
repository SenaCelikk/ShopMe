package com.example.shoppinglist.screens

import androidx.compose.foundation.Image
import com.example.shoppinglist.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.viewmodel.ShoppingListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel) {
    val shoppingList = viewModel.shoppingList
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        ShoppingListTopAppBar(showTitle = shoppingList.isNotEmpty())
    }, floatingActionButton = {
        if (shoppingList.isNotEmpty()) {
            FloatingAddButton(onClick = { showDialog = true })
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    if (shoppingList.isNotEmpty())
                        MaterialTheme.colorScheme.onBackground
                    else
                        MaterialTheme.colorScheme.onPrimary)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (shoppingList.isEmpty()) {
                EmptyState { showDialog = true }
            } else {
                Image(
                    painterResource(R.drawable.frankie),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                ShoppingListContent(
                    shoppingList = shoppingList,
                    onRemove = viewModel::removeItem,
                    onToggleSelection = viewModel::toggleItemSelection
                )
            }

            if (showDialog) {
                AddItemDialog(onAdd = { item ->
                    viewModel.addItem(item)
                    showDialog = false
                }, onDismiss = { showDialog = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListTopAppBar(showTitle: Boolean) {
    if (showTitle) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "ShopMe",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
    }
}

@Composable
fun FloatingAddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.size(70.dp, 70.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = "Add Item",
            modifier = Modifier.size(50.dp, 50.dp)
        )
    }
}


