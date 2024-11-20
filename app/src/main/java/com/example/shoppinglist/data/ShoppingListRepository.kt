package com.example.shoppinglist.data

import android.util.Log
import com.example.shoppinglist.model.ShoppingItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ShoppingListRepository(private val db: FirebaseFirestore) {

    suspend fun fetchItems(): List<ShoppingItem> {
        val items = mutableListOf<ShoppingItem>()
        try {
            val snapshot = db.collection("shopping_items").get().await()
            for (doc in snapshot.documents) {
                val item = doc.toObject(ShoppingItem::class.java)
                item?.id = doc.id // Assign the document ID to the item
                item?.let {
                    items.add(it)
                }
            }
        } catch (e: Exception) {
            Log.e("ShoppingRepository", "Error fetching items: ${e.message}")
        }
        return items
    }

    suspend fun addItem(item: ShoppingItem) {
        try {
            db.collection("shopping_items").add(item).await()
        } catch (e: Exception) {
            Log.e("ShoppingRepository", "Error adding item: ${e.message}")
        }
    }

    suspend fun removeItem(item: ShoppingItem) {
        try {
            val itemId = item.id ?: return
            db.collection("shopping_items").document(itemId).delete().await()
        } catch (e: Exception) {
            Log.e("ShoppingRepository", "Error removing item: ${e.message}")
        }
    }

    suspend fun saveItem(item: ShoppingItem) {
        try {
            item.id?.let {
                db.collection("shopping_items").document(it).set(item).await()
            }
        } catch (e: Exception) {
            Log.e("ShoppingRepository", "Error saving item: ${e.message}")
        }
    }
}
