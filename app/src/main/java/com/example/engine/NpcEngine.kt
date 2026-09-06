package com.example.engine

import com.example.model.NpcProfile
import java.util.Random

class NpcEngine(private val random: Random = Random()) {

    fun getDefaultNpcs(): List<NpcProfile> {
        return listOf(
            NpcProfile(
                id = "npc_oc_banerjee",
                name = "Inspector Anirban Banerjee",
                designation = "Inspector-in-Charge (IC)",
                roleType = "Senior Officer",
                personality = "Pragmatic, cautious, overburdened by district HQ calls, values clean paperwork.",
                goals = "Maintain station law & order without provoking political agitation; secure district promotion.",
                trustLevel = 58,
                reliability = 75,
                currentActivity = "Scrutinizing pending Case Diaries in IC chamber with hot tea."
            ),
            NpcProfile(
                id = "npc_si_soumen",
                name = "SI Soumen Chakraborty",
                designation = "Sub-Inspector (Second Officer)",
                roleType = "Colleague",
                personality = "Experienced, cynical, chain-smoker, deeply knowledgeable about local river-route smugglers.",
                goals = "Avoid departmental inquiries and complete his tenure before applying for Kolkata transfer.",
                trustLevel = 62,
                reliability = 70,
                currentActivity = "Flipping through the General Diary register while nursing a cigarette."
            ),
            NpcProfile(
                id = "npc_asi_dutta",
                name = "ASI Pradip Dutta",
                designation = "Assistant Sub-Inspector (Malkhana In-Charge)",
                roleType = "Subordinate",
                personality = "Meticulous about register entries, grumbles about lack of space in the storeroom.",
                goals = "Ensure not a single seized country-made firearm or bottle goes unaccounted.",
                trustLevel = 70,
                reliability = 84,
                currentActivity = "Auditing seized illicit liquor jars stacked beside the courtyard."
            ),
            NpcProfile(
                id = "npc_c_biswajit",
                name = "Constable Biswajit Das",
                designation = "Station Duty Constable",
                roleType = "Subordinate",
                personality = "Loyal, local dialect speaker, tired from consecutive 14-hour night-patrol shifts.",
                goals = "Secure sanctioned leave next Monday for his elder daughter's school admission in Midnapore.",
                trustLevel = 65,
                reliability = 80,
                currentActivity = "Cleaning the barrel of a .303 rifle in the guard room."
            ),
            NpcProfile(
                id = "npc_civic_dipak",
                name = "Civic Volunteer Dipak Roy",
                designation = "Civic Volunteer (Beat Assistant)",
                roleType = "Subordinate",
                personality = "Energetic, knows every tea stall owner and local rickshaw union head by name.",
                goals = "Get regularized duty rosters and earn occasional overtime stipends.",
                trustLevel = 54,
                reliability = 62,
                currentActivity = "Directing congested market auto-rickshaws near the Thana crossroad."
            ),
            NpcProfile(
                id = "npc_wife_swati",
                name = "Swati Mukherjee",
                designation = "Wife (School Teacher)",
                roleType = "Family",
                personality = "Patient but increasingly weary of unpredictable police duty hours and missed dinners.",
                goals = "Manage household expenses, ensure daughter's kindergarten education, get husband home safely.",
                trustLevel = 88,
                reliability = 92,
                currentActivity = "Marking exam answer sheets at home while monitoring her mother-in-law's fever."
            ),
            NpcProfile(
                id = "npc_adv_sen",
                name = "Advocate Debabrata Sen",
                designation = "Senior Defense Counsel (Sub-Divisional Court)",
                roleType = "Legal Professional",
                personality = "Sharp, polite on the surface, quick to identify technical flaws in seizure lists.",
                goals = "Obtain immediate bail for his clients on grounds of procedural non-compliance.",
                trustLevel = 42,
                reliability = 60,
                currentActivity = "Drafting a bail petition outside the Court Sub-Inspector's office."
            )
        )
    }

    /**
     * Generates a grounded, authentic dialogue conforming strictly to Rule 36:
     * 👮 SI Soumen
     * (irritated, checking the register)
     * "File-ta ekhono asheni?"
     */
    fun generateContextualDialogue(
        npc: NpcProfile,
        situationContext: String
    ): GeneratedDialogue {
        val dialogueTemplates = when (npc.id) {
            "npc_si_soumen" -> listOf(
                GeneratedDialogue(
                    speaker = "SI Soumen",
                    speakerRole = "Second Officer",
                    speakerAction = "irritated, checking the register with a red pen",
                    words = "\"Subrata, Court Sub-Inspector phone korechilo. Case Diary-te medical certificate add kora hoyni bolche. Ekhon abar Remand date extend korte hobe.\""
                ),
                GeneratedDialogue(
                    speaker = "SI Soumen",
                    speakerRole = "Second Officer",
                    speakerAction = "lighting a cheap bidi, blowing smoke towards the window",
                    words = "\"OC saheb ekhono DC office theke fereni. Bishnupur road-e ekta local dispute hoyeche shunlam. Tumi GD entry ta complete kore rekho, noile rate jhamela hobe.\""
                ),
                GeneratedDialogue(
                    speaker = "SI Soumen",
                    speakerRole = "Second Officer",
                    speakerAction = "tapping his fingers impatiently on the glass desk",
                    words = "\"Ei docket ta court production er jonno ready thaka dorkar. Peshkar babu dupur du-to baje chair chhere beriye jaben.\""
                )
            )

            "npc_oc_banerjee" -> listOf(
                GeneratedDialogue(
                    speaker = "Inspector A. Banerjee",
                    speakerRole = "Inspector-in-Charge",
                    speakerAction = "adjusting his spectacles, looking over his spectacles sternly",
                    words = "\"Mukherjee, local councilor-er bari theke phone eshechilo. Bhalo kore bujhe shuney handle korbe. Konorokom law & order deterioration jeno na hoy. Everything on GD register.\""
                ),
                GeneratedDialogue(
                    speaker = "Inspector A. Banerjee",
                    speakerRole = "Inspector-in-Charge",
                    speakerAction = "signing a sheaf of stationery requisitions without looking up",
                    words = "\"SP office theke pending warrant disposal list cheyeche. Last month-er execution report ta kobe pathabe? Ajker modhye table-e chai.\""
                )
            )

            "npc_asi_dutta" -> listOf(
                GeneratedDialogue(
                    speaker = "ASI Pradip Dutta",
                    speakerRole = "Malkhana In-Charge",
                    speakerAction = "wiping dust off a brass lock, frowning at the register",
                    words = "\"Sir, malkhana te ar space nei. 303 IPC-r cycle gulo brishtite jome ache baire. Seizure list e SI babu sign koreni bole Court e exhibit accept korche na.\""
                )
            )

            "npc_c_biswajit" -> listOf(
                GeneratedDialogue(
                    speaker = "Constable Biswajit",
                    speakerRole = "Station Duty",
                    speakerAction = "holding two glasses of sweet tea in a metal holder",
                    words = "\"Sir, cha enechi. Ar main gate-e ekta bhadralok darie ache, bolche bicycle churi hoyeche. GD likhbe naki complain formal nebo?\""
                )
            )

            "npc_wife_swati" -> listOf(
                GeneratedDialogue(
                    speaker = "Swati",
                    speakerRole = "Wife (over WhatsApp voice call)",
                    speakerAction = "speaking softly with domestic background sounds of vessels",
                    words = "\"Suno, Ma-er prescripton er omeprazole ar BP medicine ta sesh hoyeche. Ajki rath-e ashte parbe? Riya songe thakurmar golpo sunte cheyeche.\""
                )
            )

            else -> listOf(
                GeneratedDialogue(
                    speaker = npc.name,
                    speakerRole = npc.designation,
                    speakerAction = "speaking matter-of-factly",
                    words = "\"Official procedure ta time lagbe. Formal notice chara kaje progress kora jabe na.\""
                )
            )
        }

        return dialogueTemplates[random.nextInt(dialogueTemplates.size)]
    }
}

data class GeneratedDialogue(
    val speaker: String,
    val speakerRole: String,
    val speakerAction: String,
    val words: String
)
