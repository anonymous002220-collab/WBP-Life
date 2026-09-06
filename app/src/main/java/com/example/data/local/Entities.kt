package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.DocumentType
import com.example.model.FeedItemType

@Entity(tableName = "simulation_state")
data class SimulationStateEntity(
    @PrimaryKey val simulationId: String,
    val officerName: String,
    val rank: String,
    val badgeNumber: String,
    val posting: String,
    val district: String,
    val yearsInService: Int,
    val salary: Int,
    val savings: Int,
    val currentDate: String,
    val currentDayIndex: Int,
    val currentTime: String,
    val currentMinutesOfDay: Int,
    val location: String,
    val weather: String,
    val dutyStatus: String,
    val workload: Int,
    val isLiveMode: Boolean,
    val liveModeMinutesRemaining: Int,
    val activeEventSummary: String?,
    // Health & Fatigue
    val fatigue: Int,
    val stress: Int,
    val sleepHours: Double,
    val sleepDebtHours: Double,
    val bloodPressure: String,
    val hydrationPercent: Int,
    val acidityLevel: Int,
    val heatExhaustionRisk: Int,
    val burnoutPercent: Int,
    val physicalAilment: String,
    val lastMealTime: String,
    val healthNotes: String,
    // Resources
    val dutyOfficersAvailable: Int,
    val constablesAvailable: Int,
    val civicVolunteers: Int,
    val patrolVehiclesWorking: Int,
    val patrolMotorcycles: Int,
    val fuelAvailableLiters: Int,
    val malkhanaSpacePercent: Int,
    val lockupOccupants: Int,
    // Reputations
    val repSeniors: Int,
    val repSubordinates: Int,
    val repPeers: Int,
    val repCivilians: Int,
    val repLocalCommunity: Int,
    val repLegalProfessionals: Int,
    val repOtherDepartments: Int,
    // Family
    val spouseName: String,
    val pendingFamilyIssue: String,
    val familyStress: Int,
    // Cycle meta
    val totalCyclesCompleted: Int,
    val lastUpdatedMs: Long
)

@Entity(tableName = "feed_items")
data class FeedItemEntity(
    @PrimaryKey val id: String,
    val simulationId: String,
    val timestampMs: Long,
    val type: FeedItemType,
    val date: String,
    val time: String,
    val location: String,
    val weather: String,
    val title: String,
    val body: String,
    val fromDate: String,
    val toDate: String,
    val fromTime: String,
    val toTime: String,
    val exactDuration: String,
    val duringTheGap: String,
    val eventPlace: String,
    val eventSituation: String,
    val eventTrigger: String,
    val speaker: String,
    val speakerRole: String,
    val speakerAction: String,
    val spokenWords: String,
    val documentId: String?,
    val decisionPromptJson: String?,
    val selectedOptionId: String?,
    val decisionResolved: Boolean
)

@Entity(tableName = "npc_records")
data class NpcEntity(
    @PrimaryKey val id: String,
    val simulationId: String,
    val name: String,
    val designation: String,
    val roleType: String,
    val personality: String,
    val goals: String,
    val trustLevel: Int,
    val reliability: Int,
    val currentActivity: String
)

@Entity(tableName = "case_records")
data class CaseEntity(
    @PrimaryKey val id: String,
    val simulationId: String,
    val caseNumber: String,
    val year: Int,
    val sectionOfLaw: String,
    val complainant: String,
    val accusedName: String,
    val stage: String,
    val briefFact: String,
    val lastDiaryDate: String,
    val ioName: String
)

@Entity(tableName = "official_documents")
data class OfficialDocumentEntity(
    @PrimaryKey val id: String,
    val simulationId: String,
    val type: DocumentType,
    val referenceNumber: String,
    val date: String,
    val time: String,
    val title: String,
    val issuingAuthority: String,
    val content: String,
    val isRead: Boolean = false
)
