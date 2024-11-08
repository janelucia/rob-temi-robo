package de.fhkiel.temi.robogguide.models

enum class GuideState {
    TransferStart, // robot leads guest to next exhibit
    TransferGoing,
    Exhibit,     // robot is at exhibit
    End
}