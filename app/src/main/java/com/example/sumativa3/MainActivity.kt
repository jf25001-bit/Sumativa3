package com.example.sumativa3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.sumativa3.navigation.NavManager
import com.example.sumativa3.ui.theme.Sumativa3Theme
import com.example.sumativa3.viewmodel.InventarioViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: InventarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sumativa3Theme {
                NavManager(viewModel)
            }
        }
    }
}