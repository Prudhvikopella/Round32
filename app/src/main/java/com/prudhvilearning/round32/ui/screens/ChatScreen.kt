package com.prudhvilearning.round32.ui.screens

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.prudhvilearning.round32.R
import com.prudhvilearning.round32.ui.data.Message
import com.prudhvilearning.round32.ui.data.UserData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ChatScreen(user: UserData , navHostController: NavHostController) {
    var messages by remember {
        mutableStateOf(
            if (user.activeStatus) listOf(
                Message(
                    id = 0,
                    text = "Hi",
                    isSender = false,
                    timestamp = getCurrentTimeFormatted()
                )
            ) else emptyList()
        )
    }

    Scaffold(
        topBar = {
            TopBar(user = user , navHostController)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 👈 Correctly use innerPadding from Scaffold
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            ChatList(
                messages = messages,
                modifier = Modifier.weight(1f),
                user = user
            )

            ChatInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(15.dp)),
                onSend = { newMsg ->
                    if (newMsg.isNotBlank()) {
                        val newMessage = Message(
                            id = messages.size + 1,
                            text = newMsg,
                            isSender = true,
                            timestamp = getCurrentTimeFormatted()
                        )
                        messages = messages + newMessage
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showSystemUi = true)
@Composable
fun ChatScreenPreview() {
    val user = UserData(
        userName = "Prudhvi",
        userLocation = "Bangalore, Andhra Pradesh",
        matchPercentage = 98,
        profileUrl = "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c",
        activeStatus = true,
        age = 25
    )

    var messages by remember {
        mutableStateOf(
            if (user.activeStatus) listOf(
                Message(
                    id = 0,
                    text = "Hi",
                    isSender = false,
                    timestamp = getCurrentTimeFormatted()
                )
            ) else emptyList()
        )
    }

    Scaffold(
        topBar = {
            TopBar(user = user)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 👈 Correctly use innerPadding from Scaffold
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            ChatList(
                messages = messages,
                modifier = Modifier.weight(1f),
                user = user
            )

            ChatInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(15.dp)),
                onSend = { newMsg ->
                    if (newMsg.isNotBlank()) {
                        val newMessage = Message(
                            id = messages.size + 1,
                            text = newMsg,
                            isSender = true,
                            timestamp = getCurrentTimeFormatted()
                        )
                        messages = messages + newMessage
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    onSend: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    var showEmojiPicker by remember { mutableStateOf(false) }



    val maxLines = 4
    val lineHeight = 24.sp




    Row(
        modifier = modifier
            .background(Color(0xFF1E1E1E))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val speechLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.getOrNull(0)
            if (!spokenText.isNullOrEmpty()) {
                message += spokenText
            }
        }
        fun startSpeechRecognition() {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
            }
            speechLauncher.launch(intent)
        }
        if (showEmojiPicker) {
            ModalBottomSheet(
                onDismissRequest = { showEmojiPicker = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                val emojis = listOf("😀", "😂", "😍", "😭", "🤔", "🔥", "👍", "🎉", "❤️", "🙏")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(emojis.size) { index ->
                        Text(
                            text = emojis[index],
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    message += emojis[index] // append emoji to message
                                    showEmojiPicker = false
                                }
                        )
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ic_emojies),
            contentDescription = "Emoji",
            modifier = Modifier
                .size(30.dp)
                .padding(end = 8.dp)
                .clickable {
                showEmojiPicker = true
            }
        )

        Box(
            modifier = Modifier
                .weight(1f).align(Alignment.CenterVertically),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (message.isEmpty()) {
                Text(
                    text = "Start typing...",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
            }

            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = lineHeight
                ),
                maxLines = maxLines,
                cursorBrush = SolidColor(Color.White)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_mic),
            contentDescription = "Voice Input",
            modifier = Modifier
                .size(30.dp)
                .padding(end = 8.dp)
                .clickable {
                    startSpeechRecognition()
                }
        )

        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = Color.Red,
            modifier = Modifier
                .size(30.dp)
                .padding(start = 8.dp)
                .clickable {
                    if (message.isNotBlank()) {
                        onSend(message)
                        message = ""
                    }
                }
        )
    }
}





@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun TopBar(user: UserData , navHostController: NavHostController? = null) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)) { // Increased height for better spacing

        // Blurred background
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .graphicsLayer {
                    renderEffect = RenderEffect
                        .createBlurEffect(30f, 30f, Shader.TileMode.CLAMP)
                        .asComposeRenderEffect()
                }
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Like",
                tint = Color.White,
                modifier = Modifier.size(30.dp).clickable{navHostController?.popBackStack() },

            )

            Spacer(modifier = Modifier.width(10.dp))
            // Profile Image
            AsyncImage(
                model = user.profileUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            // Text info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = user.userName, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (user.activeStatus) "Online" else "Offline",
                        color = if (user.activeStatus) Color(0xFF4CAF50) else Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(if (user.activeStatus) Color.Green else Color.Yellow)
                    )
                }
            }

            // Icons
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Like",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Love",
                tint = Color.White,
                modifier = Modifier
                    .size(25.dp)
            )
        }
    }
}

@Composable
fun ChatList(messages: List<Message>, modifier: Modifier = Modifier, user: UserData) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        reverseLayout = true // so newest messages appear at bottom
    ) {
        items(messages.size) { index ->
            val message = messages[messages.size - 1 - index] // reverse order for bottom up

            ChatMessageItem(message = message, user = user)
        }
    }
}

@Composable
fun ChatMessageItem(message: Message, user: UserData) {
    val shape = when {
        message.isSender -> RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp) // sender bubble: bottom-left corner sharp
        else -> RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp) // receiver bubble: bottom-right corner sharp
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isSender) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom

    ) {
        if (!message.isSender) {
            AsyncImage(
                model = user.profileUrl,
                contentDescription = "Sender Profile",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .widthIn(max = 250.dp).padding(start = 8.dp)
        ) {
            Text(text = message.text, color = Color.White , modifier = Modifier.background(
                color = if (message.isSender) Color.Red else Color.Black,
                shape = shape
            ).padding(12.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray,
                modifier = Modifier.align(if (message.isSender) Alignment.End else Alignment.Start)
            )
        }

        if (message.isSender) {
            Spacer(modifier = Modifier.width(16.dp)) // space for profile on right if needed
        }
    }
}

fun getCurrentTimeFormatted(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date())
}





