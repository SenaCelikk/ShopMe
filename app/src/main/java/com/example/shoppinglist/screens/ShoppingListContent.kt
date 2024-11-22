package com.example.shoppinglist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.model.ShoppingItem


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
            .border(2.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(16.dp))
            .background(
                color = if (item.isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier=Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.isSelected, onCheckedChange = { onToggleSelection() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                ))
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
                tint = MaterialTheme.colorScheme.onTertiary,
            )
        }
    }
}