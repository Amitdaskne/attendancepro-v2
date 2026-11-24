package com.amit.attendancepro.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amit.attendancepro.ui.components.GlassBackground
import com.amit.attendancepro.ui.components.GlassCard
import com.amit.attendancepro.ui.theme.TextPrimary
import com.amit.attendancepro.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    state: AuthState,
    onLogin: (String, String) -> Unit,
    onGoToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        GlassBackground()
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Fingerprint, null, tint = Color.White, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("Welcome back", color = TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Sign in to AttendancePro", color = TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(32.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Email", color = TextSecondary, fontSize = 12.sp)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = TextSecondary) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = fieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(14.dp))
                    Text("Password", color = TextSecondary, fontSize = 12.sp)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextSecondary) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = fieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (state is AuthState.Error) {
                Text(state.message, color = Color(0xFFFF6B6B), fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = { onLogin(email, password) },
                enabled = state !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                if (state is AuthState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp, color = Color.Black)
                } else {
                    Text("Sign In", color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onGoToSignup) {
                Text("New here? Create account", color = TextSecondary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedBorderColor = TextPrimary,
    unfocusedBorderColor = TextSecondary,
    cursorColor = TextPrimary
)
