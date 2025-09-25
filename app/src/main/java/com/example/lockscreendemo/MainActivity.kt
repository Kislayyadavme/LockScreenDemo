package com.example.lockscreendemo

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.lockscreendemo.ui.theme.LockScreenDemoTheme
import com.google.accompanist.permissions.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            startLockActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Make app fullscreen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        
        setContent {
            LockScreenDemoTheme {
                MainContent()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun MainContent() {
        val context = LocalContext.current
        var showSplash by remember { mutableStateOf(true) }
        var showPermissions by remember { mutableStateOf(false) }
        
        val requiredPermissions = listOf(
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.VIBRATE
        )
        
        val permissionsState = rememberMultiplePermissionsState(requiredPermissions)

        // Auto-progress through splash screen
        LaunchedEffect(Unit) {
            delay(3000) // Show splash for 3 seconds
            showSplash = false
            showPermissions = true
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF667EEA),
                            Color(0xFF764BA2)
                        )
                    )
                )
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = showSplash,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SplashScreen()
            }
            
            androidx.compose.animation.AnimatedVisibility(
                visible = showPermissions,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                PermissionScreen(
                    permissionsState = permissionsState,
                    onPermissionsGranted = { startLockActivity() }
                )
            }
        }
    }

    @Composable
    fun SplashScreen() {
        val infiniteTransition = rememberInfiniteTransition(label = "splash")
        
        // Animated values for the text
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )
        
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Animated background particles effect (simulated with circles)
            repeat(20) { index ->
                val particleAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.1f,
                    targetValue = 0.4f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = (1000..3000).random(),
                            delayMillis = index * 100
                        ),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "particle_$index"
                )
                
                Box(
                    modifier = Modifier
                        .offset(
                            x = ((-200..200).random()).dp,
                            y = ((-400..400).random()).dp
                        )
                        .size((10..30).random().dp)
                        .alpha(particleAlpha)
                        .background(
                            Color.White.copy(alpha = 0.3f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
            }
            
            // Main "Hello" text with animation
            Text(
                text = "Hello",
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                modifier = Modifier
                    .alpha(alpha)
                    .scale(scale),
                textAlign = TextAlign.Center
            )
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun PermissionScreen(
        permissionsState: MultiplePermissionsState,
        onPermissionsGranted: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Permissions Required",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "This app needs the following permissions to demonstrate its features:",
                        fontSize = 16.sp,
                        color = Color(0xFF374151),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Permission explanations
                    PermissionItem(
                        title = "Wake Lock",
                        description = "To keep screen awake during PIN entry demo"
                    )
                    
                    PermissionItem(
                        title = "Vibrate",
                        description = "For haptic feedback on PIN keypad"
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = {
                            if (permissionsState.allPermissionsGranted) {
                                onPermissionsGranted()
                            } else {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6)
                        )
                    ) {
                        Text(
                            text = if (permissionsState.allPermissionsGranted) {
                                "Continue to Demo"
                            } else {
                                "Grant Permissions"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Check permissions periodically
                    LaunchedEffect(permissionsState.allPermissionsGranted) {
                        if (permissionsState.allPermissionsGranted) {
                            delay(500) // Small delay for smooth transition
                            onPermissionsGranted()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PermissionItem(title: String, description: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        Color(0xFF10B981),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }

    private fun startLockActivity() {
        val intent = Intent(this, LockActivity::class.java)
        startActivity(intent)
        finish() // Close MainActivity
    }
}