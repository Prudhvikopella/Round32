package com.prudhvilearning.round32.ui.screens

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.prudhvilearning.round32.GlassBottomNavBar
import com.prudhvilearning.round32.getRandomFromArray
import com.prudhvilearning.round32.ui.data.UserData
import com.prudhvilearning.round32.ui.theme.IndianRed
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString


@Composable
fun UserCardCustom(
    userName: String,
    location: String,
    matchPercentage: Int,
    profileUrl: String,
    isActive: Boolean,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    // Remember the image request to prevent re-creation on each recomposition
    val imageRequest = remember(profileUrl) {
        ImageRequest.Builder(context)
            .data(profileUrl)
            .crossfade(true)
            .build()
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                    onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                    model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(if (isActive) Color.Green else Color.Yellow)
                        )
                    }
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Card(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    // large enough to make corners circular
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    ,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "match $matchPercentage%",
                    modifier = Modifier.background(IndianRed).padding(vertical = 2.dp , horizontal = 10.dp),
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()){
        StaggeredUserGrid(navController)
        GlassTabSwitcher("For You", {}, modifier = Modifier.padding(10.dp))
        GlassBottomNavBar(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth())

    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun ThisScreenPreview(){
    val navController = rememberNavController()
   Box{
       StaggeredUserGrid(navController)
       GlassTabSwitcher("For You", {}, modifier = Modifier.padding(10.dp))
       GlassBottomNavBar(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth())
   }
}


@Composable
fun GlassTabSwitcher(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf("For You", "Matches")

    Box(
        modifier = modifier
            .padding(vertical = 75.dp)
            .height(56.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        // Row with tabs + toggle FAB
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Tabs Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == selectedTab

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onTabSelected(tab) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = Color.White, // persistently white text
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        // Bottom indicator
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .height(3.dp)
                                    .width(40.dp)
                                    .background(Color.White, shape = RoundedCornerShape(1.5.dp))
                            )
                        } else {
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                    }
                }
            }

            // Toggle Button on the right (optional)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StaggeredUserGrid(navHostController: NavHostController) {

    val userData = listOf(
        UserData("Prudhvi", "Bangalore, Andhra Pradesh", 98, "https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d", false, 25),
        UserData("Ashish", "Pervali, Andhra Pradesh", 95, "https://images.unsplash.com/photo-1506744038136-46273834b3fb", true, 27),
        UserData("Gowtham", "Hyderabad, Telangana", 92, "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde", false, 26),
        UserData("Dhoni", "Ranchi, Jharkhand", 96, "https://images.unsplash.com/photo-1544005313-94ddf0286df2", true, 38),
        UserData("Pawan Kalyan", "Vijayawada, Andhra Pradesh", 90, "https://images.unsplash.com/photo-1552058544-f2b08422138a", false, 51),
        UserData("Rathod", "Mumbai, Maharashtra", 87, "https://images.unsplash.com/photo-1494790108377-be9c29b29330", true, 30),
        UserData("HumansHere", "Delhi, India", 91, "https://images.unsplash.com/photo-1544005313-94ddf0286df2", false, 22),
        UserData("Tony Stark", "Chennai, Tamil Nadu", 89, "https://images.unsplash.com/photo-1527980965255-d3b416303d12", true, 45),
        UserData("Loki", "Asgard, Marvel", 93, "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c", false, 32),
        UserData("Steve Rogers", "Brooklyn, NY", 94, "https://images.unsplash.com/photo-1517841905240-472988babdf9", true, 39),
        UserData("Bruce Wayne", "Gotham", 85, "https://images.unsplash.com/photo-1552374196-c4e7ffc6e126", true, 41),
        UserData("Clark Kent", "Metropolis", 88, "https://images.unsplash.com/photo-1524504388940-b1c1722653e1", false, 34),
        UserData("Peter Parker", "New York, USA", 86, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d", true, 21),
        UserData("Black Widow", "Volgograd, Russia", 84, "https://images.unsplash.com/photo-1504593811423-6dd665756598", false, 35),
        UserData("Hawkeye", "Iowa, USA", 90, "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df", true, 37),
        UserData("Thor", "Asgard", 99, "https://images.unsplash.com/photo-1531123897727-8f129e1688ce", false, 1500),
        UserData("Wanda", "Sokovia", 95, "https://images.unsplash.com/photo-1554151228-14d9def656e4", true, 29),
        UserData("Vision", "AI Lab", 97, "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce", false, 3),
        UserData("Shuri", "Wakanda", 91, "https://images.unsplash.com/photo-1524504388940-b1c1722653e1", true, 20),
        UserData("T'Challa", "Wakanda", 98, "https://images.unsplash.com/photo-1534528741775-53994a69daeb", false, 35)
    )
    val randomHeights = remember {
        userData.map {
            getRandomFromArray(intArrayOf(200, 250, 280, 300, 350, 400))!!.dp
        }
    }
    val staggeredGridState = rememberLazyStaggeredGridState()
    val userList = remember { userData }
    LazyVerticalStaggeredGrid(
        state = staggeredGridState,
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(userList.size, key = { userList[it].userName }) { index ->

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(randomHeights[index])
            ) {
                UserCardCustom(
                    userName = "${userData[index].userName} , ${userData[index].age}",
                    location = userData[index].userLocation,
                    matchPercentage = userData[index].matchPercentage,
                    profileUrl = userData[index].profileUrl,
                    isActive = userData[index].activeStatus,
                    onClick = {
                        val userJson = Uri.encode(Json.encodeToString(userData[index]))
                        navHostController.navigate("detail/$userJson")
                    }
                )
            }
        }
    }
}


