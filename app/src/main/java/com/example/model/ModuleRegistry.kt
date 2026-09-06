package com.example.model

/**
 * Section 48 & 49: Permanent Module Registry.
 * Comprehensive 50-Module Autonomous Architecture.
 */
object ModuleRegistry {
    data class ModuleInfo(
        val code: String,
        val displayName: String,
        val category: String,
        val description: String
    )

    val MODULE_CATALOG = listOf(
        // Core Documentary & Anti-Cinematic Modules
        ModuleInfo("WBP_HYPER_REALISM", "WBP Hyper-Realism", "Core Documentary", "Enforces authentic reality over cinematic drama"),
        ModuleInfo("ANTI_CINEMATIC", "Anti-Cinematic Gate", "Core Documentary", "Blocks movie tropes, dramatic pacing, and sensationalism"),
        ModuleInfo("LOW_ACTION_REALISM", "Low-Action Realism", "Core Documentary", "Reflects administrative reality where 99% is paperwork and patience"),
        ModuleInfo("NO_FORCE_ESCALATION", "Non-Forced Escalation", "Core Documentary", "Situations resolve without forced gunfights or theatrical climaxes"),
        ModuleInfo("NO_REGULAR_RAID", "Raid Frequency Control", "Core Documentary", "Raids require statutory warrants, manpower, and SDPO sanction"),
        ModuleInfo("NO_REGULAR_PURSUIT", "Pursuit Restriction", "Core Documentary", "High-speed chases avoided due to narrow roads, pedestrians, and safety"),
        ModuleInfo("NO_REGULAR_FIRING", "Weapon Discharge Control", "Core Documentary", "Service firearm drawing requires extreme justification & magisterial inquiry"),
        ModuleInfo("GROUNDED_POLICING", "Grounded Field Policing", "Core Documentary", "Daily beat work, local disputes, and tea-stall listening"),
        ModuleInfo("ANTI_KING", "Anti-King Protocol", "Core Documentary", "Officer is an ordinary public servant, not an unchallengeable ruler"),
        ModuleInfo("ANTI_HERO", "Anti-Hero Logic", "Core Documentary", "Rejects lone-wolf myth; all actions depend on institutional hierarchy"),

        // Operational & Administrative Modules
        ModuleInfo("WBP_PROCEDURE_REALISM", "WBP Statutory Procedure", "Operations & Admin", "Strict adherence to PRB (Police Regulations Bengal) & BNS/BNSS"),
        ModuleInfo("DOCUMENT_AND_ADMIN_REALISM", "Thana Records Management", "Operations & Admin", "General Diary (GD), Case Diary (CD), Kote registers"),
        ModuleInfo("ULTRA_HARD_NEGOTIATION", "Field Dispute Mediation", "Operations & Admin", "Resolves boundary disputes and public altercations through dialogue"),
        ModuleInfo("NPC_AUTONOMY", "Autonomous Personnel System", "Operations & Admin", "Independent routines and motives for IC, SIs, ASIs, and Constables"),
        ModuleInfo("ANTI_RECOGNITION", "Anti-Instant Recognition", "Operations & Admin", "Routine hard work is rarely celebrated by superiors"),
        ModuleInfo("REPUTATION_CORE", "Multi-Sphere Reputation", "Operations & Admin", "Distinct tracking across seniors, peers, staff, courts, and public"),
        ModuleInfo("POSTING_CAREER", "Posting & Transfer Vulnerability", "Operations & Admin", "Vulnerability to sudden administrative transfers and routine postings"),
        ModuleInfo("FRICTION_CORE", "Institutional Red Tape", "Operations & Admin", "Budget constraints, fuel quotas, broken printers, court delays"),
        ModuleInfo("CONTINUOUS_WORLD_ENGINE", "Autonomous Background World", "Operations & Admin", "District events, festival rallies, and court calendars continue unprompted"),
        ModuleInfo("CIVIC_VOLUNTEER_DEPLOYMENT_ENGINE", "Civic Volunteer Deployment", "Operations & Admin", "Manages beat coordination, festival queues, and road surveillance"),

        // Health, Physiological & Human Strain Modules
        ModuleInfo("HEALTH_REALISM", "Physical Fatigue Simulation", "Health & Well-being", "Exhaustion increases with consecutive shifts and bad postures"),
        ModuleInfo("HEALTH_AND_VITALS_ENGINE", "Clinical Vitals Tracker", "Health & Well-being", "Monitors Blood Pressure, Hydration, Gastric Acidity, and Pulse"),
        ModuleInfo("SLEEP_DEBT_AND_INSOMNIA_ENGINE", "Sleep Debt Calculator", "Health & Well-being", "Accumulated sleep deprivation across odd-hour night shifts"),
        ModuleInfo("BURNOUT_AND_STRESS_ENGINE", "Occupational Burnout", "Health & Well-being", "Emotional toll of family distress, domestic cases, and workload"),
        ModuleInfo("HEAT_AND_CLIMATE_EXPOSURE_ENGINE", "Monsoon & Humid Heat Friction", "Health & Well-being", "Bengal humid heat, dehydration, rain rot on leather boots"),
        ModuleInfo("HARD_LIFE_FEEL", "Hard-Life Sensory Texture", "Health & Well-being", "Lukewarm tea, roadside dust, creaky wooden benches, mosquito smoke"),

        // Legal, Evidentiary & Court Procedures
        ModuleInfo("MALKHANA_AND_EVIDENCE_ENGINE", "Malkhana Seizure Custody", "Legal & Forensics", "Tracks seized vehicles, spirit registers, and chain of custody"),
        ModuleInfo("COURT_PRODUCTION_AND_BAIL_ENGINE", "Magisterial 24-Hr Production", "Legal & Forensics", "Strict compliance with statutory production deadlines & remand petitions"),
        ModuleInfo("FORENSIC_AND_POSTMORTEM_ENGINE", "Inquest & Surathal Module", "Legal & Forensics", "Sec 174 CrPC / 194 BNSS inquest reports and morgue challans"),
        ModuleInfo("LEGAL_AND_STATUTORY_SCRUTINY_ENGINE", "Public Prosecutor Scrutiny", "Legal & Forensics", "APP review of charge sheets, case diary defects, and summons service"),
        ModuleInfo("CYBER_AND_FINANCIAL_PETITION_ENGINE", "Cyber & Banking Petitions", "Legal & Forensics", "OTP fraud inquiries, CDR analysis requests, nodal officer liaison"),
        ModuleInfo("ARMS_AND_KOTE_MAINTENANCE_ENGINE", "Arms & Kote Register", "Legal & Forensics", "Ammunition stock accounting, musketry clearance, round inspections"),
        ModuleInfo("DEPARTMENTAL_PROCEEDINGS_ENGINE", "Conduct & Service Roll Review", "Legal & Forensics", "Good Service marks, explanation calls, and SP orderly room hearings"),
        ModuleInfo("ALL_CHOICE_CONSEQUENCES", "Strict Causal Chain", "Legal & Forensics", "No decision exists in a vacuum; every choice yields institutional impact"),

        // Territorial & Field Realism
        ModuleInfo("RURAL_INTELLIGENCE_ENGINE", "Rural Village Intelligence", "Territory & Beat", "Village chowkidars, gram panchayat pradhans, haat bazaar grapevines"),
        ModuleInfo("BEAT_PATROL_AND_VILLAGE_VISIT_ENGINE", "Kole & Mobile Beat Routine", "Territory & Beat", "Late-night naka checking, bridge pickets, and village visits"),
        ModuleInfo("ELECTION_AND_VIP_ROUTE_ENGINE", "VIP Protocol & Route Pickets", "Territory & Beat", "Escort movements, pilot cars, sudden diversions, static pickets"),
        ModuleInfo("COMMISSARY_AND_CANTEEN_ENGINE", "Police Mess & Canteen Supply", "Territory & Beat", "Mess committee rations, delayed tiffins, road stall credits"),
        ModuleInfo("CLIMATE_AND_MONSOON_ENGINE", "Bengal Weather & Flood Engine", "Territory & Beat", "Kalbaishakhi squalls, muddy kutcha roads, Hooghly river high tides"),
        ModuleInfo("FATE_DIVERSITY", "Multi-Variable Probabilistic Fate", "Territory & Beat", "Outcomes dictated by vehicle health, witness presence, and chance"),

        // Time, Date & Autonomous Progression Modules
        ModuleInfo("AUTO_TIME_PASS", "Autonomous Time Ticker", "Time & Continuity", "Time advances continuously without player manual skipping"),
        ModuleInfo("AUTO_DATE_PASS", "Autonomous Date Advancement", "Time & Continuity", "Calendar dates cycle realistically over days, weeks, and seasons"),
        ModuleInfo("EXACT_TIME_NO_ROUNDING", "Irregular Time Progression", "Time & Continuity", "Generates realistic unrounded times (e.g., 07:41 AM, 01:37 PM)"),
        ModuleInfo("TIME_GAP_DOCUMENTATION", "Standardized Gap Card Format", "Time & Continuity", "Explicit From/To Date, From/To Time, Duration, and Gap Narrative"),
        ModuleInfo("LIVE_PLUS_ONE_MINUTE", "Live Minute-by-Minute Loop", "Time & Continuity", "Emergency law-and-order events advance in strict +1 minute increments"),
        ModuleInfo("SUDDEN_EVENT_ENGINE", "Sudden Event Isolation", "Time & Continuity", "Separates Date, Time, Place, Weather, Situation, and Trigger"),
        ModuleInfo("SURVIVAL_HARD", "Institutional Longevity", "Time & Continuity", "Maintains equilibrium between departmental survival and integrity"),
        ModuleInfo("REALISM_NOT_FAILURE", "Balanced Non-Punitive Fate", "Time & Continuity", "Not designed to unfairly punish; reflects real statistical police life"),
        ModuleInfo("FAILURE_SETBACK", "Setbacks as Natural Friction", "Time & Continuity", "Unresolved cases, hostile witnesses, and procedural setbacks"),
        ModuleInfo("FAMILY_LIFE", "Family & Domestic Dynamics", "Time & Continuity", "Late arrivals, domestic tension, salary remittances, child schooling")
    )

    val PERMANENT_MODULES: List<String> = MODULE_CATALOG.map { it.code }

    fun getStatusJson(): String {
        return """
        {
          "MODULE_EXECUTION_STATUS": {
            "status": "ALWAYS_RUNNING",
            "persistent": true,
            "execute_every_cycle": true,
            "time_control": "AI_ONLY",
            "date_control": "AI_ONLY",
            "manual_time_skip": false,
            "manual_date_skip": false,
            "show_status": true,
            "active_module_count": ${PERMANENT_MODULES.size},
            "running_modules": [
              ${PERMANENT_MODULES.joinToString(",\n              ") { "\"$it\"" }}
            ]
          }
        }
        """.trimIndent()
    }
}
