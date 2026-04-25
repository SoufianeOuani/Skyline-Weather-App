package com.example.training

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.training.presentation.ui.screens.home.HomeScreen
import com.example.training.presentation.ui.screens.home.HomeViewModel
import com.example.training.ui.theme.TrainingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainingTheme {
                val viewModel: HomeViewModel = viewModel()
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
