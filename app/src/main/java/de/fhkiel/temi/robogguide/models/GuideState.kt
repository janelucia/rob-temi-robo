package de.fhkiel.temi.robogguide.models

enum class GuideState {
    TransferStart, // robot leads guest to next exhibit
    TransferGoing,
    TransferError,
    Exhibit,     // robot is at exhibit
    End
    //TODO vielleicht weitere states hinzufügen wie exhibit introduction, exhibit idle, exhibit end (...)
}