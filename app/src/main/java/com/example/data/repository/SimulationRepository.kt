package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.CaseEntity
import com.example.data.local.FeedItemEntity
import com.example.data.local.NpcEntity
import com.example.data.local.OfficialDocumentEntity
import com.example.data.local.SimulationStateEntity
import com.example.model.CaseRecord
import com.example.model.FamilyState
import com.example.model.HealthAndFatigue
import com.example.model.NpcProfile
import com.example.model.OfficialDocument
import com.example.model.PoliceOfficer
import com.example.model.ReputationScores
import com.example.model.SimulationChoiceOption
import com.example.model.SimulationDecisionPrompt
import com.example.model.SimulationFeedItem
import com.example.model.SimulationState
import com.example.model.StationResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

class SimulationRepository(private val database: AppDatabase) {
    private val dao = database.simulationDao()

    fun observeState(simulationId: String): Flow<SimulationState?> {
        return dao.getState(simulationId).map { entity ->
            entity?.let { toDomainState(it) }
        }
    }

    suspend fun getStateDirect(simulationId: String): SimulationState? {
        return dao.getStateDirect(simulationId)?.let { toDomainState(it) }
    }

    fun observeFeed(simulationId: String): Flow<List<SimulationFeedItem>> {
        return dao.getFeedItems(simulationId).map { list ->
            list.map { toDomainFeedItem(it) }
        }
    }

    fun observeNpcs(simulationId: String): Flow<List<NpcProfile>> {
        return dao.getAllNpcs(simulationId).map { list ->
            list.map {
                NpcProfile(
                    id = it.id,
                    name = it.name,
                    designation = it.designation,
                    roleType = it.roleType,
                    personality = it.personality,
                    goals = it.goals,
                    trustLevel = it.trustLevel,
                    reliability = it.reliability,
                    currentActivity = it.currentActivity
                )
            }
        }
    }

    fun observeCases(simulationId: String): Flow<List<CaseRecord>> {
        return dao.getAllCases(simulationId).map { list ->
            list.map {
                CaseRecord(
                    caseNumber = it.caseNumber,
                    year = it.year,
                    sectionOfLaw = it.sectionOfLaw,
                    complainant = it.complainant,
                    accusedName = it.accusedName,
                    stage = it.stage,
                    briefFact = it.briefFact,
                    lastDiaryDate = it.lastDiaryDate,
                    ioName = it.ioName
                )
            }
        }
    }

    fun observeDocuments(simulationId: String): Flow<List<OfficialDocument>> {
        return dao.getAllDocuments(simulationId).map { list ->
            list.map {
                OfficialDocument(
                    id = it.id,
                    type = it.type,
                    referenceNumber = it.referenceNumber,
                    date = it.date,
                    time = it.time,
                    title = it.title,
                    issuingAuthority = it.issuingAuthority,
                    content = it.content
                )
            }
        }
    }

    suspend fun saveState(state: SimulationState) {
        dao.insertState(toEntity(state))
    }

    suspend fun addFeedItem(item: SimulationFeedItem, simulationId: String) {
        dao.insertFeedItem(toEntity(item, simulationId))
    }

    suspend fun updateFeedItem(item: SimulationFeedItem, simulationId: String) {
        dao.updateFeedItem(toEntity(item, simulationId))
    }

    suspend fun saveNpcs(npcs: List<NpcProfile>, simulationId: String) {
        dao.insertNpcs(npcs.map {
            NpcEntity(
                id = it.id,
                simulationId = simulationId,
                name = it.name,
                designation = it.designation,
                roleType = it.roleType,
                personality = it.personality,
                goals = it.goals,
                trustLevel = it.trustLevel,
                reliability = it.reliability,
                currentActivity = it.currentActivity
            )
        })
    }

    suspend fun saveCases(cases: List<CaseRecord>, simulationId: String) {
        dao.insertCases(cases.map {
            CaseEntity(
                id = "${it.caseNumber}/${it.year}",
                simulationId = simulationId,
                caseNumber = it.caseNumber,
                year = it.year,
                sectionOfLaw = it.sectionOfLaw,
                complainant = it.complainant,
                accusedName = it.accusedName,
                stage = it.stage,
                briefFact = it.briefFact,
                lastDiaryDate = it.lastDiaryDate,
                ioName = it.ioName
            )
        })
    }

    suspend fun saveDocument(doc: OfficialDocument, simulationId: String) {
        dao.insertDocument(
            OfficialDocumentEntity(
                id = doc.id,
                simulationId = simulationId,
                type = doc.type,
                referenceNumber = doc.referenceNumber,
                date = doc.date,
                time = doc.time,
                title = doc.title,
                issuingAuthority = doc.issuingAuthority,
                content = doc.content,
                isRead = false
            )
        )
    }

    suspend fun markDocumentRead(docId: String) {
        dao.markDocumentRead(docId)
    }

    suspend fun resetSimulation(simulationId: String) {
        dao.deleteState(simulationId)
        dao.clearFeed(simulationId)
        dao.clearNpcs(simulationId)
        dao.clearCases(simulationId)
        dao.clearDocuments(simulationId)
    }

    // --- MAPPERS ---

    private fun toDomainState(entity: SimulationStateEntity): SimulationState {
        return SimulationState(
            simulationId = entity.simulationId,
            character = PoliceOfficer(
                name = entity.officerName,
                rank = entity.rank,
                badgeNumber = entity.badgeNumber,
                currentPosting = entity.posting,
                district = entity.district,
                yearsInService = entity.yearsInService,
                salary = entity.salary,
                savings = entity.savings
            ),
            currentDate = entity.currentDate,
            currentDayIndex = entity.currentDayIndex,
            currentTime = entity.currentTime,
            currentMinutesOfDay = entity.currentMinutesOfDay,
            location = entity.location,
            weather = entity.weather,
            dutyStatus = entity.dutyStatus,
            workload = entity.workload,
            isLiveMode = entity.isLiveMode,
            liveModeMinutesRemaining = entity.liveModeMinutesRemaining,
            activeEventSummary = entity.activeEventSummary,
            healthAndFatigue = HealthAndFatigue(
                fatigue = entity.fatigue,
                stress = entity.stress,
                sleepHoursLastNight = entity.sleepHours,
                sleepDebtHours = entity.sleepDebtHours,
                bloodPressure = entity.bloodPressure,
                hydrationPercent = entity.hydrationPercent,
                acidityLevel = entity.acidityLevel,
                heatExhaustionRisk = entity.heatExhaustionRisk,
                burnoutPercent = entity.burnoutPercent,
                physicalAilment = entity.physicalAilment,
                lastMealTime = entity.lastMealTime,
                healthNotes = entity.healthNotes
            ),
            resources = StationResources(
                dutyOfficersAvailable = entity.dutyOfficersAvailable,
                constablesAvailable = entity.constablesAvailable,
                civicVolunteers = entity.civicVolunteers,
                patrolVehiclesWorking = entity.patrolVehiclesWorking,
                patrolMotorcycles = entity.patrolMotorcycles,
                fuelAvailableLiters = entity.fuelAvailableLiters,
                malkhanaSpaceAvailablePercent = entity.malkhanaSpacePercent,
                lockupOccupants = entity.lockupOccupants
            ),
            reputation = ReputationScores(
                seniors = entity.repSeniors,
                subordinates = entity.repSubordinates,
                peers = entity.repPeers,
                civilians = entity.repCivilians,
                localCommunity = entity.repLocalCommunity,
                legalProfessionals = entity.repLegalProfessionals,
                otherDepartments = entity.repOtherDepartments
            ),
            familyState = FamilyState(
                spouseName = entity.spouseName,
                pendingFamilyIssue = entity.pendingFamilyIssue,
                familyStress = entity.familyStress
            ),
            totalCyclesCompleted = entity.totalCyclesCompleted
        )
    }

    private fun toEntity(state: SimulationState): SimulationStateEntity {
        return SimulationStateEntity(
            simulationId = state.simulationId,
            officerName = state.character.name,
            rank = state.character.rank,
            badgeNumber = state.character.badgeNumber,
            posting = state.character.currentPosting,
            district = state.character.district,
            yearsInService = state.character.yearsInService,
            salary = state.character.salary,
            savings = state.character.savings,
            currentDate = state.currentDate,
            currentDayIndex = state.currentDayIndex,
            currentTime = state.currentTime,
            currentMinutesOfDay = state.currentMinutesOfDay,
            location = state.location,
            weather = state.weather,
            dutyStatus = state.dutyStatus,
            workload = state.workload,
            isLiveMode = state.isLiveMode,
            liveModeMinutesRemaining = state.liveModeMinutesRemaining,
            activeEventSummary = state.activeEventSummary,
            fatigue = state.healthAndFatigue.fatigue,
            stress = state.healthAndFatigue.stress,
            sleepHours = state.healthAndFatigue.sleepHoursLastNight,
            sleepDebtHours = state.healthAndFatigue.sleepDebtHours,
            bloodPressure = state.healthAndFatigue.bloodPressure,
            hydrationPercent = state.healthAndFatigue.hydrationPercent,
            acidityLevel = state.healthAndFatigue.acidityLevel,
            heatExhaustionRisk = state.healthAndFatigue.heatExhaustionRisk,
            burnoutPercent = state.healthAndFatigue.burnoutPercent,
            physicalAilment = state.healthAndFatigue.physicalAilment,
            lastMealTime = state.healthAndFatigue.lastMealTime,
            healthNotes = state.healthAndFatigue.healthNotes,
            dutyOfficersAvailable = state.resources.dutyOfficersAvailable,
            constablesAvailable = state.resources.constablesAvailable,
            civicVolunteers = state.resources.civicVolunteers,
            patrolVehiclesWorking = state.resources.patrolVehiclesWorking,
            patrolMotorcycles = state.resources.patrolMotorcycles,
            fuelAvailableLiters = state.resources.fuelAvailableLiters,
            malkhanaSpacePercent = state.resources.malkhanaSpaceAvailablePercent,
            lockupOccupants = state.resources.lockupOccupants,
            repSeniors = state.reputation.seniors,
            repSubordinates = state.reputation.subordinates,
            repPeers = state.reputation.peers,
            repCivilians = state.reputation.civilians,
            repLocalCommunity = state.reputation.localCommunity,
            repLegalProfessionals = state.reputation.legalProfessionals,
            repOtherDepartments = state.reputation.otherDepartments,
            spouseName = state.familyState.spouseName,
            pendingFamilyIssue = state.familyState.pendingFamilyIssue,
            familyStress = state.familyState.familyStress,
            totalCyclesCompleted = state.totalCyclesCompleted,
            lastUpdatedMs = System.currentTimeMillis()
        )
    }

    private fun toDomainFeedItem(entity: FeedItemEntity): SimulationFeedItem {
        val decision = entity.decisionPromptJson?.let { parseDecision(it) }
        return SimulationFeedItem(
            id = entity.id,
            timestampMs = entity.timestampMs,
            type = entity.type,
            date = entity.date,
            time = entity.time,
            location = entity.location,
            weather = entity.weather,
            title = entity.title,
            body = entity.body,
            fromDate = entity.fromDate,
            toDate = entity.toDate,
            fromTime = entity.fromTime,
            toTime = entity.toTime,
            exactDuration = entity.exactDuration,
            duringTheGap = entity.duringTheGap,
            eventPlace = entity.eventPlace,
            eventSituation = entity.eventSituation,
            eventTrigger = entity.eventTrigger,
            speaker = entity.speaker,
            speakerRole = entity.speakerRole,
            speakerAction = entity.speakerAction,
            spokenWords = entity.spokenWords,
            decisionPrompt = decision,
            selectedOptionId = entity.selectedOptionId,
            decisionResolved = entity.decisionResolved
        )
    }

    private fun toEntity(item: SimulationFeedItem, simulationId: String): FeedItemEntity {
        return FeedItemEntity(
            id = item.id,
            simulationId = simulationId,
            timestampMs = item.timestampMs,
            type = item.type,
            date = item.date,
            time = item.time,
            location = item.location,
            weather = item.weather,
            title = item.title,
            body = item.body,
            fromDate = item.fromDate,
            toDate = item.toDate,
            fromTime = item.fromTime,
            toTime = item.toTime,
            exactDuration = item.exactDuration,
            duringTheGap = item.duringTheGap,
            eventPlace = item.eventPlace,
            eventSituation = item.eventSituation,
            eventTrigger = item.eventTrigger,
            speaker = item.speaker,
            speakerRole = item.speakerRole,
            speakerAction = item.speakerAction,
            spokenWords = item.spokenWords,
            documentId = item.document?.id,
            decisionPromptJson = item.decisionPrompt?.let { serializeDecision(it) },
            selectedOptionId = item.selectedOptionId,
            decisionResolved = item.decisionResolved
        )
    }

    private fun serializeDecision(prompt: SimulationDecisionPrompt): String {
        val json = JSONObject()
        json.put("decisionId", prompt.decisionId)
        json.put("situation", prompt.situation)
        val arr = JSONArray()
        for (opt in prompt.options) {
            val optObj = JSONObject()
            optObj.put("optionId", opt.optionId)
            optObj.put("text", opt.text)
            optObj.put("description", opt.description)
            optObj.put("potentialTradeoff", opt.potentialTradeoff)
            optObj.put("riskFactor", opt.riskFactor)
            arr.put(optObj)
        }
        json.put("options", arr)
        return json.toString()
    }

    private fun parseDecision(jsonStr: String): SimulationDecisionPrompt? {
        return try {
            val json = JSONObject(jsonStr)
            val decisionId = json.getString("decisionId")
            val situation = json.getString("situation")
            val arr = json.getJSONArray("options")
            val options = mutableListOf<SimulationChoiceOption>()
            for (i in 0 until arr.length()) {
                val optObj = arr.getJSONObject(i)
                options.add(
                    SimulationChoiceOption(
                        optionId = optObj.getString("optionId"),
                        text = optObj.getString("text"),
                        description = optObj.optString("description", ""),
                        potentialTradeoff = optObj.optString("potentialTradeoff", ""),
                        riskFactor = optObj.optString("riskFactor", "")
                    )
                )
            }
            SimulationDecisionPrompt(decisionId, situation, options)
        } catch (e: Exception) {
            null
        }
    }
}
