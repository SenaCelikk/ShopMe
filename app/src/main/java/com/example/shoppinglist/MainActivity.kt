package com.example.shoppinglist

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.shoppinglist.screens.ShoppingListScreen
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.example.shoppinglist.viewmodel.ShoppingListViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val viewModel: ShoppingListViewModel by viewModels()
    private lateinit var auth: FirebaseAuth // firebase authentication


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ShoppingListTheme {
                Scaffold { innerPadding ->
                    ShoppingListScreen(viewModel)
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val windowInsetsController = window.insetsController
            windowInsetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShoppingListScreenPreview() {
    ShoppingListTheme {
    }
}
