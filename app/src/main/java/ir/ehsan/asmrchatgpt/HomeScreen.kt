package ir.ehsan.asmrchatgpt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.ehsan.asmrchatgpt.models.Message
import ir.ehsan.asmrchatgpt.models.fromUser
import ir.ehsan.asmrchatgpt.ui.theme.White200
import ir.ehsan.asmrchatgpt.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val anythingLoading by viewModel.loading.collectAsState()

    val (input, setInput) = remember {
        mutableStateOf("")
    }

    Scaffold(containerColor = Color.White, topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Chat GPT", color = Color.White)
                Text("Online", fontSize = 10.sp, color = White200)
            }
        }
    }) { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn() {
                items(messages) { message ->
                    Message(message)
                }
                if (anythingLoading) {
                    item {
                        Loading()
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(White200)
                    .padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(modifier = Modifier.weight(.9f), value = input, onValueChange = {
                    setInput(it)
                },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        textColor = Color.Gray,
                        placeholderColor = Color.Gray

                    ), placeholder = {
                        Text("Question")
                    })
                IconButton(modifier=Modifier.weight(.1f),onClick = {
                    if (input.isNotEmpty()){
                        viewModel.askQuestion(input)
                        setInput("")
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.Gray)
                }
            }
        }
    }


}

@Composable
fun Message(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topEnd = if (message.fromUser) 2.dp else 14.dp,
                        topStart = if (message.fromUser) 14.dp else 2.dp,
                        bottomStart = 14.dp,
                        bottomEnd = 14.dp
                    )
                )
                .background(if (message.fromUser) White200 else MaterialTheme.colorScheme.primary)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {

            Text(
                if (message.fromUser) "You" else "Chat GPT",
                fontSize = 10.sp,
                color = if (message.fromUser) Color(0xff414141) else White200
            )
            Text(
                message.content,
                color = if (message.fromUser) Color(0xff212121) else Color.White
            )
            Spacer(modifier = Modifier.height(5.dp))

        }
    }
}

@Composable
fun Loading() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 2.dp,
                        topEnd = 14.dp,
                        bottomEnd = 14.dp,
                        bottomStart = 14.dp
                    )
                )
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
        }
    }
}