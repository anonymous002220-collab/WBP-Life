package com.example.engine

import java.util.Random

/**
 * Handles autonomous fictional date and time progression adhering strictly to:
 * - Rule 5: Autonomous AI time control
 * - Rule 6: Exact irregular time, never rounded, never seconds
 * - Rule 7: Time gap documentation
 * - Rule 8: Date continuity across midnight
 * - Rule 10: Live mode exact +1 minute increments
 */
class TimeEngine(private val random: Random = Random()) {

    private val dayNames = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )

    private val monthNames = listOf(
        "March", "April", "May", "June", "July", "August", "September"
    )

    /**
     * Converts minute of day (0 to 1439) into formatted irregular time string without seconds.
     * Example: 461 -> "07:41 AM", 817 -> "01:37 PM"
     */
    fun formatTime(minutesOfDay: Int): String {
        val normalized = ((minutesOfDay % 1440) + 1440) % 1440
        val hour24 = normalized / 60
        val minute = normalized % 60
        val amPm = if (hour24 < 12) "AM" else "PM"
        val hour12 = when (hour24) {
            0 -> 12
            in 1..12 -> hour24
            else -> hour24 - 12
        }
        return String.format("%02d:%02d %s", hour12, minute, amPm)
    }

    /**
     * Advances time by exact minutes. Handles midnight transitions and updates the date string.
     */
    fun advanceTime(
        currentDayIndex: Int,
        currentMinutesOfDay: Int,
        minutesToAdd: Int
    ): TimeAdvanceResult {
        val totalMinutes = currentMinutesOfDay + minutesToAdd
        val daysAdvanced = totalMinutes / 1440
        val newMinutesOfDay = totalMinutes % 1440
        val newDayIndex = currentDayIndex + daysAdvanced

        val newDate = formatDateForDayIndex(newDayIndex)
        val newTime = formatTime(newMinutesOfDay)

        return TimeAdvanceResult(
            newDayIndex = newDayIndex,
            newDate = newDate,
            newMinutesOfDay = newMinutesOfDay,
            newTime = newTime,
            daysPassed = daysAdvanced
        )
    }

    /**
     * Computes an irregular duration in minutes based on the world context:
     * - Routine paperwork/desk: 23 to 58 minutes
     * - Quiet lull / post-lunch: 74 to 143 minutes
     * - Night shift quiet hours: 187 to 312 minutes
     * Always ensures irregular minutes (e.g., 29 mins, 47 mins, not 30 or 60).
     */
    fun determineAutonomousGapMinutes(currentMinutesOfDay: Int, isQuietPeriod: Boolean): Int {
        val baseMinutes = when {
            isQuietPeriod -> {
                // Between 1 hr 19 mins and 2 hrs 43 mins
                79 + random.nextInt(85)
            }
            currentMinutesOfDay in 420..780 -> {
                // Morning / afternoon active desk & complaint hours: 23 to 53 mins
                23 + random.nextInt(31)
            }
            currentMinutesOfDay in 781..1200 -> {
                // Evening beat / verification / diary writing: 33 to 71 mins
                33 + random.nextInt(39)
            }
            else -> {
                // Late night quiet Thana: 113 to 227 mins
                113 + random.nextInt(115)
            }
        }
        // Ensure not an exact multiple of 10 or 15 to maintain irregular feel
        var irregular = baseMinutes
        if (irregular % 10 == 0 || irregular % 15 == 0) {
            irregular += if (random.nextBoolean()) 3 else 7
        }
        return irregular
    }

    /**
     * Formats duration into human readable string:
     * e.g., "47 minutes", "1 hour 38 minutes", "2 days 4 hours 19 minutes"
     */
    fun formatDuration(minutes: Int): String {
        val days = minutes / 1440
        val remainingAfterDays = minutes % 1440
        val hours = remainingAfterDays / 60
        val mins = remainingAfterDays % 60

        return buildString {
            if (days > 0) {
                append("$days day${if (days > 1) "s" else ""} ")
            }
            if (hours > 0) {
                append("$hours hour${if (hours > 1) "s" else ""} ")
            }
            if (mins > 0 || (days == 0 && hours == 0)) {
                append("$mins minute${if (mins != 1) "s" else ""}")
            }
        }.trim()
    }

    /**
     * Formats calendar date based on the day index starting from Wednesday, 12th March 2025.
     */
    fun formatDateForDayIndex(dayIndex: Int): String {
        // Base: Wednesday, 12th March 2025
        val baseDayOfWeekIndex = 2 // Wednesday (0 = Mon, 1 = Tue, 2 = Wed)
        val dayOfWeek = dayNames[(baseDayOfWeekIndex + (dayIndex - 1)) % 7]
        
        val baseDay = 12
        val currentDay = baseDay + (dayIndex - 1)
        val month = "March 2025" // In a longer simulation, can rollover to April/May

        val suffix = when {
            currentDay in 11..13 -> "th"
            currentDay % 10 == 1 -> "st"
            currentDay % 10 == 2 -> "nd"
            currentDay % 10 == 3 -> "rd"
            else -> "th"
        }
        return "$dayOfWeek, ${currentDay}$suffix $month"
    }
}

data class TimeAdvanceResult(
    val newDayIndex: Int,
    val newDate: String,
    val newMinutesOfDay: Int,
    val newTime: String,
    val daysPassed: Int
)
