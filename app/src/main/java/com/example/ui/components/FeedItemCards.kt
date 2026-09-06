package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FeedItemType
import com.example.model.SimulationFeedItem
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.BrassGold
import com.example.ui.theme.KhakiContainer
import com.example.ui.theme.KhakiOnContainer
import com.example.ui.theme.KhakiPrimary
import com.example.ui.theme.LedgerPaper
import com.example.ui.theme.PoliceNavyDark
import com.example.ui.theme.PoliceNavyMedium
import com.example.ui.theme.SealRed

@Composable
fun FeedItemDispatcher(
    item: SimulationFeedItem,
    onOptionSelected: (decisionId: String, optionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (item.type) {
        FeedItemType.TIME_GAP -> TimeGapCard(item = item, modifier = modifier)
        FeedItemType.SUDDEN_EVENT -> SuddenEventCard(item = item, onOptionSelected = onOptionSelected, modifier = modifier)
        FeedItemType.DIALOGUE -> DialogueCard(item = item, modifier = modifier)
        FeedItemType.OFFICIAL_DOCUMENT -> OfficialDocumentCard(item = item, modifier = modifier)
        FeedItemType.DECISION_PROMPT -> DecisionCard(item = item, onOptionSelected = onOptionSelected, modifier = modifier)
        FeedItemType.CONSEQUENCE_REVELATION -> ConsequenceCard(item = item, modifier = modifier)
        FeedItemType.ROUTINE_EVENT, FeedItemType.BACKGROUND_WORLD -> RoutineEventCard(
            item = item,
            onOptionSelected = onOptionSelected,
            modifier = modifier
        )
    }
}

/**
 * Section 7: Time Gap Documentation
 * 📅 FROM DATE: ...
 * 📅 TO DATE: ...
 * ⏰ FROM TIME: ...
 * ⏰ TO TIME: ...
 * ⌛ EXACT DURATION: ...
 * 📖 DURING THE GAP: ...
 */
@Composable
fun TimeGapCard(
    item: SimulationFeedItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("card_time_gap_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "⌛ AUTONOMOUS TIME PASSAGE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = item.exactDuration,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = KhakiPrimary
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )

            // Two-column or structured grid of From/To
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = "📅 FROM DATE: ${item.fromDate.ifBlank { item.date }}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "📅 TO DATE: ${item.toDate.ifBlank { item.date }}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "⏰ FROM TIME: ${item.fromTime.ifBlank { item.time }}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "⏰ TO TIME: ${item.toTime.ifBlank { item.time }}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "⌛ EXACT DURATION: ${item.exactDuration}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "📖 DURING THE GAP:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.duringTheGap.ifBlank { item.body },
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * Section 12: Sudden Event Presentation
 * 🚨 SUDDEN EVENT
 * 📅 DATE: ...
 * ⏰ TIME: ...
 * 📍 PLACE: ...
 * 🌦️ WEATHER: ...
 * 📊 SITUATION: ...
 * 🎲 TRIGGER: ...
 */
@Composable
fun SuddenEventCard(
    item: SimulationFeedItem,
    onOptionSelected: (decisionId: String, optionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("card_sudden_event_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, AlertAmber)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Alert Banner
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(AlertAmber)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🚨 SUDDEN EVENT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "EMERGENT DEVELOPMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AlertAmber
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mandatory Separate Parameters: Date, Time, Place, Weather
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "📅 DATE: ${item.date}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "⏰ TIME: ${item.time}",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = AlertAmber
                )
                Text(
                    text = "📍 PLACE: ${item.eventPlace.ifBlank { item.location }}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "🌦️ WEATHER: ${item.weather}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Situation
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "📊 SITUATION:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.eventSituation.ifBlank { item.body },
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (item.eventTrigger.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🎲 TRIGGER: ${item.eventTrigger}",
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // If a decision prompt is attached to this sudden event
            item.decisionPrompt?.let { prompt ->
                Spacer(modifier = Modifier.height(12.dp))
                DecisionOptionButtons(prompt = prompt, onOptionSelected = onOptionSelected)
            }
        }
    }
}

/**
 * Section 36: Dialogue Format
 * 👮 SI Soumen
 * (irritated, checking the register)
 * "File-ta ekhono asheni?"
 */
@Composable
fun DialogueCard(
    item: SimulationFeedItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("card_dialogue_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(6.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Left Khaki Stripe
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(KhakiPrimary)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "👮 ${item.speaker}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (item.speakerRole.isNotBlank()) {
                        Text(
                            text = " (${item.speakerRole})",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (item.speakerAction.isNotBlank()) {
                    Text(
                        text = "(${item.speakerAction})",
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.spokenWords.ifBlank { item.body },
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Section 37: Official Document / Station Register Card
 */
@Composable
fun OfficialDocumentCard(
    item: SimulationFeedItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("card_doc_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = LedgerPaper
        ),
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7B7A3))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Government / Police Stationery Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GOVERNMENT OF WEST BENGAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PoliceNavyDark,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = item.title.ifBlank { "OFFICIAL MEMO" },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SealRed
                    )
                }

                // Official Seal Badge
                Box(
                    modifier = Modifier
                        .border(1.dp, SealRed, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "OFFICIAL RECORD",
                        color = SealRed,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFD3C7B5))

            // Body in crisp monospaced or structured format
            Text(
                text = item.body,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 17.sp,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ref: ${item.document?.referenceNumber ?: "GD/2025"}",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "${item.date} • ${item.time}",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * Section 38: Standard Documentary Feed Card
 */
@Composable
fun RoutineEventCard(
    item: SimulationFeedItem,
    onOptionSelected: (decisionId: String, optionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .testTag("card_routine_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            if (item.title.isNotBlank()) {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Text(
                text = item.body,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            item.decisionPrompt?.let { prompt ->
                Spacer(modifier = Modifier.height(10.dp))
                DecisionOptionButtons(prompt = prompt, onOptionSelected = onOptionSelected)
            }
        }
    }
}

@Composable
fun DecisionCard(
    item: SimulationFeedItem,
    onOptionSelected: (decisionId: String, optionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("card_decision_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "⚖️ OPERATIONAL DECISION REQUIRED",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.body,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            item.decisionPrompt?.let { prompt ->
                Spacer(modifier = Modifier.height(10.dp))
                DecisionOptionButtons(prompt = prompt, onOptionSelected = onOptionSelected)
            }
        }
    }
}

@Composable
fun ConsequenceCard(
    item: SimulationFeedItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .testTag("card_consequence_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, BrassGold)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = item.title.ifBlank { "⚖️ INSTITUTIONAL CONSEQUENCE" },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.body,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Section 18 & 39: Interactive Choice Buttons with explicit Trade-offs and Risks.
 * Immediately invokes onOptionSelected when tapped (no "Continue?" prompt).
 */
@Composable
fun DecisionOptionButtons(
    prompt: com.example.model.SimulationDecisionPrompt,
    onOptionSelected: (decisionId: String, optionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "DECISION OPTIONS:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        prompt.options.forEach { option ->
            OutlinedButton(
                onClick = { onOptionSelected(prompt.decisionId, option.optionId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_choice_${option.optionId}"),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = option.text,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (option.description.isNotBlank()) {
                        Text(
                            text = option.description,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (option.potentialTradeoff.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "⚖️ Trade-off: ${option.potentialTradeoff}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (option.riskFactor.isNotBlank()) {
                        Text(
                            text = "⚠️ Risk: ${option.riskFactor}",
                            fontSize = 10.sp,
                            color = AlertAmber
                        )
                    }
                }
            }
        }
    }
}
