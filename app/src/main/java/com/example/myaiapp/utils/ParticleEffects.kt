
// ============================================================
// ParticleEffects.kt
// تأثيرات جزيئية في Jetpack Compose
// ============================================================

package com.example.myaiapp.ui.effects

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ============================================================
// نموذج الجزيء
// ============================================================
data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var size: Float,
    var color: Color,
    var alpha: Float,
    var lifetime: Float = 1f
)

// ============================================================
# 1. تأثير الجزيئات العام
# ============================================================
@Composable
fun ParticleEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 50,
    colors: List<Color> = listOf(
        Color(0xFF4a90d9),
        Color(0xFF00ff88),
        Color(0xFF6bb6ff),
        Color(0xFFffffff)
    ),
    centerX: Float = 0.5f,
    centerY: Float = 0.5f
) {
    val particles = remember { mutableStateListOf<Particle>() }

    LaunchedEffect(Unit) {
        // تهيئة الجزيئات
        repeat(particleCount) {
            particles.add(createParticle(centerX, centerY, colors))
        }

        // تحديث الجزيئات
        while (true) {
            delay(16) // ~60 FPS

            particles.forEachIndexed { index, particle ->
                particle.x += particle.vx
                particle.y += particle.vy
                particle.lifetime -= 0.01f
                particle.alpha = particle.lifetime

                // إعادة التهيئة لو انتهى العمر
                if (particle.lifetime <= 0) {
                    particles[index] = createParticle(centerX, centerY, colors)
                }
            }
        }
    }

    Canvas(modifier = modifier) {
        particles.forEach { particle ->
            if (particle.alpha > 0) {
                drawCircle(
                    color = particle.color.copy(alpha = particle.alpha),
                    radius = particle.size,
                    center = Offset(
                        particle.x * size.width,
                        particle.y * size.height
                    )
                )

                // رسم الذيل
                drawLine(
                    color = particle.color.copy(alpha = particle.alpha * 0.3f),
                    start = Offset(
                        particle.x * size.width,
                        particle.y * size.height
                    ),
                    end = Offset(
                        (particle.x - particle.vx * 5) * size.width,
                        (particle.y - particle.vy * 5) * size.height
                    ),
                    strokeWidth = 1f
                )
            }
        }
    }
}

private fun createParticle(centerX: Float, centerY: Float, colors: List<Color>): Particle {
    val angle = Random.nextFloat() * 2 * Math.PI
    val speed = Random.nextFloat() * 0.01f + 0.002f

    return Particle(
        x = centerX,
        y = centerY,
        vx = cos(angle).toFloat() * speed,
        vy = sin(angle).toFloat() * speed - 0.005f, // لفوق
        size = Random.nextFloat() * 4f + 2f,
        color = colors.random(),
        alpha = Random.nextFloat() * 0.5f + 0.5f,
        lifetime = Random.nextFloat() * 0.5f + 0.5f
    )
}

// ============================================================
# 2. تأثير النجوم (Success Effect)
# ============================================================
@Composable
fun SuccessParticles(
    modifier: Modifier = Modifier,
    trigger: Boolean = false
) {
    val particles = remember { mutableStateListOf<Particle>() }
    var animationTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(trigger) {
        if (trigger) {
            animationTrigger++
            // إنشاء جزيئات الانفجار
            repeat(30) {
                val angle = Random.nextFloat() * 2 * Math.PI
                val speed = Random.nextFloat() * 0.02f + 0.01f
                particles.add(
                    Particle(
                        x = 0.5f,
                        y = 0.5f,
                        vx = cos(angle).toFloat() * speed,
                        vy = sin(angle).toFloat() * speed,
                        size = Random.nextFloat() * 6f + 3f,
                        color = listOf(
                            Color(0xFF00ff88),
                            Color(0xFF4a90d9),
                            Color(0xFFffd93d)
                        ).random(),
                        alpha = 1f,
                        lifetime = 1f
                    )
                )
            }
        }
    }

    LaunchedEffect(animationTrigger) {
        while (particles.isNotEmpty()) {
            delay(16)

            val iterator = particles.iterator()
            while (iterator.hasNext()) {
                val particle = iterator.next()
                particle.x += particle.vx
                particle.y += particle.vy
                particle.vy += 0.0005f // جاذبية
                particle.lifetime -= 0.02f
                particle.alpha = particle.lifetime

                if (particle.lifetime <= 0) {
                    iterator.remove()
                }
            }
        }
    }

    Canvas(modifier = modifier) {
        particles.forEach { particle ->
            if (particle.alpha > 0) {
                drawCircle(
                    color = particle.color.copy(alpha = particle.alpha),
                    radius = particle.size,
                    center = Offset(
                        particle.x * size.width,
                        particle.y * size.height
                    )
                )
            }
        }
    }
}

// ============================================================
# 3. تأثير الموجة الصوتية (Voice Wave)
# ============================================================
@Composable
fun VoiceWaveEffect(
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    intensity: Float = 0.5f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")

    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val baseRadius = size.minDimension * 0.3f

        // رسم 3 حلقات موجة
        repeat(3) { ringIndex ->
            val ringOffset = ringIndex * (2 * Math.PI / 3).toFloat()
            val animatedRadius = baseRadius + 
                sin(waveOffset + ringOffset) * 20f * intensity

            drawCircle(
                color = Color(0xFF4a90d9).copy(
                    alpha = (0.3f - ringIndex * 0.1f) * if (isActive) 1f else 0.3f
                ),
                radius = animatedRadius,
                center = Offset(centerX, centerY),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )
        }

        // نقطة المركز
        if (isActive) {
            drawCircle(
                color = Color(0xFFff6b6b),
                radius = baseRadius * 0.3f + sin(waveOffset * 2) * 5f,
                center = Offset(centerX, centerY)
            )
        }
    }
}

// ============================================================
# 4. تأثير التوهج (Glow Effect)
# ============================================================
@Composable
fun GlowEffect(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF4a90d9),
    intensity: Float = 0.5f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        // توهج خارجي
        drawCircle(
            color = color.copy(alpha = glowAlpha * intensity),
            radius = size.minDimension * 0.4f,
            center = Offset(centerX, centerY)
        )

        // توهج داخلي
        drawCircle(
            color = color.copy(alpha = glowAlpha * intensity * 1.5f),
            radius = size.minDimension * 0.25f,
            center = Offset(centerX, centerY)
        )
    }
}

// ============================================================
# 5. تأثير الكتابة (Typing Effect)
# ============================================================
@Composable
fun TypingDots(
    modifier: Modifier = Modifier,
    dotColor: Color = Color(0xFF4a90d9)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(16.dp)
    ) {
        repeat(3) { index ->
            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400, delayMillis = index * 150),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_$index"
            )

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .offset(y = offset.dp)
                    .background(dotColor, androidx.compose.foundation.shape.CircleShape)
            )
        }
    }
}

// ============================================================
# 6. تأثير الاهتزاز (Shake Effect)
# ============================================================
@Composable
fun ShakeEffect(
    modifier: Modifier = Modifier,
    shake: Boolean = false,
    content: @Composable () -> Unit
) {
    val shakeOffset by animateFloatAsState(
        targetValue = if (shake) 10f else 0f,
        animationSpec = keyframes {
            durationMillis = 500
            0f at 0
            (-10f) at 50
            10f at 100
            (-10f) at 150
            10f at 200
            (-5f) at 250
            5f at 300
            0f at 500
        },
        label = "shake"
    )

    Box(
        modifier = modifier.offset(x = shakeOffset.dp)
    ) {
        content()
    }
}

// ============================================================
# 7. تأثير التلاشي (Fade Effect)
# ============================================================
@Composable
fun FadeInEffect(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    durationMillis: Int = 500,
    content: @Composable () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis),
        label = "fade"
    )

    Box(
        modifier = modifier.alpha(alpha)
    ) {
        content()
    }
}

// ============================================================
# 8. تأثير الانزلاق (Slide Effect)
# ============================================================
@Composable
fun SlideInEffect(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    direction: SlideDirection = SlideDirection.UP,
    content: @Composable () -> Unit
) {
    val offset by animateFloatAsState(
        targetValue = if (visible) 0f else when (direction) {
            SlideDirection.UP -> 100f
            SlideDirection.DOWN -> -100f
            SlideDirection.LEFT -> 100f
            SlideDirection.RIGHT -> -100f
        },
        animationSpec = tween(500),
        label = "slide"
    )

    val xOffset = if (direction == SlideDirection.LEFT || direction == SlideDirection.RIGHT) offset.dp else 0.dp
    val yOffset = if (direction == SlideDirection.UP || direction == SlideDirection.DOWN) offset.dp else 0.dp

    Box(
        modifier = modifier.offset(x = xOffset, y = yOffset)
    ) {
        content()
    }
}

enum class SlideDirection {
    UP, DOWN, LEFT, RIGHT
}

// ============================================================
# الاستخدام في الشاشات:
# ============================================================

/*
@Composable
fun ChatScreen() {
    // تأثير الجزيئات في الخلفية
    ParticleEffect(
        modifier = Modifier.fillMaxSize(),
        particleCount = 30
    )

    // تأثير الموجة الصوتية
    VoiceWaveEffect(
        modifier = Modifier.size(100.dp),
        isActive = isRecording,
        intensity = audioLevel
    )

    // نقاط الكتابة
    if (isLoading) {
        TypingDots()
    }

    // تأثير النجاح
    SuccessParticles(
        trigger = showSuccess,
        modifier = Modifier.size(200.dp)
    )
}
*/
