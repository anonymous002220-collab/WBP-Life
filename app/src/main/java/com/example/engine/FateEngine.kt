package com.example.engine

import com.example.model.SimulationState
import java.util.Random

enum class FateOutcomeType {
    ROUTINE_SUCCESS,
    PARTIAL_SUCCESS,
    ADMINISTRATIVE_DELAY,
    BUREAUCRATIC_BLOCKAGE,
    STAFF_SHORTAGE_SETBACK,
    UNEXPECTED_COMPLICATION,
    REPUTATION_FRICTION,
    QUIET_RESOLUTION,
    UNRELATED_DEVELOPMENT,
    COURT_RESCHEDULED
}

data class FateEvaluation(
    val outcomeType: FateOutcomeType,
    val narrative: String,
    val fatigueDelta: Int,
    val stressDelta: Int,
    val seniorRepDelta: Int,
    val civilianRepDelta: Int,
    val subordinateRepDelta: Int
)

/**
 * Probabilistic Fate Engine implementing Section 14, 34, and 35.
 * Realistic causality without plot armor and without artificial spite.
 */
class FateEngine(private val random: Random = Random()) {

    fun evaluateActionFate(
        choiceId: String,
        state: SimulationState
    ): FateEvaluation {
        val roll = random.nextInt(100)
        val fatigue = state.healthAndFatigue.fatigue
        val vehicleAvailable = state.resources.patrolVehiclesWorking > 0
        val constables = state.resources.constablesAvailable

        // Friction penalties from real-world conditions
        val frictionScore = (fatigue / 20) + (if (!vehicleAvailable) 2 else 0) + (if (constables < 4) 2 else 0)
        val adjustedRoll = (roll - (frictionScore * 4)).coerceIn(0, 99)

        return when {
            // 0 - 18: Bureaucratic delay / waiting / administrative friction
            adjustedRoll < 18 -> FateEvaluation(
                outcomeType = FateOutcomeType.ADMINISTRATIVE_DELAY,
                narrative = "File movement is stalled as the Court Inspector is occupied with Remand hearings and the Peshkar requested a revised forwarding memo.",
                fatigueDelta = 4,
                stressDelta = 5,
                seniorRepDelta = 0,
                civilianRepDelta = -1,
                subordinateRepDelta = 0
            )

            // 19 - 36: Staff shortage / vehicle unavailability setback
            adjustedRoll < 36 -> FateEvaluation(
                outcomeType = FateOutcomeType.STAFF_SHORTAGE_SETBACK,
                narrative = "Only one departmental vehicle was functional; it was abruptly diverted by OC for an emergency SDPO convoy pilot duty, requiring reliance on local rickshaws and walking.",
                fatigueDelta = 8,
                stressDelta = 6,
                seniorRepDelta = 0,
                civilianRepDelta = -2,
                subordinateRepDelta = 1
            )

            // 37 - 55: Partial progress with lingering procedural steps
            adjustedRoll < 55 -> FateEvaluation(
                outcomeType = FateOutcomeType.PARTIAL_SUCCESS,
                narrative = "Statements recorded under Section 161 CrPC / Section 180 BNSS, but independent panchas (local witnesses) hesitated to sign seizure papers without local panchayat consensus.",
                fatigueDelta = 5,
                stressDelta = 2,
                seniorRepDelta = 1,
                civilianRepDelta = 0,
                subordinateRepDelta = 1
            )

            // 56 - 82: Grounded institutional completion without fanfare
            adjustedRoll < 82 -> FateEvaluation(
                outcomeType = FateOutcomeType.ROUTINE_SUCCESS,
                narrative = "Procedural entries duly docketed in the General Diary and Case Diary volume. The paperwork was counter-signed by the Second Officer without objection.",
                fatigueDelta = 3,
                stressDelta = -2,
                seniorRepDelta = 2,
                civilianRepDelta = 1,
                subordinateRepDelta = 1
            )

            // 83 - 99: Unexpected complication or unrelated development
            else -> FateEvaluation(
                outcomeType = FateOutcomeType.UNEXPECTED_COMPLICATION,
                narrative = "While the inquiry was proceeding, an unannounced VIP movement order arrived over the RT network, requiring immediate static picket deployment along the highway.",
                fatigueDelta = 6,
                stressDelta = 7,
                seniorRepDelta = 0,
                civilianRepDelta = -1,
                subordinateRepDelta = -1
            )
        }
    }
}
