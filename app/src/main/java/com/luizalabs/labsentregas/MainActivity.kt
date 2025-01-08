package com.luizalabs.labsentregas

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luizalabs.labsentregas.ui.detail.DeliveryDetailsScreen
import com.luizalabs.labsentregas.ui.home.HomeScreen
import com.luizalabs.labsentregas.ui.navigation.Home
import com.luizalabs.labsentregas.ui.navigation.NewDelivery
import com.luizalabs.labsentregas.ui.newdelivery.NewDeliveryScreen
import com.luizalabs.labsentregas.ui.theme.LabsEntregasTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var showSplashScreen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                showSplashScreen
            }
            setOnExitAnimationListener{ screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.7f,
                    0f
                )
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.7f,
                    0f
                )
                zoomX.duration = 500
                zoomY.duration = 500
                zoomX.interpolator = OvershootInterpolator()
                zoomY.interpolator = OvershootInterpolator()
                zoomX.doOnEnd {
                    screen.remove()
                }
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomY.start()
                zoomX.start()
            }
        }
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
                        composable(
                             "Details/{deliveryId}",
                            arguments = listOf(navArgument("deliveryId") { type = NavType.IntType })
                        ) {
                            DeliveryDetailsScreen(navController)
                        }
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            showSplashScreen = false
        }
    }
}
