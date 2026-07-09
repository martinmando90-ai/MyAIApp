package com.example.myaiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaiapp.ui.theme.MyAIAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAIAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0a0a1a)
                ) {
                    ChatScreen()
                }
            }
        }
    }
}

data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var currentMode by remember { mutableStateOf("Online") }

    // رسائل ترحيبية
    LaunchedEffect(Unit) {
        messages = listOf(
            ChatMessage(
                content = "👋 أهلاً بيك في مساعدك الذكي!\n\nأنا هنا عشان أساعدك في أي حاجة:\n• 💬 أسئلة وأجوبة\n• 🔍 بحث على الإنترنت\n• 📄 قراءة ملفات\n• 🔔 تذكيرات\n• 🎤 صوتي\n\nجرب تسألني أي حاجة!",
                isFromUser = false
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🤖 مساعدي الذكي", fontSize = 20.sp, color = Color.White)
                        Text(
                            text = if (currentMode == "Online") "🟢 $currentMode - Gemini Pro" else "📴 $currentMode - Gemini Nano",
                            fontSize = 12.sp,
                            color = if (currentMode == "Online") Color(0xFF00ff88) else Color(0xFFffd93d)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1a1a2e)
                ),
                actions = {
                    // زر تغيير الوضع
                    IconButton(onClick = {
                        currentMode = if (currentMode == "Online") "Offline" else "Online"
                    }) {
                        Icon(
                            imageVector = if (currentMode == "Online") 
                                Icons.Default.Cloud else Icons.Default.CloudOff,
                            contentDescription = "Mode",
                            tint = Color(0xFF4a90d9)
                        )
                    }
                    // زر المايك
                    IconButton(onClick = { /* Voice input */ }) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice",
                            tint = Color(0xFFff6b6b)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                color = Color(0xFF1a1a2e)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { 
                            Text("اكتب رسالتك هنا...", color = Color(0xFF4a4a6a)) 
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4a90d9),
                            unfocusedBorderColor = Color(0xFF2d2d44),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                // إضافة رسالة المستخدم
                                messages = messages + ChatMessage(
                                    content = inputText,
                                    isFromUser = true
                                )
                                val userQuery = inputText
                                inputText = ""
                                isLoading = true

                                // محاكاة رد الـ AI
                                messages = messages + ChatMessage(
                                    content = "...",
                                    isFromUser = false,
                                    isLoading = true
                                )

                                // رد بعد 2 ثانية
                                kotlinx.coroutines.GlobalScope.launch {
                                    kotlinx.coroutines.delay(2000)
                                    isLoading = false
                                    messages = messages.filter { !it.isLoading }

                                    val response = when {
                                        userQuery.contains("كيك") || userQuery.contains("cake") -> {
                                            "🍰 وصفة كيكة الشوكولاتة:\n\n1️⃣ اخلطي 2 كوب دقيق + 1 كوب كاكاو + 2 كوب سكر\n2️⃣ ضيفي 4 بيضات + 1 كوب زيت + 1 كوب حليب\n3️⃣ اخبزي على 180° لمدة 35 دقيقة\n\nبالهنا والشفا! 😋"
                                        }
                                        userQuery.contains("بحث") || userQuery.contains("دور") || userQuery.contains("search") -> {
                                            "🔍 نتائج البحث:\n\n📰 Google I/O 2026: Gemini Nano يدعم 100+ لغة\n📱 Android 16 يجلب AI محلي للجميع\n🤖 OpenAI تطلق GPT-5\n\nهل تريد تفاصيل أكتر عن أي خبر؟"
                                        }
                                        userQuery.contains("ذكر") || userQuery.contains("تذكير") || userQuery.contains("remind") -> {
                                            "✅ تم إضافة التذكير:\n\n📝 $userQuery\n⏰ غداً الساعة 9:00 صباحاً\n\nهفكرك إن شاء الله! 🔔"
                                        }
                                        userQuery.contains("ملف") || userQuery.contains("pdf") || userQuery.contains("doc") -> {
                                            "📄 قسم المستندات:\n\n• يمكنك رفع ملفات PDF, Word, TXT\n• هلخصلك المحتوى\n• هبحثلك جوه الملفات\n\nروح لقسم 📄 الملفات من القائمة السفلية"
                                        }
                                        userQuery.contains("صوت") || userQuery.contains("voice") -> {
                                            "🎤 المساعد الصوتي:\n\n• اضغط على زر المايك الأحمر\n• تكلم بالعربي أو الإنجليزي\n• هفهمك وهرد عليك\n\nجرب دلوقتي!"
                                        }
                                        else -> {
                                            "🤔 فهمت سؤالك!\n\n$userQuery\n\nده سؤال جميل! 💡\n\nأنا هنا عشان أساعدك في:\n• الإجابة على الأسئلة\n• البحث على الإنترنت\n• قراءة الملفات\n• التذكيرات\n• والمزيد...\n\nعايز تسأل عن حاجة تانية؟"
                                        }
                                    }

                                    messages = messages + ChatMessage(
                                        content = response,
                                        isFromUser = false
                                    )
                                }
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        containerColor = Color(0xFF4a90d9),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "إرسال",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                MessageBubble(message = message)
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isUser = message.isFromUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ))
                .then(
                    if (message.isLoading) {
                        Modifier
                    } else {
                        Modifier
                    }
                )
                .padding(12.dp)
                .widthIn(max = 300.dp)
        ) {
            if (message.isLoading) {
                // نقاط تحميل
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(3) { i ->
                        val offset by androidx.compose.animation.core.animateFloatAsState(
                            targetValue = if (i == 0) -5f else if (i == 1) 0f else 5f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                animation = androidx.compose.animation.core.tween(500),
                                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                            ),
                            label = "dot_$i"
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .offset(y = offset.dp)
                                .background(Color(0xFF4a90d9), RoundedCornerShape(4.dp))
                        )
                    }
                }
            } else {
                Text(
                    text = message.content,
                    color = if (isUser) Color.White else Color(0xFFc0c0c0),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
