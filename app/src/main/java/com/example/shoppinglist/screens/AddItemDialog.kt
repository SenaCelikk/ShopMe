package com.example.shoppinglist.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.model.ShoppingItem

@Composable
fun AddItemDialog(onAdd: (ShoppingItem) -> Unit, onDismiss: () -> Unit) {
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss },
        title = { Text("Add Shopping Item") },
        text = {
            Column {
                OutlinedTextField(
                    value = itemName,
                    label = { Text("Enter Item Name") },
                    onValueChange = {
                        itemName = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "ItemName"
                        )
                    },
                    modifier = Modifier.padding(10.dp)
                )
                OutlinedTextField(
                    value = itemQuantity.toString(),
                    label = { Text("Enter Quantity") },
                    onValueChange = { input ->
                        itemQuantity = if (input.all { it.isDigit() }) input else itemQuantity
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "ItemName"
                        )
                    },
                    modifier = Modifier.padding(10.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (itemName.isNotBlank()) {
                    onAdd(ShoppingItem(name = itemName, quantity = itemQuantity.toIntOrNull() ?: 1))
                    itemName = ""
                    itemQuantity = "1"
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        })
}
