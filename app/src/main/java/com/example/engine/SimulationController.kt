package com.example.engine

import com.example.data.repository.SimulationRepository
import com.example.engine.ai.PoliceAiProvider
import com.example.engine.ai.StandalonePoliceAiProvider
import com.example.model.FeedItemType
import com.example.model.HealthAndFatigue
import com.example.model.ReputationScores
import com.example.model.SimulationFeedItem
import com.example.model.SimulationState
import com.example.model.StationResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random

class SimulationController(
    private val repository: SimulationRepository,
    private val timeEngine: TimeEngine = TimeEngine(),
    private val fateEngine: FateEngine = FateEngine(),
    private val npcEngine: NpcEngine = NpcEngine(),
    private val eventEngine: EventEngine = EventEngine(),
    private val aiProvider: PoliceAiProvider = StandalonePoliceAiProvider(),
    private val scope: CoroutineScope
) {
    private val simId = "WBP-SIM-001"
    private val random = Random()

    private val _currentState = MutableStateFlow<SimulationState>(SimulationState(simulationId = simId))
    val currentState: StateFlow<SimulationState> = _currentState.asStateFlow()

    private val _isEngineTicking = MutableStateFlow(false)
    val isEngineTicking: StateFlow<Boolean> = _isEngineTicking.asStateFlow()

    private var heartbeatJob: Job? = null

    fun initializeSimulation() {
        scope.launch(Dispatchers.IO) {
            val existing = repository.getStateDirect(simId)
            if (existing != null) {
                _currentState.value = existing
            } else {
                val fresh = SimulationState(simulationId = simId)
                repository.saveState(fresh)
                repository.saveNpcs(npcEngine.getDefaultNpcs(), simId)
                _currentState.value = fresh

                // Initial Documentary Entry
                val initialIntro = SimulationFeedItem(
                    id = "init_${System.currentTimeMillis()}",
                    timestampMs = System.currentTimeMillis(),
                    type = FeedItemType.ROUTINE_EVENT,
                    date = fresh.currentDate,
                    time = fresh.currentTime,
                    location = fresh.location,
                    weather = fresh.weather,
                    title = "Commencement of General Diary Duty",
                    body = "📍 PLACE: ${fresh.location}\n" +
                            "📅 DATE: ${fresh.currentDate}\n" +
                            "⏰ TIME: ${fresh.currentTime}\n" +
                            "🌦️ WEATHER: ${fresh.weather}\n" +
                            "📊 STATUS: Day Duty Officer handover. Sentry inspection completed. General Diary volume open."
                )
                repository.addFeedItem(initialIntro, simId)
            }
            startAutonomousHeartbeat()
        }
    }

    fun startAutonomousHeartbeat() {
        if (heartbeatJob?.isActive == true) return

        heartbeatJob = scope.launch(Dispatchers.IO) {
            _isEngineTicking.value = true
            while (isActive) {
                // If a player decision is waiting, pause autonomous ticking until choice made
                if (_currentState.value.pendingDecision != null) {
                    delay(800)
                    continue
                }

                // Realistic documentary pause between autonomous life events (approx 4.5 seconds real time)
                delay(4500)

                executeAutonomousMasterCycle()
            }
            _isEngineTicking.value = false
        }
    }

    fun stopAutonomousHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
        _isEngineTicking.value = false
    }

    /**
     * Section 47: Master Execution Loop
     * Implements the 20-step execution cycle using Kotlin Coroutines:
     * 1. LOAD CURRENT STATE
     * 2. VALIDATE DATE/TIME CONTINUITY
     * 3. UPDATE WORLD
     * 4. UPDATE NPCs
     * 5. UPDATE PERSONAL LIFE
     * 6. UPDATE WORK
     * 7. UPDATE RESOURCES
     * 8. UPDATE REPUTATION
     * 9. UPDATE CAREER
     * 10. EVALUATE CONSEQUENCES
     * 11. DETERMINE TIME GAP
     * 12. AUTO-ADVANCE DATE/TIME
     * 13. CHECK FOR EMERGING EVENTS
     * 14. IF EVENT → STOP AT EXACT TIME
     * 15. SHOW SUDDEN EVENT
     * 16. ENTER LIVE +1 MINUTE MODE
     * 17. PROCESS EVENT
     * 18. UPDATE CONSEQUENCES
     * 19. SAVE STATE
     * 20. RENDER DOCUMENTARY RESPONSE
     * 21. CONTINUE AUTONOMOUSLY
     */
    private suspend fun executeAutonomousMasterCycle() {
        // Step 1: LOAD CURRENT STATE
        val state = repository.getStateDirect(simId) ?: _currentState.value
        val cycleIndex = state.totalCyclesCompleted + 1

        // Step 2: VALIDATE DATE/TIME CONTINUITY
        val currentMinutes = state.currentMinutesOfDay
        val currentDay = state.currentDayIndex

        if (state.isLiveMode && state.liveModeMinutesRemaining > 0) {
            // Steps 16 & 17: LIVE +1 MINUTE MODE (Rule 10)
            executeLiveMinuteStep(state, cycleIndex)
            return
        }

        // Steps 3 to 10: UPDATE WORLD, NPCs, PERSONAL LIFE, WORK, RESOURCES, REPUTATION, CAREER
        val worldUpdatedState = applyRoutineWorldDrift(state, cycleIndex)

        // Step 11: DETERMINE TIME GAP
        val isQuietLull = cycleIndex % 5 == 0
        val gapMinutes = timeEngine.determineAutonomousGapMinutes(
            currentMinutesOfDay = worldUpdatedState.currentMinutesOfDay,
            isQuietPeriod = isQuietLull
        )

        val fromDate = worldUpdatedState.currentDate
        val fromTime = worldUpdatedState.currentTime

        // Step 12: AUTO-ADVANCE DATE/TIME (Never rounded, never seconds)
        val timeResult = timeEngine.advanceTime(
            currentDayIndex = worldUpdatedState.currentDayIndex,
            currentMinutesOfDay = worldUpdatedState.currentMinutesOfDay,
            minutesToAdd = gapMinutes
        )

        // Step 13: CHECK FOR EMERGING EVENTS
        val nextEvent = eventEngine.determineNextEvent(worldUpdatedState, cycleIndex)

        // Step 14 & 15: IF EVENT → STOP AT EXACT TIME & SHOW SUDDEN EVENT
        val exactDuration = timeEngine.formatDuration(gapMinutes)
        val gapNarrative = aiProvider.generateGapNarrative(worldUpdatedState, gapMinutes, fromTime, timeResult.newTime)

        // Render Time Gap Transition Card adhering strictly to Rule 7
        val gapItem = SimulationFeedItem(
            id = "gap_${System.currentTimeMillis()}",
            timestampMs = System.currentTimeMillis(),
            type = FeedItemType.TIME_GAP,
            date = timeResult.newDate,
            time = timeResult.newTime,
            location = worldUpdatedState.location,
            weather = worldUpdatedState.weather,
            fromDate = fromDate,
            toDate = timeResult.newDate,
            fromTime = fromTime,
            toTime = timeResult.newTime,
            exactDuration = exactDuration,
            duringTheGap = gapNarrative,
            body = "Autonomous transition across $exactDuration."
        )
        repository.addFeedItem(gapItem, simId)

        if (nextEvent.isSuddenEvent) {
            // Steps 15 & 16: Render Sudden Event & Enter Live Mode
            val suddenItem = SimulationFeedItem(
                id = "sudden_${System.currentTimeMillis()}",
                timestampMs = System.currentTimeMillis() + 1,
                type = FeedItemType.SUDDEN_EVENT,
                date = timeResult.newDate,
                time = timeResult.newTime,
                location = nextEvent.suddenPlace,
                weather = worldUpdatedState.weather,
                title = "🚨 SUDDEN EVENT",
                body = "Emergent operational transmission on district wireless RT.",
                eventPlace = nextEvent.suddenPlace,
                eventSituation = nextEvent.suddenSituation,
                eventTrigger = nextEvent.suddenTrigger,
                decisionPrompt = nextEvent.decisionPrompt
            )
            repository.addFeedItem(suddenItem, simId)
        } else {
            // Steps 17 & 20: Routine / Document Rendering
            val eventItem = SimulationFeedItem(
                id = "event_${System.currentTimeMillis()}",
                timestampMs = System.currentTimeMillis() + 1,
                type = FeedItemType.ROUTINE_EVENT,
                date = timeResult.newDate,
                time = timeResult.newTime,
                location = worldUpdatedState.location,
                weather = worldUpdatedState.weather,
                title = nextEvent.title,
                body = "📍 PLACE: ${worldUpdatedState.location}\n" +
                        "📅 DATE: ${timeResult.newDate}\n" +
                        "⏰ TIME: ${timeResult.newTime}\n" +
                        "🌦️ WEATHER: ${worldUpdatedState.weather}\n" +
                        "📊 STATUS: ${nextEvent.narrative}",
                decisionPrompt = nextEvent.decisionPrompt
            )
            repository.addFeedItem(eventItem, simId)

            if (nextEvent.generatedDocument != null) {
                repository.saveDocument(nextEvent.generatedDocument, simId)
                val docItem = SimulationFeedItem(
                    id = "doc_feed_${System.currentTimeMillis()}",
                    timestampMs = System.currentTimeMillis() + 2,
                    type = FeedItemType.OFFICIAL_DOCUMENT,
                    date = timeResult.newDate,
                    time = timeResult.newTime,
                    location = worldUpdatedState.location,
                    weather = worldUpdatedState.weather,
                    title = nextEvent.generatedDocument.title,
                    body = nextEvent.generatedDocument.content,
                    document = nextEvent.generatedDocument
                )
                repository.addFeedItem(docItem, simId)
            }
        }

        // Autonomous Colleague Dialogue (Section 36)
        if (cycleIndex % 2 == 0 && nextEvent.decisionPrompt == null) {
            val npcs = npcEngine.getDefaultNpcs()
            val randomNpc = npcs[random.nextInt(npcs.size)]
            val dialogue = npcEngine.generateContextualDialogue(randomNpc, worldUpdatedState.location)

            val dialogueItem = SimulationFeedItem(
                id = "dial_${System.currentTimeMillis()}",
                timestampMs = System.currentTimeMillis() + 3,
                type = FeedItemType.DIALOGUE,
                date = timeResult.newDate,
                time = timeResult.newTime,
                location = worldUpdatedState.location,
                weather = worldUpdatedState.weather,
                body = "",
                speaker = dialogue.speaker,
                speakerRole = dialogue.speakerRole,
                speakerAction = dialogue.speakerAction,
                spokenWords = dialogue.words
            )
            repository.addFeedItem(dialogueItem, simId)
        }

        // Steps 18 & 19: UPDATE CONSEQUENCES & SAVE STATE
        val finalState = worldUpdatedState.copy(
            currentDayIndex = timeResult.newDayIndex,
            currentDate = timeResult.newDate,
            currentTime = timeResult.newTime,
            currentMinutesOfDay = timeResult.newMinutesOfDay,
            isLiveMode = nextEvent.triggersLiveMode,
            liveModeMinutesRemaining = if (nextEvent.triggersLiveMode) 10 else 0,
            activeEventSummary = if (nextEvent.triggersLiveMode) nextEvent.suddenSituation else null,
            pendingDecision = nextEvent.decisionPrompt,
            totalCyclesCompleted = cycleIndex,
            healthAndFatigue = worldUpdatedState.healthAndFatigue.copy(
                fatigue = (worldUpdatedState.healthAndFatigue.fatigue + if (gapMinutes > 60) 3 else 1).coerceAtMost(100)
            )
        )

        _currentState.value = finalState
        repository.saveState(finalState)
        // Step 21: CONTINUE AUTONOMOUSLY handled by startAutonomousHeartbeat loop
    }

    private suspend fun executeLiveMinuteStep(state: SimulationState, cycleIndex: Int) {
        val timeResult = timeEngine.advanceTime(
            currentDayIndex = state.currentDayIndex,
            currentMinutesOfDay = state.currentMinutesOfDay,
            minutesToAdd = 1 // Strict +1 Minute Tick
        )

        val remainingLive = state.liveModeMinutesRemaining - 1
        val exitLive = remainingLive <= 0

        val updatedState = state.copy(
            currentDayIndex = timeResult.newDayIndex,
            currentDate = timeResult.newDate,
            currentTime = timeResult.newTime,
            currentMinutesOfDay = timeResult.newMinutesOfDay,
            isLiveMode = !exitLive,
            liveModeMinutesRemaining = remainingLive,
            activeEventSummary = if (exitLive) null else state.activeEventSummary,
            healthAndFatigue = state.healthAndFatigue.copy(
                fatigue = (state.healthAndFatigue.fatigue + 1).coerceAtMost(100)
            ),
            totalCyclesCompleted = cycleIndex
        )

        _currentState.value = updatedState
        repository.saveState(updatedState)

        val liveFeed = SimulationFeedItem(
            id = "live_${System.currentTimeMillis()}",
            timestampMs = System.currentTimeMillis(),
            type = FeedItemType.ROUTINE_EVENT,
            date = updatedState.currentDate,
            time = updatedState.currentTime,
            location = updatedState.location,
            weather = updatedState.weather,
            title = if (exitLive) "Operational Situation Normalizing" else "Active Situation (+1 Min)",
            body = if (exitLive) {
                "Minute ${10 - remainingLive}: Spot tension has dissolved into routine paperwork. Local traffic movement restored. Exiting Live Mode."
            } else {
                "Minute ${10 - remainingLive}: Active spot monitoring in progress. Wireless RT reports local beat personnel maintaining perimeter."
            }
        )
        repository.addFeedItem(liveFeed, simId)
    }

    /**
     * Steps 3 to 10: Drift adjustments for world, personal life, resources, and workload.
     * Incorporates Health & Vitals Engine (BP, Hydration, Acidity, Sleep Debt, Burnout).
     */
    private fun applyRoutineWorldDrift(state: SimulationState, cycleIndex: Int): SimulationState {
        var fatigue = state.healthAndFatigue.fatigue
        var stress = state.healthAndFatigue.stress
        var familyStress = state.familyState.familyStress
        var acidity = state.healthAndFatigue.acidityLevel
        var hydration = state.healthAndFatigue.hydrationPercent
        var heatRisk = state.healthAndFatigue.heatExhaustionRisk
        var sleepDebt = state.healthAndFatigue.sleepDebtHours
        var burnout = state.healthAndFatigue.burnoutPercent

        // Fatigue and stress accumulate over duty cycles
        if (cycleIndex % 3 == 0) {
            fatigue = (fatigue + 2).coerceAtMost(100)
            stress = (stress + 1).coerceAtMost(100)
            acidity = (acidity + 2).coerceAtMost(100) // road tea & skipped meal drift
            sleepDebt = (sleepDebt + 0.15).coerceAtMost(24.0)
        }

        // Daytime hydration loss in Bengal humid climate
        val minutes = state.currentMinutesOfDay
        if (minutes in 600..1020 && cycleIndex % 4 == 0) { // 10:00 AM - 05:00 PM
            hydration = (hydration - 2).coerceAtLeast(15)
            heatRisk = (heatRisk + 2).coerceAtMost(100)
        }

        if (cycleIndex % 7 == 0) {
            familyStress = (familyStress + 2).coerceAtMost(100)
            burnout = (burnout + (if (stress > 50) 2 else 1)).coerceAtMost(100)
        }

        // Realistic clinical BP estimation based on stress, fatigue, and road tea
        val systolic = (120 + (stress * 0.22).toInt() + (fatigue * 0.12).toInt()).coerceIn(116, 168)
        val diastolic = (78 + (stress * 0.14).toInt()).coerceIn(74, 104)
        val dynamicBp = "$systolic/$diastolic mmHg"

        val note = when {
            fatigue > 70 -> "Severe physical exhaustion, heavy eyelids, lumbar pain from long desk duty."
            acidity > 60 -> "Acute gastric heartburn burning in esophagus from irregular canteen food and spicy snacks."
            heatRisk > 60 -> "Heavy sweating, salt loss, mild dizziness from humid afternoon beat."
            stress > 65 -> "High operational tension, throbbing headache, anxiety over pending diaries."
            else -> "Mild lumbar stiffness. Routine fatigue manageable with warm tea."
        }

        return state.copy(
            healthAndFatigue = state.healthAndFatigue.copy(
                fatigue = fatigue,
                stress = stress,
                acidityLevel = acidity,
                hydrationPercent = hydration,
                heatExhaustionRisk = heatRisk,
                sleepDebtHours = sleepDebt,
                burnoutPercent = burnout,
                bloodPressure = dynamicBp,
                healthNotes = note
            ),
            familyState = state.familyState.copy(
                familyStress = familyStress
            )
        )
    }

    /**
     * Allows officer to take authentic clinical & physical health actions:
     * - "ANTACID": Pantocid DSR / Gelusil for acidity
     * - "ORS_WATER": Electrolytes and water for dehydration
     * - "REST_EYES": 10-min posture stretch and rest
     */
    fun performHealthIntervention(actionType: String) {
        val state = _currentState.value
        scope.launch(Dispatchers.IO) {
            val (updatedHealth, message) = when (actionType) {
                "ANTACID" -> {
                    val newAcidity = (state.healthAndFatigue.acidityLevel - 28).coerceAtLeast(10)
                    state.healthAndFatigue.copy(
                        acidityLevel = newAcidity,
                        healthNotes = "Acid reflux subsided after taking Pantocid DSR with water."
                    ) to "Took Pantocid DSR antacid from desk drawer with warm water. Stomach burning sensations gradually subside."
                }
                "ORS_WATER" -> {
                    val newHydration = (state.healthAndFatigue.hydrationPercent + 32).coerceAtMost(100)
                    val newHeat = (state.healthAndFatigue.heatExhaustionRisk - 22).coerceAtLeast(5)
                    state.healthAndFatigue.copy(
                        hydrationPercent = newHydration,
                        heatExhaustionRisk = newHeat,
                        healthNotes = "Hydration restored with electrolytic lemon ORS."
                    ) to "Mixed a packet of Electral ORS in a stainless-steel glass. Cooled body temperature and alleviated dehydration."
                }
                "REST_EYES" -> {
                    val newStress = (state.healthAndFatigue.stress - 8).coerceAtLeast(5)
                    val newFatigue = (state.healthAndFatigue.fatigue - 6).coerceAtLeast(5)
                    state.healthAndFatigue.copy(
                        stress = newStress,
                        fatigue = newFatigue,
                        healthNotes = "10 minutes quiet eye rest in the officer restroom."
                    ) to "Stepped into the quiet Thana restroom for 10 minutes. Stretched lumbar spine and rested eyes away from registers."
                }
                else -> state.healthAndFatigue to "Health check completed."
            }

            val updatedState = state.copy(healthAndFatigue = updatedHealth)
            _currentState.value = updatedState
            repository.saveState(updatedState)

            val healthItem = SimulationFeedItem(
                id = "health_${System.currentTimeMillis()}",
                timestampMs = System.currentTimeMillis(),
                type = FeedItemType.ROUTINE_EVENT,
                date = updatedState.currentDate,
                time = updatedState.currentTime,
                location = updatedState.location,
                weather = updatedState.weather,
                title = "🩺 Health & Physiological Care",
                body = message
            )
            repository.addFeedItem(healthItem, simId)
        }
    }

    /**
     * Processes player choice immediately adhering to Rule 18 & 39:
     * - No "Continue?" confirmation prompt.
     * - Process immediately.
     * - Evaluate probabilistic fate (Section 14).
     * - Update reputation, fatigue, and consequences.
     * - Resume autonomous progression.
     */
    fun onPlayerChooseOption(decisionId: String, optionId: String) {
        val state = _currentState.value
        val prompt = state.pendingDecision ?: return
        val selectedOption = prompt.options.find { it.optionId == optionId } ?: return

        scope.launch(Dispatchers.IO) {
            val fate = fateEngine.evaluateActionFate(optionId, state)

            val decisionResolutionItem = SimulationFeedItem(
                id = "res_${System.currentTimeMillis()}",
                timestampMs = System.currentTimeMillis(),
                type = FeedItemType.CONSEQUENCE_REVELATION,
                date = state.currentDate,
                time = state.currentTime,
                location = state.location,
                weather = state.weather,
                title = "Decision Executed: ${selectedOption.text}",
                body = "⚖️ INSTITUTIONAL CONSEQUENCE:\n${fate.narrative}\n\n" +
                        "Trade-off: ${selectedOption.potentialTradeoff}"
            )
            repository.addFeedItem(decisionResolutionItem, simId)

            val newRep = state.reputation.copy(
                seniors = (state.reputation.seniors + fate.seniorRepDelta).coerceIn(0, 100),
                civilians = (state.reputation.civilians + fate.civilianRepDelta).coerceIn(0, 100),
                subordinates = (state.reputation.subordinates + fate.subordinateRepDelta).coerceIn(0, 100)
            )

            val newHealth = state.healthAndFatigue.copy(
                fatigue = (state.healthAndFatigue.fatigue + fate.fatigueDelta).coerceIn(0, 100),
                stress = (state.healthAndFatigue.stress + fate.stressDelta).coerceIn(0, 100)
            )

            val updatedState = state.copy(
                pendingDecision = null,
                reputation = newRep,
                healthAndFatigue = newHealth
            )

            _currentState.value = updatedState
            repository.saveState(updatedState)
        }
    }

    fun restartSimulation() {
        scope.launch(Dispatchers.IO) {
            repository.resetSimulation(simId)
            initializeSimulation()
        }
    }
}
