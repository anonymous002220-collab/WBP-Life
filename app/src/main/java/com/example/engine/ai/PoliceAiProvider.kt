package com.example.engine.ai

import com.example.model.SimulationState

/**
 * Clean interface isolating AI generation so the simulation engine can operate
 * with configurable backends (Standalone procedural simulation engine vs Gemini API).
 */
interface PoliceAiProvider {
    val providerName: String

    suspend fun generateGapNarrative(
        state: SimulationState,
        durationMinutes: Int,
        fromTime: String,
        toTime: String
    ): String

    suspend fun generateWorldConsequence(
        state: SimulationState,
        actionTaken: String
    ): String
}

/**
 * High-fidelity, deterministic standalone documentary reality simulation engine.
 * Runs completely on-device, zero latency, no network requirement, strict adherence to WBP realism.
 */
class StandalonePoliceAiProvider : PoliceAiProvider {
    override val providerName: String = "Autonomous WBP Documentary Simulation Engine"

    private val gapNarratives = listOf(
        "General Diary desks were occupied with carbon-paper counter entries. Second Officer scrutinized the daily wireless log. Civilian complainants for passport verification waited on the wooden verandah bench. A light drizzle swept through the courtyard, dampening the open vehicle shed.",
        "Station life proceeded at its natural administrative tempo. The sentry changed duty at the main gate with standard rifle inspection. Court Sub-Inspector's office transmitted the morning remand list via telephone. Constable brought sweet red tea from the corner stall.",
        "Routine filing of non-cognizable complaint petitions. ASI checked the seal of the property room. Heavy sand-laden trucks rumbled past the State Highway culvert outside, vibrating the glass panes of the Duty Officer room.",
        "Quiet operational lull in the Thana. You completed the pending paragraphs of Case Diary Volume II. Outside, village chowkidars gathered near the banyan tree for their weekly roster briefing.",
        "Afternoon heat settled over the rural police station. Wireless set maintained periodic district net checks. A local civic volunteer arrived on bicycle to submit the village market price report."
    )

    override suspend fun generateGapNarrative(
        state: SimulationState,
        durationMinutes: Int,
        fromTime: String,
        toTime: String
    ): String {
        val index = (state.totalCyclesCompleted + durationMinutes) % gapNarratives.size
        return gapNarratives[index]
    }

    override suspend fun generateWorldConsequence(
        state: SimulationState,
        actionTaken: String
    ): String {
        return "The decision was recorded in the station diary. Minor administrative ripple observed; local dynamics adjusted without immediate dramatic crisis."
    }
}
