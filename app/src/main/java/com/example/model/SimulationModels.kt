package com.example.model

/**
 * Core character representing an ordinary Sub-Inspector (or probationary officer)
 * serving in West Bengal Police.
 */
data class PoliceOfficer(
    val name: String = "Subrata Mukherjee",
    val rank: String = "Sub-Inspector of Police",
    val badgeNumber: String = "SI/WBP/7841",
    val currentPosting: String = "Kalyanpur Police Station",
    val district: String = "Diamond Harbour Police District",
    val yearsInService: Int = 4,
    val salary: Int = 41200, // Monthly salary in INR
    val savings: Int = 38500
)

/**
 * Distinct reputations tracked across different institutional spheres.
 */
data class ReputationScores(
    val seniors: Int = 54,        // 0 - 100
    val subordinates: Int = 62,
    val peers: Int = 58,
    val civilians: Int = 50,
    val localCommunity: Int = 48,
    val legalProfessionals: Int = 52,
    val otherDepartments: Int = 46
)

/**
 * Physical, physiological, and mental workload effects (Rule 32: Health Realism).
 */
data class HealthAndFatigue(
    val fatigue: Int = 32,                 // 0 (fresh) to 100 (exhausted)
    val stress: Int = 38,                  // 0 to 100
    val sleepHoursLastNight: Double = 5.5,
    val sleepDebtHours: Double = 9.5,      // Cumulative missed sleep over 72 hours
    val bloodPressure: String = "134/88 mmHg", // Mild pre-hypertension from stress & tea
    val hydrationPercent: Int = 54,        // 0 to 100
    val acidityLevel: Int = 42,            // 0 to 100 (gastric issue from late canteen meals)
    val heatExhaustionRisk: Int = 28,      // 0 to 100 (Bengal humidity & beat exposure)
    val burnoutPercent: Int = 36,          // 0 to 100
    val physicalAilment: String = "Lumbar spine stiffness from prolonged wooden chair duty & rough motorcycle beat",
    val lastMealTime: String = "08:15 AM",
    val healthNotes: String = "Mild acid reflux after morning road stall tea. Lumbar stiffness."
)

/**
 * Police station resources under real-world institutional constraints.
 */
data class StationResources(
    val dutyOfficersAvailable: Int = 3,
    val constablesAvailable: Int = 8,
    val civicVolunteers: Int = 6,
    val patrolVehiclesWorking: Int = 1, // Usually 1 Scorpio/Bolero and 2 motorcycles
    val patrolMotorcycles: Int = 2,
    val fuelAvailableLiters: Int = 18,
    val malkhanaSpaceAvailablePercent: Int = 22, // Malkhana is usually crowded
    val lockupOccupants: Int = 2
)

/**
 * Officer's family and personal obligations.
 */
data class FamilyState(
    val maritalStatus: String = "Married",
    val spouseName: String = "Swati Mukherjee",
    val children: String = "One daughter (Riya, age 4)",
    val residence: String = "Rental flat 14 km from Police Station",
    val monthlyRemittanceSent: Boolean = true,
    val pendingFamilyIssue: String = "Mother's blood pressure medicines need refill; wife requested home arrival before 9 PM if duty permits.",
    val familyStress: Int = 35
)

/**
 * Active police case in West Bengal Thana jurisdiction.
 */
data class CaseRecord(
    val caseNumber: String,
    val year: Int,
    val sectionOfLaw: String, // e.g. "Sec 379/411 IPC" or "Sec 303(2) BNS"
    val complainant: String,
    val accusedName: String,
    val stage: String, // "Under Investigation", "Notice Issued", "Case Diary Pending", "Court Production"
    val briefFact: String,
    val lastDiaryDate: String,
    val ioName: String = "SI Subrata Mukherjee"
)

/**
 * Authentic West Bengal Police document types.
 */
enum class DocumentType {
    GENERAL_DIARY_ENTRY,
    FIR_EXCERPT,
    CASE_DIARY_PAGE,
    SEIZURE_LIST,
    NOTICE_SECTION_41A,
    COURT_FORWARDING_REPORT,
    DUTY_ROSTER_EXTRACT,
    SUPERIOR_MEMO,
    INQUEST_SURATHAL_REPORT
}

data class OfficialDocument(
    val id: String,
    val type: DocumentType,
    val referenceNumber: String,
    val date: String,
    val time: String,
    val title: String,
    val issuingAuthority: String,
    val content: String
)

/**
 * NPC Profile for colleagues, superiors, magistrates, complainants, lawyers.
 */
data class NpcProfile(
    val id: String,
    val name: String,
    val designation: String, // e.g., "Inspector-in-Charge (IC)", "ASI", "Court Sub-Inspector (CSI)"
    val roleType: String,    // "Senior Officer", "Colleague", "Subordinate", "Civilian", "Judiciary", "Family"
    val personality: String,
    val goals: String,
    val trustLevel: Int = 50, // 0 to 100
    val reliability: Int = 65, // 0 to 100
    val currentActivity: String
)

/**
 * Choice options presented when a genuine operational or personal decision exists.
 */
data class SimulationChoiceOption(
    val optionId: String,
    val text: String,
    val description: String,
    val potentialTradeoff: String,
    val riskFactor: String
)

data class SimulationDecisionPrompt(
    val decisionId: String,
    val situation: String,
    val options: List<SimulationChoiceOption>
)

/**
 * Feed items displayed in the chronological documentary feed.
 */
enum class FeedItemType {
    ROUTINE_EVENT,
    TIME_GAP,
    SUDDEN_EVENT,
    DIALOGUE,
    OFFICIAL_DOCUMENT,
    DECISION_PROMPT,
    CONSEQUENCE_REVELATION,
    BACKGROUND_WORLD
}

data class SimulationFeedItem(
    val id: String,
    val timestampMs: Long,
    val type: FeedItemType,
    val date: String,
    val time: String,
    val location: String,
    val weather: String,
    val title: String = "",
    val body: String,
    // For TIME_GAP:
    val fromDate: String = "",
    val toDate: String = "",
    val fromTime: String = "",
    val toTime: String = "",
    val exactDuration: String = "",
    val duringTheGap: String = "",
    // For SUDDEN_EVENT:
    val eventPlace: String = "",
    val eventSituation: String = "",
    val eventTrigger: String = "",
    // For DIALOGUE:
    val speaker: String = "",
    val speakerRole: String = "",
    val speakerAction: String = "",
    val spokenWords: String = "",
    // For OFFICIAL_DOCUMENT:
    val document: OfficialDocument? = null,
    // For DECISION_PROMPT:
    val decisionPrompt: SimulationDecisionPrompt? = null,
    val selectedOptionId: String? = null,
    val decisionResolved: Boolean = false
)

/**
 * Complete persistent simulation state.
 */
data class SimulationState(
    val simulationId: String = "WBP-SIM-001",
    val character: PoliceOfficer = PoliceOfficer(),
    val currentDate: String = "Wednesday, 12th March 2025",
    val currentDayIndex: Int = 1,
    val currentTime: String = "07:41 AM",
    val currentMinutesOfDay: Int = 461, // 07:41 AM = 7*60 + 41 = 461
    val location: String = "Kalyanpur Police Station — Duty Officer Room",
    val weather: String = "Misty morning, 23°C, humid river breeze from Hooghly",
    val dutyStatus: String = "Day Officer / General Duty Roster",
    val workload: Int = 64, // 0 to 100
    val isLiveMode: Boolean = false,
    val liveModeMinutesRemaining: Int = 0,
    val activeEventSummary: String? = null,
    val healthAndFatigue: HealthAndFatigue = HealthAndFatigue(),
    val resources: StationResources = StationResources(),
    val reputation: ReputationScores = ReputationScores(),
    val familyState: FamilyState = FamilyState(),
    val pendingDecision: SimulationDecisionPrompt? = null,
    val unreadDocumentsCount: Int = 0,
    val totalCyclesCompleted: Int = 0
)
