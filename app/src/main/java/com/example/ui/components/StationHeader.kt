package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SimulationState
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.BrassGold
import com.example.ui.theme.PoliceNavyDark
import com.example.ui.theme.PoliceNavyMedium

@Composable
fun StationHeader(
    state: SimulationState,
    isTicking: Boolean,
    onOpenDossier: () -> Unit,
    onOpenNpcs: () -> Unit,
    onOpenModules: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("station_header"),
        color = PoliceNavyDark,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Top Bar: Emblems, Title & Quick Dialog Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // WBP Badge Token
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PoliceNavyMedium)
                            .border(1.dp, BrassGold, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "WBP",
                            color = BrassGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "WEST BENGAL POLICE",
                            color = BrassGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = state.character.currentPosting,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Action Icons (Dossier/Cases, Personnel, Health, Modules)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenDossier,
                        modifier = Modifier.testTag("btn_open_dossier")
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Station Dossier & Cases",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onOpenNpcs,
                        modifier = Modifier.testTag("btn_open_npcs")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Colleagues & Personnel",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onOpenModules,
                        modifier = Modifier.testTag("btn_open_modules")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "50-Module Autonomous Architecture",
                            tint = BrassGold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Officer & Duty Subtitle
            Text(
                text = "${state.character.rank} ${state.character.name} (${state.character.badgeNumber}) — ${state.dutyStatus}",
                color = Color.LightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Status strip: DATE, TIME, WEATHER, LIVE MODE / TICKING
            Surface(
                color = PoliceNavyMedium,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📅 ${state.currentDate}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        // Time: Exact irregular time, never rounded
                        Text(
                            text = "⏰ ${state.currentTime}",
                            color = BrassGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌦️ ${state.weather}",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp,
                            maxLines = 1
                        )

                        // Smooth pulsing mode indicator badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val dotColor = if (state.isLiveMode) AlertAmber else if (isTicking) Color(0xFF4ADE80) else Color.Gray
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .alpha(if (isTicking || state.isLiveMode) pulseAlpha else 1f)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (state.isLiveMode) "LIVE (+1m)" else if (isTicking) "DOCUMENTARY TICK" else "PAUSED",
                                color = if (state.isLiveMode) AlertAmber else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Interactive Health & Vitals Strip (Tap to view full Health Dossier)
            Surface(
                color = Color(0xFF0F2027),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A5F)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenDossier() }
                    .testTag("header_health_strip")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Health & Vitals",
                            tint = if (state.healthAndFatigue.fatigue > 60 || state.healthAndFatigue.stress > 60) AlertAmber else Color(0xFFEF4444),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BP: ${state.healthAndFatigue.bloodPressure}",
                            color = Color(0xFFF1F5F9),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⚡ Fatg: ${state.healthAndFatigue.fatigue}%",
                            color = if (state.healthAndFatigue.fatigue > 65) AlertAmber else Color(0xFFE2E8F0),
                            fontSize = 10.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "💧 Hyd: ${state.healthAndFatigue.hydrationPercent}%",
                            color = if (state.healthAndFatigue.hydrationPercent < 40) AlertAmber else Color(0xFF38BDF8),
                            fontSize = 10.5.sp
                        )
                    }

                    Text(
                        text = "Vitals ▸",
                        color = BrassGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
