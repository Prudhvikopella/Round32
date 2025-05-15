package com.prudhvilearning.round32.ui.screens


import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.prudhvilearning.round32.ui.theme.SoftPink
import com.prudhvilearning.round32.ui.theme.SoftPurple
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.prudhvilearning.round32.ui.data.UserData

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserDetailScreen(user: UserData, navHostController: NavHostController? = null) {
    var isLiked by remember { mutableStateOf(false) }
    val icon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    val tintColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else Color.Gray,
        label = "HeartColor"
    )
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1f,
        label = "HeartScale"
    )
    Box(modifier = Modifier.fillMaxSize()) {

        AsyncImage(
            model = user.profileUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Back Button
        IconButton(
            onClick = { navHostController?.popBackStack() },
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }

        // User Info and Actions
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // Name and Status
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${user.userName}, ${user.age}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(if (user.activeStatus) Color.Green else Color.Yellow)
                        )
                    }
                    Text(
                        text = user.userLocation,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            // Interests
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BlurredTextCard("Photography")
                BlurredTextCard("Cricket")
                BlurredTextCard("Shooting")
                BlurredTextCard("+6")
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Dismiss",
                    tint = Color.Blue,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(SoftPurple)
                        .padding(15.dp)
                        . clickable{
                        navHostController?.popBackStack()
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                FloatingActionButton(
                    onClick = {
                        val userJson = Uri.encode(Json.encodeToString(user))
                        navHostController?.navigate("chat_screen/$userJson")
                    },
                    containerColor = Color(0xFFD32F2F),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Send Message",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    imageVector = icon,
                    contentDescription = "Favorite",
                    tint = tintColor,
                    modifier = Modifier
                        .size(50.dp)
                        .graphicsLayer(scaleX = scale, scaleY = scale)
                        .clip(CircleShape)
                        .background(SoftPink)
                        .padding(15.dp)
                        .clickable {
                            isLiked = !isLiked
                        }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BlurredTextCard(text: String, modifier: Modifier = Modifier) {
    Box {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .graphicsLayer {
                    renderEffect = RenderEffect
                        .createBlurEffect(30f, 30f, Shader.TileMode.CLAMP)
                        .asComposeRenderEffect()
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        renderEffect = RenderEffect
                            .createBlurEffect(60f, 60f, Shader.TileMode.CLAMP)
                            .asComposeRenderEffect()
                    }
            )
            Text(
                text = text,
                color = Color.Transparent,
                fontSize = 10.sp
            )
        }

        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
