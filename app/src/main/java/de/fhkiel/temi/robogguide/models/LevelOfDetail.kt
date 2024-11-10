package de.fhkiel.temi.robogguide.models

/**
 * Enum class for the level of detail of a tour
 * The parameters need to be filled at a later point, when the db is analyzed.
 * These Level of Details need to be set for each new Place.
 *
 */
enum class LevelOfDetail {
    EVERYTHING_DETAILED,
    EVERYTHING_CONCISE,
    ONLY_IMPORTANT_DETAILED,
    ONLY_IMPORTANT_CONCISE;

    fun isDetailed(): Boolean {
        return this == EVERYTHING_DETAILED || this == ONLY_IMPORTANT_DETAILED
    }

    fun tourName(): String {
        return when (this) {
            EVERYTHING_DETAILED -> "Lange Führung mit allen Informationen"
            EVERYTHING_CONCISE -> "Lange Führung mit kurzen Informationen"
            ONLY_IMPORTANT_DETAILED -> "Nur wichtige Stationen mit allen Informationen"
            ONLY_IMPORTANT_CONCISE -> "Nur wichtige Stationen mit kurzen Informationen"
        }
    }
}