package com.example.engine

import com.example.model.DocumentType
import com.example.model.OfficialDocument
import com.example.model.SimulationChoiceOption
import com.example.model.SimulationDecisionPrompt
import com.example.model.SimulationState
import java.util.Random

enum class EventRarity {
    ROUTINE_ORDINARY,    // 60 - 75%
    MINOR_DEVELOPMENT,   // 15 - 25%
    SIGNIFICANT_EVENT,   // 5 - 10%
    MAJOR_EVENT          // Rare
}

data class GeneratedEvent(
    val rarity: EventRarity,
    val title: String,
    val narrative: String,
    val isSuddenEvent: Boolean,
    val suddenPlace: String = "",
    val suddenSituation: String = "",
    val suddenTrigger: String = "",
    val triggersLiveMode: Boolean = false,
    val generatedDocument: OfficialDocument? = null,
    val decisionPrompt: SimulationDecisionPrompt? = null
)

class EventEngine(private val random: Random = Random()) {

    fun determineNextEvent(
        state: SimulationState,
        stepCount: Int
    ): GeneratedEvent {
        val roll = random.nextInt(100)

        return when {
            // 0 - 68: ROUTINE / ORDINARY LIFE (approx 68%)
            roll < 68 -> generateRoutineEvent(state, stepCount)

            // 69 - 88: MINOR DEVELOPMENTS (approx 20%)
            roll < 88 -> generateMinorDevelopment(state, stepCount)

            // 89 - 97: SIGNIFICANT EVENTS (approx 9%) - enters LIVE MODE +1 MINUTE
            roll < 98 -> generateSignificantSuddenEvent(state, stepCount)

            // 98 - 99: RARE MAJOR EVENT
            else -> generateRareInstitutionalEvent(state, stepCount)
        }
    }

    private fun generateRoutineEvent(state: SimulationState, stepCount: Int): GeneratedEvent {
        val routines = listOf(
            RoutineTemplate(
                title = "General Diary Entry — Routine Handover & Station Log",
                narrative = "The Duty Officer table is covered with carbon papers, register leaves, and blue ink bottles. Constable Biswajit brings the morning Station GD register for signatures after recording the night beat return and arms kote verification. You verify the entry for 14 service rifles and 120 rounds of 7.62 ammunition.",
                hasDoc = true,
                docType = DocumentType.GENERAL_DIARY_ENTRY,
                docTitle = "Extract of General Diary Entry No. 412/25",
                docContent = "G.D. Entry No. 412 dated 12/03/2025: At 08:14 hrs, SI S. Mukherjee took charge of the Station General Duty desk. Guard turned out. Arms & ammunition in kote verified correct: 14 Nos. .303 rifles, 2 Nos. 9mm pistols, 220 live rounds. Wireless RT set channel operating on District net without disturbance. No untoward incident during the preceding four hours."
            ),
            RoutineTemplate(
                title = "Case Diary Writing — Theft Case No. 84/25",
                narrative = "Sitting under the creaking Usha ceiling fan in the IO room, you open Case Diary Volume II for Kalyanpur PS Case No. 84/25 (u/s 303(2) BNS / 379 IPC). The electric pump stolen from an agricultural shallow tube-well in Joynagar mouza remains untraced. You note the non-availability of independent witnesses due to harvesting season.",
                hasDoc = true,
                docType = DocumentType.CASE_DIARY_PAGE,
                docTitle = "Case Diary No. 04 — Kalyanpur PS Case 84/25",
                docContent = "CD Para 12: I, SI S. Mukherjee, visited the Place of Occurrence at Joynagar village. Met local cultivator Haripada Mondal. Examined the broken lock of the pump-shed. The owner could not provide the engine chassis purchase invoice. Instructed Beat Civic Volunteer to maintain discreet surveillance at local scrap metal junkyards."
            ),
            RoutineTemplate(
                title = "Malkhana Property Scrutiny",
                narrative = "ASI Pradip Dutta requests your presence at the malkhana iron gate to inspect seized country liquor containers under the Bengal Excise Act. The damp smell of fermenting mahua hangs thick in the corridor. A court forwarding memo requires your initials before the magistrate's afternoon inspection.",
                hasDoc = true,
                docType = DocumentType.SEIZURE_LIST,
                docTitle = "Malkhana Property Inventory Extract — Ref Ex/2025/19",
                docContent = "Item: 3 jerrycans containing approximately 60 liters of suspected illicit distilled spirit seized u/s 46A Bengal Excise Act. Samples drawn in glass phials, labeled and sealed with station brass seal in presence of local witnesses. Handed over to ASI P. Dutta for safe custody."
            ),
            RoutineTemplate(
                title = "Civilian Verification Inquiry",
                narrative = "Two applicants for passport police verification and a retired headmaster seeking character certificate for a postal agency contract sit quietly on the wooden bench on the Thana verandah. You verify their Aadhaar cards, ration cards, and local voter lists against the district crime records index.",
                hasDoc = false
            ),
            RoutineTemplate(
                title = "Station Staff Duty Roster Consultation",
                narrative = "Second Officer SI Soumen reviews the upcoming Friday prayer and weekly haat duty roster. Two constables are down with viral fever, leaving the market mobile patrol shorthanded. You discuss reassigning civic volunteers without leaving the station lockup sentry unguarded.",
                hasDoc = false
            ),
            RoutineTemplate(
                title = "Inquest & Surathal Documentation — Sec 174 CrPC / 194 BNSS",
                narrative = "ASI Pradip Dutta submits the rough carbon draft of an Inquest (Surathal) report regarding an elderly villager found deceased near the river canal. You verify the witness signatures, body marks description, and prepare the Dead Body Challan for the Sub-divisional Hospital morgue autopsy.",
                hasDoc = true,
                docType = DocumentType.INQUEST_SURATHAL_REPORT,
                docTitle = "Inquest Report Excerpt (U/D Case No. 14/25)",
                docContent = "Surathal Report under Sec 174 CrPC / 194 BNSS: Deceased identified as Naren Halder, age approx 68 yrs. No external ligature or sharp injury marks detected. Panchas (local witnesses) agree death appears natural or cardiac failure. Body forwarded to Sub-divisional Hospital Morgue under escort of Constable No. 412 for post-mortem examination."
            ),
            RoutineTemplate(
                title = "Physical Strain & Health Break — Thana Verandah",
                narrative = "A sharp, burning sensation of acid reflux flares in your chest after two cups of sugary roadside tea and irregular tiffin meals. Your lumbar back aches from six hours in the wooden duty chair. You pause to drink a glass of water and take a Pantocid DSR antacid from your desk drawer before signing the night patrol log.",
                hasDoc = false
            )
        )

        val selected = routines[stepCount % routines.size]
        val doc = if (selected.hasDoc && selected.docType != null) {
            OfficialDocument(
                id = "doc_${System.currentTimeMillis()}_${random.nextInt(999)}",
                type = selected.docType,
                referenceNumber = "WBP/KLP/2025/${random.nextInt(899) + 100}",
                date = state.currentDate,
                time = state.currentTime,
                title = selected.docTitle,
                issuingAuthority = "Kalyanpur Police Station, Diamond Harbour",
                content = selected.docContent
            )
        } else null

        // Sometimes a routine matter poses a realistic institutional choice (every 3rd or 4th routine)
        val decision = if (stepCount % 3 == 0) {
            createRoutineDecision(stepCount)
        } else null

        return GeneratedEvent(
            rarity = EventRarity.ROUTINE_ORDINARY,
            title = selected.title,
            narrative = selected.narrative,
            isSuddenEvent = false,
            generatedDocument = doc,
            decisionPrompt = decision
        )
    }

    private fun generateMinorDevelopment(state: SimulationState, stepCount: Int): GeneratedEvent {
        val developments = listOf(
            MinorTemplate(
                title = "Boundary Dispute at Dakshin Raypur",
                narrative = "A village panchayat member and two agitated brothers arrive at the Thana regarding a disputed pond embankment boundary. Mud has been excavated, blocking a shared drainage canal. Voices were raised, but no physical violence has occurred.",
                decision = SimulationDecisionPrompt(
                    decisionId = "dec_boundary_${System.currentTimeMillis()}",
                    situation = "The complainant demands immediate registration of an FIR for criminal trespass, while the opposite party claims a civil court injunction is pending.",
                    options = listOf(
                        SimulationChoiceOption(
                            optionId = "opt_gd_inquiry",
                            text = "Record a General Diary entry and depute ASI for local spot inquiry",
                            description = "Follow standard procedure: avoid criminalizing civil boundary disputes prematurely.",
                            potentialTradeoff = "Complainant might accuse police of delay; maintains strict legal correctness.",
                            riskFactor = "Low risk of escalation if parties remain calm."
                        ),
                        SimulationChoiceOption(
                            optionId = "opt_advise_civil",
                            text = "Advise parties to maintain status quo and approach BDO/Civil Court",
                            description = "Directly clarify that title and boundary demarcation fall under Land Reforms revenue authorities.",
                            potentialTradeoff = "Refusing to intervene saves station manpower, but parties may quarrel again tonight.",
                            riskFactor = "Medium risk of minor scuffle requiring subsequent police intervention."
                        ),
                        SimulationChoiceOption(
                            optionId = "opt_mediate_thana",
                            text = "Summon both sides in the evening for informal mediation at the Thana",
                            description = "Attempt to defuse tension by hearing local elders in the presence of civic volunteer.",
                            potentialTradeoff = "Consumes 2 hours of duty time; police cannot legally enforce property agreements.",
                            riskFactor = "Risk of either party feeling the police took a biased informal stance."
                        )
                    )
                )
            ),
            MinorTemplate(
                title = "Notice Under Section 41A CrPC / 35 BNSS Compliance",
                narrative = "An accused in a matrimonial cruelty complaint (Sec 498A IPC / Sec 85 BNS) appears at the station with his advocate to comply with the statutory appearance notice. The advocate is carrying anticipatory bail documents and demands immediate acknowledgement on the notice duplicate copy.",
                decision = SimulationDecisionPrompt(
                    decisionId = "dec_notice41a_${System.currentTimeMillis()}",
                    situation = "The defense lawyer insists that the IO record their presence immediately and certify that the accused is cooperating, while the victim's family is on the phone questioning why the accused is not behind bars.",
                    options = listOf(
                        SimulationChoiceOption(
                            optionId = "opt_proper_interrogation",
                            text = "Conduct thorough questioning, record questionnaire answers, endorse notice",
                            description = "Adhere strictly to procedural guidelines: question thoroughly, verify address, endorse presence.",
                            potentialTradeoff = "Takes 90 minutes; complainant's family remains emotionally disgruntled.",
                            riskFactor = "Lowest legal risk; fully protects IO from contempt of court."
                        ),
                        SimulationChoiceOption(
                            optionId = "opt_quick_endorse",
                            text = "Briefly endorse presence on copy and schedule detailed statement next week",
                            description = "Quickly sign the receipt copy to clear the crowded desk and resume pending case diaries.",
                            potentialTradeoff = "Saves immediate time; defense counsel gets an easy clean chit of cooperation.",
                            riskFactor = "Complainant might file an affidavit alleging police collusion."
                        )
                    )
                )
            ),
            MinorTemplate(
                title = "Sub-Divisional Court Summons Dispatch",
                narrative = "Court Constable brings a bundle of 18 witness summons and 3 Non-Bailable Warrants (NBW) from the Sub-Divisional Judicial Magistrate (SDJM) Court. Two warrants pertain to individuals who have reportedly shifted to Mumbai for construction labor.",
                decision = null
            )
        )

        val selected = developments[stepCount % developments.size]
        return GeneratedEvent(
            rarity = EventRarity.MINOR_DEVELOPMENT,
            title = selected.title,
            narrative = selected.narrative,
            isSuddenEvent = false,
            decisionPrompt = selected.decision
        )
    }

    private fun generateSignificantSuddenEvent(state: SimulationState, stepCount: Int): GeneratedEvent {
        val suddenEvents = listOf(
            SuddenTemplate(
                place = "Kalyanpur-Bishnupur Highway near Netaji More",
                situation = "A loaded sand truck has suffered an axle breakdown across the narrow two-lane bridge, creating a 3 km vehicular gridlock. Commuter buses are blowing horns continuously; local e-rickshaw drivers are arguing over wrong-side driving.",
                trigger = "Heavy overloaded commercial movement from the river ghat combined with broken road culvert.",
                decision = SimulationDecisionPrompt(
                    decisionId = "dec_traffic_gridlock_${System.currentTimeMillis()}",
                    situation = "Traffic is spreading into village feeder lanes. Passengers in private buses are protesting in the heat.",
                    options = listOf(
                        SimulationChoiceOption(
                            optionId = "opt_divert_civics",
                            text = "Deploy 4 Civic Volunteers to divert light vehicles via rural loop road and requisition a recovery crane",
                            description = "Active traffic management using available auxiliary staff.",
                            potentialTradeoff = "Rural roads are narrow and dusty; villagers may protest heavy vehicle entry.",
                            riskFactor = "Medium chance crane arrival takes 45+ minutes."
                        ),
                        SimulationChoiceOption(
                            optionId = "opt_spot_command",
                            text = "Proceed personally in the patrol Bolero with one ASI to clear one lane manually",
                            description = "Direct officer presence to impose immediate physical discipline.",
                            potentialTradeoff = "Leaves station desk unattended; burns limited vehicle fuel quota.",
                            riskFactor = "High physical fatigue; direct confrontation with impatient truck drivers."
                        )
                    )
                )
            ),
            SuddenTemplate(
                place = "Joynagar Ghat Shibtala Bazar",
                situation = "Heated verbal altercation between two vegetable vendors over stall placement has drawn a crowd of 40 local onlookers. One person was pushed onto an empty wooden crate, suffering an abrasion on the elbow.",
                trigger = "Pre-haat morning rush for prime road-frontage spots following heavy morning rain.",
                decision = SimulationDecisionPrompt(
                    decisionId = "dec_bazar_brawl_${System.currentTimeMillis()}",
                    situation = "Crowd is swelling. One vendor belongs to the local market committee executive's family.",
                    options = listOf(
                        SimulationChoiceOption(
                            optionId = "opt_beat_intervention",
                            text = "Direct the Beat Mobile motorcycle patrol to separate the parties and bring them to Thana",
                            description = "Defuse spot commotion quickly without escalating into an operational raid.",
                            potentialTradeoff = "Both parties will sit in the Thana verandah for hours demanding counter-cases.",
                            riskFactor = "Low risk of further violence; high administrative paperwork."
                        ),
                        SimulationChoiceOption(
                            optionId = "opt_local_pradhan_resolve",
                            text = "Instruct Beat Constable to involve the Bazar Committee Secretary on spot to arbitrate",
                            description = "Utilize traditional local community dispute mechanism under police presence.",
                            potentialTradeoff = "Police maintain low profile; market committee asserts authority.",
                            riskFactor = "Slight risk of accusations of leaving law enforcement to private interests."
                        )
                    )
                )
            )
        )

        val selected = suddenEvents[stepCount % suddenEvents.size]
        return GeneratedEvent(
            rarity = EventRarity.SIGNIFICANT_EVENT,
            title = "Sudden Operational Development",
            narrative = selected.situation,
            isSuddenEvent = true,
            suddenPlace = selected.place,
            suddenSituation = selected.situation,
            suddenTrigger = selected.trigger,
            triggersLiveMode = true, // Rule 10: Enters LIVE MODE +1 MINUTE
            decisionPrompt = selected.decision
        )
    }

    private fun generateRareInstitutionalEvent(state: SimulationState, stepCount: Int): GeneratedEvent {
        return GeneratedEvent(
            rarity = EventRarity.MAJOR_EVENT,
            title = "Sudden Inspection Alert — SP / SDPO Station Visit",
            narrative = "Wireless RT crackles with an urgent message: The Superintendent of Police (SP), while en route to a district coordination conference, will conduct an unscheduled 15-minute halt at Kalyanpur PS to inspect the Malkhana register, Kote arms, and pending Warrant registers.",
            isSuddenEvent = true,
            suddenPlace = "Kalyanpur Police Station Main Compound",
            suddenSituation = "Wireless alert confirms SP convoy is 12 km away on the State Highway. Station orderlies and ASI are hurriedly straightening dusty register stacks.",
            suddenTrigger = "District administrative routine monitoring following quarterly review.",
            triggersLiveMode = true,
            decisionPrompt = SimulationDecisionPrompt(
                decisionId = "dec_sp_inspection_${System.currentTimeMillis()}",
                situation = "Inspector-in-Charge is at the Sub-Divisional Court. As senior Sub-Inspector on duty, you must present the station guard and registers.",
                options = listOf(
                    SimulationChoiceOption(
                        optionId = "opt_audit_kote_registers",
                        text = "Personally double-check the Kote register, Lockup Register, and Station GD volume",
                        description = "Ensure fundamental statutory records are up-to-date and signed.",
                        potentialTradeoff = "Malkhana physical yard might remain cluttered with dust and old seized vehicles.",
                        riskFactor = "Low risk; senior officers primarily examine GD entries and lockup numbers."
                    ),
                    SimulationChoiceOption(
                        optionId = "opt_compound_turnout",
                        text = "Turn out the station sentry in full ceremonial uniform and clear public verandah",
                        description = "Focus on institutional discipline, ceremonial guard, and compound tidiness.",
                        potentialTradeoff = "Any clerical mismatch inside registers will be immediately noticed.",
                        riskFactor = "Medium risk if inspection delves deeply into pending case diaries."
                    )
                )
            )
        )
    }

    private fun createRoutineDecision(stepCount: Int): SimulationDecisionPrompt {
        return SimulationDecisionPrompt(
            decisionId = "dec_routine_${System.currentTimeMillis()}",
            situation = "A tea vendor reports that a stray bag containing some clothes and a passbook was left behind on a bench near the station gate.",
            options = listOf(
                SimulationChoiceOption(
                    optionId = "opt_gd_property",
                    text = "Deposit the article under General Diary entry and record inventory",
                    description = "Follow Section 25 Police Act procedure for unclaimed property.",
                    potentialTradeoff = "Adds 30 minutes of administrative documentation for an ordinary bag.",
                    riskFactor = "Completely safe from any allegation of misappropriation."
                ),
                SimulationChoiceOption(
                    optionId = "opt_check_passbook_contact",
                    text = "Examine the passbook name to identify the owner and contact local post office",
                    description = "Proactive community assistance to return the bag before registering formal property.",
                    potentialTradeoff = "Informal action before formal GD entry might be technically irregular.",
                    riskFactor = "Owner might claim cash was in the bag if unrecorded."
                )
            )
        )
    }

    private data class RoutineTemplate(
        val title: String,
        val narrative: String,
        val hasDoc: Boolean,
        val docType: DocumentType? = null,
        val docTitle: String = "",
        val docContent: String = ""
    )

    private data class MinorTemplate(
        val title: String,
        val narrative: String,
        val decision: SimulationDecisionPrompt?
    )

    private data class SuddenTemplate(
        val place: String,
        val situation: String,
        val trigger: String,
        val decision: SimulationDecisionPrompt?
    )
}
