package com.example.shoppinglist.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.model.ShoppingItem
import com.example.shoppinglist.data.ShoppingListRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ShoppingListViewModel : ViewModel() {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val shoppingRepository = ShoppingListRepository(db)
    private val _shoppingList = mutableStateListOf<ShoppingItem>()
    val shoppingList: List<ShoppingItem> get() = _shoppingList

    init {
        observeItems()
    }
    private fun observeItems() {
        shoppingRepository.observeItems { items ->
            _shoppingList.clear()
            _shoppingList.addAll(items)
        }
    }

    private fun fetchItems() {
        viewModelScope.launch {
            val items = shoppingRepository.fetchItems()
            _shoppingList.clear()
            _shoppingList.addAll(items)
        }
    }

    fun addItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingRepository.addItem(item)
            fetchItems() // Refresh the list after adding
        }
    }

    fun removeItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingRepository.removeItem(item)
            _shoppingList.remove(item) // Remove item from the local list after successful deletion
        }
    }

    fun saveItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingRepository.saveItem(item)
        }
    }

    fun toggleItemSelection(item: ShoppingItem) {
        val updatedItem = item.copy(isSelected = !item.isSelected)
        saveItem(updatedItem) // Save the updated item
        val index = _shoppingList.indexOf(item)
        if (index != -1) {
            _shoppingList[index] = updatedItem
        }
    }
}
