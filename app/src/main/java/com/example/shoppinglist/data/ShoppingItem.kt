package com.example.shoppinglist.data

data class ShoppingItem(
    var id: String? = null,
    var name: String? = null,
    var quantity: Int? = null,
    var swipeColorHex: String = "#B7E0FF",
    var isSelected: Boolean = false
)
