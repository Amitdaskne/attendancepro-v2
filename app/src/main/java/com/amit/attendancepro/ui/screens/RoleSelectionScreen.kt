package com.amit.attendancepro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amit.attendancepro.domain.model.UserRole
import com.amit.attendancepro.ui.components.GlassBackground
import com.amit.attendancepro.ui.components.GlassCard
import com.amit.attendancepro.ui.theme.BlobPurple
import com.amit.attendancepro.ui.theme.TextPrimary
import com.amit.attendancepro.ui.theme.TextSecondary

@Composable
fun RoleSelectionScreen(onRoleSelected: (UserRole) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        GlassBackground()

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(BlobPurple, Color(0xFF00B894))))
                ) {
                    Icon(
                        Icons.Default.Fingerprint,
                        null,
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center).size(30.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        "AttendancePro",
                        color = TextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "Enterprise attendance system",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(48.dp))

            Text(
                "Select your role",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Choose how you want to sign in",
                color = TextSecondary,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(24.dp))

            UserRole.entries.forEach { role ->
                RoleCard(role = role, onClick = { onRoleSelected(role) })
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun RoleCard(role: UserRole, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.08f))
            ) {
                Icon(
                    role.icon,
                    null,
                    tint = TextPrimary,
                    modifier = Modifier.align(Alignment.Center).size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    role.displayName,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    role.description,
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
            Icon(
                Icons.Default.ArrowForward,
                null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
