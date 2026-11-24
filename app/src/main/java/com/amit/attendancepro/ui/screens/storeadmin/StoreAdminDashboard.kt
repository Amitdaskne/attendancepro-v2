package com.amit.attendancepro.ui.screens.storeadmin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amit.attendancepro.ui.components.GlassBackground
import com.amit.attendancepro.ui.components.GlassCard
import com.amit.attendancepro.ui.theme.TextPrimary
import com.amit.attendancepro.ui.theme.TextSecondary

@Composable
fun StoreAdminDashboard(onBack: () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        GlassBackground()
        Column(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) }
                Text("Store Admin", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(24.dp))
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Store, null, tint = TextPrimary, modifier = Modifier.size(56.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("Manage store", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    Text("Coming in Phase 4", color = TextSecondary, fontSize = 13.sp)
                }
            }
        }
    }
}
