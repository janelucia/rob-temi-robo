package de.fhkiel.temi.robogguide.models

enum class GuideState {
    Transfer, // robot leads guest to next exhibit
    Exhibit     // robot is at exhibit
    //TODO vielleicht weitere states hinzufügen wie exhibit introduction, exhibit idle, exhibit end (...)
}