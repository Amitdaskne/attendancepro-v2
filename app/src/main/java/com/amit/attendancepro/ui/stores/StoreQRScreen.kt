package com.amit.attendancepro.ui.stores

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amit.attendancepro.ui.components.GlassBackground
import com.amit.attendancepro.ui.components.GlassCard
import com.amit.attendancepro.ui.components.QRCodeView
import com.amit.attendancepro.ui.theme.TextPrimary
import com.amit.attendancepro.ui.theme.TextSecondary

@Composable
fun StoreQRScreen(
    storeId: String,
    onBack: () -> Unit,
    vm: StoreViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(storeId) { vm.loadStoreById(storeId) }

    Box(Modifier.fillMaxSize()) {
        GlassBackground()
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = TextPrimary)
                }
                Text("Store QR", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            when (val s = state) {
                is StoreUiState.Loading -> {
                    Spacer(Modifier.height(60.dp))
                    CircularProgressIndicator(color = Color.White)
                }
                is StoreUiState.SingleStore -> {
                    val store = s.store
                    if (store == null) {
                        Text("Store not found", color = Color(0xFFFF6B6B), fontSize = 14.sp)
                    } else {
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Store, null, tint = TextPrimary, modifier = Modifier.size(36.dp))
                                Spacer(Modifier.height(8.dp))
                                Text(store.name, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(store.address, color = TextSecondary, fontSize = 13.sp)
                                Spacer(Modifier.height(20.dp))
                                QRCodeView(content = store.qrToken)
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    "Employees scan this to mark attendance",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
                is StoreUiState.Error -> {
                    Text(s.message, color = Color(0xFFFF6B6B), fontSize = 13.sp)
                }
                else -> {}
            }
        }
    }
}
