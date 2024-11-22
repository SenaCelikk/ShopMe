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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
        title = { Text("Add Shopping Item",
            color =  MaterialTheme.colorScheme.primary) },
        containerColor = MaterialTheme.colorScheme.tertiary,
        text = {
            Column {
                OutlinedTextField(
                    value = itemName,
                    label = {
                        Text(
                            text = "Enter Item Name",
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onValueChange = {
                        itemName = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "ItemName",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier.padding(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary
                    )
                )
                OutlinedTextField(
                    value = itemQuantity.toString(),
                    label = {
                        Text(
                            text = "Enter Quantity",
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onValueChange = { input ->
                        itemQuantity = if (input.all { it.isDigit() }) input else itemQuantity
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "ItemName",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    modifier = Modifier.padding(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary
                    )
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
