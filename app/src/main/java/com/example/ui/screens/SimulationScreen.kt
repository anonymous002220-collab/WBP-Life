package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DossierDialog
import com.example.ui.components.FeedItemDispatcher
import com.example.ui.components.ModuleRegistryDialog
import com.example.ui.components.PersonnelDialog
import com.example.ui.components.StationHeader
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.BrassGold
import com.example.ui.theme.PoliceNavyDark

@Composable
fun SimulationScreen(
    viewModel: SimulationViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.simulationState.collectAsState()
    val feedItems by viewModel.feedItems.collectAsState()
    val isTicking by viewModel.isTicking.collectAsState()
    val npcs by viewModel.npcs.collectAsState()
    val cases by viewModel.cases.collectAsState()

    val showDossier by viewModel.showDossier.collectAsState()
    val showPersonnel by viewModel.showPersonnel.collectAsState()
    val showModules by viewModel.showModules.collectAsState()

    val listState = rememberLazyListState()

    // Smooth auto-scroll to the latest event as new documentary items stream in
    LaunchedEffect(feedItems.size) {
        if (feedItems.isNotEmpty()) {
            listState.animateScrollToItem(feedItems.size - 1)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("simulation_screen"),
        topBar = {
            StationHeader(
                state = state,
                isTicking = isTicking,
                onOpenDossier = { viewModel.openDossier() },
                onOpenNpcs = { viewModel.openPersonnel() },
                onOpenModules = { viewModel.openModules() }
            )
        },
        bottomBar = {
            // Institutional status bar at the bottom: Shows active heartbeat status or pending decision alert
            Surface(
                color = PoliceNavyDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.pendingDecision != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⚖️ ACTION REQUIRED: Choose an option above",
                                color = AlertAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isTicking) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(10.dp),
                                    strokeWidth = 1.5.dp,
                                    color = BrassGold
                                )
                                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                            }
                            Text(
                                text = if (state.isLiveMode) {
                                    "LIVE SITUATION MONITORING (+1 Min/Tick)"
                                } else {
                                    "DOCUMENTARY SIMULATION RUNNING (AUTONOMOUS TIME)"
                                },
                                color = Color(0xFFCBD5E1),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = "CYCLE #${state.totalCyclesCompleted}",
                        color = Color.LightGray,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (feedItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Opening General Diary and Station Records...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = feedItems,
                        key = { it.id }
                    ) { item ->
                        Box(modifier = Modifier.animateItem()) {
                            FeedItemDispatcher(
                                item = item,
                                onOptionSelected = { decId, optId ->
                                    viewModel.onOptionSelected(decId, optId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showDossier) {
        DossierDialog(
            state = state,
            cases = cases,
            onHealthAction = { actionType ->
                viewModel.performHealthIntervention(actionType)
            },
            onDismiss = { viewModel.closeDossier() }
        )
    }

    if (showPersonnel) {
        PersonnelDialog(
            npcs = npcs,
            onDismiss = { viewModel.closePersonnel() }
        )
    }

    if (showModules) {
        ModuleRegistryDialog(
            onDismiss = { viewModel.closeModules() }
        )
    }
}
