package com.luizalabs.labsentregas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.luizalabs.labsentregas.ui.home.HomeScreen
import com.luizalabs.labsentregas.ui.navigation.Home
import com.luizalabs.labsentregas.ui.navigation.NewDelivery
import com.luizalabs.labsentregas.ui.newdelivery.NewDeliveryScreen
import com.luizalabs.labsentregas.ui.theme.LabsEntregasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LabsEntregasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Home,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<NewDelivery> {
                            NewDeliveryScreen(navController)
                        }
                        composable<Home> {
                            HomeScreen(navController)
                        }
                    }
                }
            }

        }

    }
}