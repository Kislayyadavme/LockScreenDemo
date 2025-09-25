package com.example.lockscreendemo

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
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
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class LockActivity : ComponentActivity() {
    private val correctPin = "123456"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Make the activity fullscreen and keep screen awake
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        
        // Disable back button for this activity
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing - back button is disabled in lock screen
            }
        })

        setContent {
            LockScreenDemoTheme {
                LockScreenContent()
            }
        }
    }

    @Composable
    fun LockScreenContent() {
        val context = LocalContext.current
        var enteredPin by remember { mutableStateOf("") }
        var showError by remember { mutableStateOf(false) }
        var showSuccess by remember { mutableStateOf(false) }
        
        // Error animation
        val errorShake by animateFloatAsState(
            targetValue = if (showError) 1f else 0f,
            animationSpec = tween(300),
            label = "error_shake"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF1E293B),
                            Color(0xFF334155)
                        )
                    )
                )
        ) {
            // Blurred background effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(20.dp)
                    .alpha(0.3f)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF3B82F6).copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
            
            if (showSuccess) {
                SuccessScreen()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(80.dp))
                    
                    // Current time display
                    CurrentTimeDisplay()
                    
                    Spacer(modifier = Modifier.height(60.dp))
                    
                    // Demo notice
                    DemoNotice()
                    
                    Spacer(modifier = Modifier.height(40.dp))
                    
                    // PIN entry title
                    Text(
                        text = "Enter PIN",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // PIN dots display
                    PinDotsDisplay(
                        enteredLength = enteredPin.length,
                        maxLength = correctPin.length,
                        showError = showError,
                        errorShake = errorShake
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    // PIN keypad
                    PinKeypad(
                        onNumberClick = { number ->
                            if (enteredPin.length < correctPin.length) {
                                enteredPin += number
                                vibratePhone(context)
                                
                                if (enteredPin.length == correctPin.length) {
                                    if (enteredPin == correctPin) {
                                        showSuccess = true
                                    } else {
                                        showError = true
                                        vibrateError(context)
                                    }
                                }
                            }
                        },
                        onBackspaceClick = {
                            if (enteredPin.isNotEmpty()) {
                                enteredPin = enteredPin.dropLast(1)
                                vibratePhone(context)
                            }
                            showError = false
                        }
                    )
                    
                    // Reset error state after showing
                    LaunchedEffect(showError) {
                        if (showError) {
                            delay(1000)
                            showError = false
                            enteredPin = ""
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CurrentTimeDisplay() {
        val currentTime = remember { mutableStateOf(getCurrentTime()) }
        
        LaunchedEffect(Unit) {
            while (true) {
                currentTime.value = getCurrentTime()
                delay(1000) // Update every second
            }
        }
        
        Text(
            text = currentTime.value,
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun DemoNotice() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚠️ Demo Lock Screen",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "This is for demonstration only. You can still use your device power button to lock the screen normally.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun PinDotsDisplay(
        enteredLength: Int,
        maxLength: Int,
        showError: Boolean,
        errorShake: Float
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.offset(x = (errorShake * 10).dp, y = 0.dp)
        ) {
            repeat(maxLength) { index ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = when {
                                showError -> Color.Red
                                index < enteredLength -> Color.White
                                else -> Color.White.copy(alpha = 0.3f)
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }

    @Composable
    fun PinKeypad(
        onNumberClick: (String) -> Unit,
        onBackspaceClick: () -> Unit
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rows 1-3: Numbers 1-9
            for (row in 0..2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    for (col in 1..3) {
                        val number = (row * 3 + col).toString()
                        KeypadButton(
                            text = number,
                            onClick = { onNumberClick(number) }
                        )
                    }
                }
            }
            
            // Row 4: Empty, 0, Backspace
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Empty space
                Box(modifier = Modifier.size(72.dp))
                
                // Zero button
                KeypadButton(
                    text = "0",
                    onClick = { onNumberClick("0") }
                )
                
                // Backspace button
                KeypadButton(
                    onClick = onBackspaceClick,
                    isBackspace = true
                )
            }
        }
    }

    @Composable
    fun KeypadButton(
        text: String = "",
        onClick: () -> Unit,
        isBackspace: Boolean = false
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    Color.White.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isBackspace) {
                Icon(
                    imageVector = Icons.Default.Backspace,
                    contentDescription = "Backspace",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = text,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
            }
        }
    }

    @Composable
    fun SuccessScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "✅",
                fontSize = 72.sp,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "PIN Correct!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Demo completed successfully. Thank you for trying this app!",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = { finish() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Exit Demo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun vibratePhone(context: Context) {
        val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }

    private fun vibrateError(context: Context) {
        val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val pattern = longArrayOf(0, 100, 100, 100)
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            @Suppress("DEPRECATION")
            val pattern = longArrayOf(0, 100, 100, 100)
            vibrator.vibrate(pattern, -1)
        }
    }
}