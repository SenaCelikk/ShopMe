package com.example.shoppinglist.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.model.ShoppingItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ShoppingListViewModel : ViewModel() {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val _shoppingList = mutableStateListOf<ShoppingItem>()
    val shoppingList: List<ShoppingItem> get() = _shoppingList

    init {
        fetchItems()
    }

    private fun fetchItems() {
        db.collection("shopping_items")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                _shoppingList.clear()
                for (doc in snapshot.documents) {
                    val item = doc.toObject(ShoppingItem::class.java)
                    item?.id = doc.id // Assign the document ID to the item
                    item?.let {
                        // Parse the color from hex string and handle any parsing errors
                        val colorHex = it.swipeColorHex // Default to white if no color is set
                        it.swipeColorHex = colorHex // Store hex color for potential re-use
                        _shoppingList.add(it)
                    }
                }
            }
    }

    fun updateSwipeColor(item: ShoppingItem, color: Color) {
        val colorHex = "#%06X".format(0xFFFFFF and color.toArgb()) // Format color as hex string
        db.collection("shopping_items").document(item.id!!)
            .update("swipeColorHex", colorHex) // Store hex color string in Firestore
            .addOnSuccessListener {
                // Update locally to maintain consistency without refetching
                item.swipeColorHex = colorHex
                val index = _shoppingList.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    _shoppingList[index] = item // Update list with new color
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ShoppingListViewModel", "Error updating color: ${exception.message}")
            }
    }

    fun addItem(item: ShoppingItem) {
        db.collection("shopping_items").add(item)
    }

    fun removeItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                val itemId = item.id ?: return@launch
                db.collection("shopping_items").document(itemId).delete()
                    .addOnSuccessListener {
                        _shoppingList.remove(item) // Remove item from local list after successful deletion
                    }
                    .addOnFailureListener { exception ->
                        Log.e("ShoppingListViewModel", "Error removing item: ${exception.message}")
                    }
            } catch (e: Exception) {
                Log.e("ShoppingListViewModel", "Error removing item", e)
            }
        }
    }
}
