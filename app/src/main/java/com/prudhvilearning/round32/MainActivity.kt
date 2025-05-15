package com.prudhvilearning.round32

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prudhvilearning.round32.ui.data.UserData
import com.prudhvilearning.round32.ui.screens.ChatScreen
import com.prudhvilearning.round32.ui.screens.HomeScreen
import com.prudhvilearning.round32.ui.screens.UserDetailScreen
import com.prudhvilearning.round32.ui.theme.Round32Theme
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Round32Theme {
                MainApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp() {
    val navController = rememberNavController()

    Scaffold(

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(navController)
            }
            composable("detail/{userJson}") { backStackEntry ->
                val userJson = backStackEntry.arguments?.getString("userJson")
                val user = Json.decodeFromString<UserData>(userJson ?: "")
                UserDetailScreen(user = user, navHostController = navController)

            }
            composable("chat_screen/{userJson}") { backStackEntry ->
                val userJson = backStackEntry.arguments?.getString("userJson")
                val user = Json.decodeFromString<UserData>(userJson ?: "")
                ChatScreen(user = user , navController)
            }
        }
    }
}

fun getRandomFromArray(arr: IntArray): Int? {
    return if (arr.isNotEmpty()) {
        arr.random()
    } else {
        null // return null if the array is empty
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Round32Theme {

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(){
                Box(modifier = Modifier.fillMaxSize()){
                   // HomeScreen(navController)
                }


                GlassBottomNavBar(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun GlassBottomNavBar(navController : androidx.navigation.NavHostController? = null, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(55.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        // 🔹 Blurred background layer


        Box(
            modifier = Modifier
                .blur(0.5.dp, edgeTreatment = BlurredEdgeTreatment. Unbounded)
                .background(Color.Black.copy(alpha = 0.9f))
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 100.dp)
            ,
        )

        // 🔹 Foreground content (NOT blurred)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp , horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.planet_sharp),
                contentDescription = "Emoji",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
                    .clickable {

                    }
            )
            Image(
                painter = painterResource(id = R.drawable.heart_sharp),
                contentDescription = "Emoji",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
                    .clickable {

                    }
            )
            Image(
                painter = painterResource(id = R.drawable.send),
                contentDescription = "Emoji",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
                    .clickable {

                    }
            )
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}




