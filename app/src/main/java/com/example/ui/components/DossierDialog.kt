package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaseRecord
import com.example.model.SimulationState
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.BrassGold
import com.example.ui.theme.PoliceNavyDark

@Composable
fun DossierDialog(
    state: SimulationState,
    cases: List<CaseRecord>,
    onHealthAction: (String) -> Unit = {},
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "SERVICE DOSSIER & STATION STATUS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PoliceNavyDark,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${state.character.rank} ${state.character.name} • ${state.character.badgeNumber}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dialog_dossier_content"),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Section 1: Clinical Health, Vitals & Workload (Rule 32: Health Realism)
                item {
                    StatusSectionHeader(title = "CLINICAL VITALS & HUMAN STRAIN")
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Blood Pressure & Sleep Summary Card
                        Surface(
                            color = Color(0xFF0F172A),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Blood Pressure:", color = Color.LightGray, fontSize = 11.sp)
                                    Text(
                                        text = state.healthAndFatigue.bloodPressure,
                                        color = BrassGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Cumulative Sleep Debt:", color = Color.LightGray, fontSize = 11.sp)
                                    Text(
                                        text = "${state.healthAndFatigue.sleepDebtHours} hrs",
                                        color = if (state.healthAndFatigue.sleepDebtHours > 8) AlertAmber else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Last Sleep / Meal:", color = Color.LightGray, fontSize = 11.sp)
                                    Text(
                                        text = "${state.healthAndFatigue.sleepHoursLastNight}h / ${state.healthAndFatigue.lastMealTime}",
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        MetricRow(
                            label = "Physical Fatigue",
                            value = "${state.healthAndFatigue.fatigue}%",
                            progress = state.healthAndFatigue.fatigue / 100f,
                            color = if (state.healthAndFatigue.fatigue > 60) AlertAmber else MaterialTheme.colorScheme.primary
                        )
                        MetricRow(
                            label = "Operational Stress",
                            value = "${state.healthAndFatigue.stress}%",
                            progress = state.healthAndFatigue.stress / 100f,
                            color = if (state.healthAndFatigue.stress > 60) AlertAmber else MaterialTheme.colorScheme.primary
                        )
                        MetricRow(
                            label = "Hydration Level",
                            value = "${state.healthAndFatigue.hydrationPercent}%",
                            progress = state.healthAndFatigue.hydrationPercent / 100f,
                            color = if (state.healthAndFatigue.hydrationPercent < 40) AlertAmber else Color(0xFF0284C7)
                        )
                        MetricRow(
                            label = "Gastric Acidity",
                            value = "${state.healthAndFatigue.acidityLevel}%",
                            progress = state.healthAndFatigue.acidityLevel / 100f,
                            color = if (state.healthAndFatigue.acidityLevel > 50) AlertAmber else Color(0xFFF59E0B)
                        )
                        MetricRow(
                            label = "Heat Exhaustion Risk",
                            value = "${state.healthAndFatigue.heatExhaustionRisk}%",
                            progress = state.healthAndFatigue.heatExhaustionRisk / 100f,
                            color = if (state.healthAndFatigue.heatExhaustionRisk > 50) AlertAmber else Color(0xFFEF4444)
                        )
                        MetricRow(
                            label = "Occupational Burnout",
                            value = "${state.healthAndFatigue.burnoutPercent}%",
                            progress = state.healthAndFatigue.burnoutPercent / 100f,
                            color = if (state.healthAndFatigue.burnoutPercent > 50) AlertAmber else Color(0xFF8B5CF6)
                        )

                        Text(
                            text = "Ailment: ${state.healthAndFatigue.physicalAilment}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Clinical Notes: ${state.healthAndFatigue.healthNotes}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Interactive Officer Health Interventions
                        Text(
                            text = "OFFICER HEALTH INTERVENTIONS:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PoliceNavyDark,
                            letterSpacing = 0.5.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onHealthAction("ANTACID") }
                                    .testTag("action_take_antacid")
                            ) {
                                Column(
                                    modifier = Modifier.padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("💊 Antacid", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("Pantocid DSR", fontSize = 8.5.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                }
                            }

                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onHealthAction("ORS_WATER") }
                                    .testTag("action_drink_ors")
                            ) {
                                Column(
                                    modifier = Modifier.padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("🥤 Drink ORS", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("Electrolytes", fontSize = 8.5.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                }
                            }

                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onHealthAction("REST_EYES") }
                                    .testTag("action_rest_eyes")
                            ) {
                                Column(
                                    modifier = Modifier.padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("🧘 Stretch", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("10m Eye Rest", fontSize = 8.5.sp, color = MaterialTheme.colorScheme.onTertiaryContainer)
                                }
                            }
                        }
                    }
                }

                // Section 2: Institutional Reputation
                item {
                    StatusSectionHeader(title = "INSTITUTIONAL REPUTATION")
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        MetricRow("Superior Officers", "${state.reputation.seniors}%", state.reputation.seniors / 100f, MaterialTheme.colorScheme.primary)
                        MetricRow("Subordinate Staff", "${state.reputation.subordinates}%", state.reputation.subordinates / 100f, MaterialTheme.colorScheme.primary)
                        MetricRow("Peers & Batchmates", "${state.reputation.peers}%", state.reputation.peers / 100f, MaterialTheme.colorScheme.primary)
                        MetricRow("Civilian Public", "${state.reputation.civilians}%", state.reputation.civilians / 100f, MaterialTheme.colorScheme.primary)
                        MetricRow("Local Community / Elders", "${state.reputation.localCommunity}%", state.reputation.localCommunity / 100f, MaterialTheme.colorScheme.primary)
                        MetricRow("Legal Professionals / Courts", "${state.reputation.legalProfessionals}%", state.reputation.legalProfessionals / 100f, MaterialTheme.colorScheme.primary)
                    }
                }

                // Section 3: Station Resources
                item {
                    StatusSectionHeader(title = "STATION RESOURCES (REAL CONSTRAINTS)")
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("• Duty Officers Available: ${state.resources.dutyOfficersAvailable}", fontSize = 11.sp)
                            Text("• Available Constables: ${state.resources.constablesAvailable} / 12 sanctioned", fontSize = 11.sp)
                            Text("• Civic Volunteers on Beat: ${state.resources.civicVolunteers}", fontSize = 11.sp)
                            Text("• Functional Patrol Vehicles: ${state.resources.patrolVehiclesWorking} (Fuel: ${state.resources.fuelAvailableLiters}L)", fontSize = 11.sp)
                            Text("• Malkhana Capacity Occupied: ${100 - state.resources.malkhanaSpaceAvailablePercent}%", fontSize = 11.sp)
                            Text("• Lockup Occupants: ${state.resources.lockupOccupants}", fontSize = 11.sp)
                        }
                    }
                }

                // Section 4: Personal Life & Finances
                item {
                    StatusSectionHeader(title = "PERSONAL LIFE & FINANCES")
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text("• Monthly Pay: ₹${state.character.salary} | Savings: ₹${state.character.savings}", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("• Spouse: ${state.familyState.spouseName} (${state.familyState.children})", fontSize = 11.sp)
                            Text("• Pending Household Issue: ${state.familyState.pendingFamilyIssue}", fontSize = 11.sp)
                            Text("• Domestic Strain Level: ${state.familyState.familyStress}%", fontSize = 11.sp)
                        }
                    }
                }

                // Section 5: Active Investigation Cases
                item {
                    StatusSectionHeader(title = "ACTIVE INVESTIGATION CASES")
                }

                if (cases.isEmpty()) {
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No active major case assigned at this desk today. Routine General Diary petitions under inquiry.",
                                fontSize = 11.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                } else {
                    items(cases) { c ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Case No. ${c.caseNumber}/${c.year} (${c.sectionOfLaw})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(text = "Complainant: ${c.complainant} | Accused: ${c.accusedName}", fontSize = 11.sp)
                                Text(text = "Stage: ${c.stage}", fontSize = 11.sp, color = BrassGold)
                                Text(text = "Brief: ${c.briefFact}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.testTag("btn_close_dossier")) {
                Text("CLOSE DOSSIER")
            }
        }
    )
}

@Composable
fun StatusSectionHeader(title: String) {
    Column {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
        HorizontalDivider(modifier = Modifier.padding(top = 2.dp, bottom = 4.dp))
    }
}

@Composable
fun MetricRow(label: String, value: String, progress: Float, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 11.sp)
            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = color
        )
    }
}
