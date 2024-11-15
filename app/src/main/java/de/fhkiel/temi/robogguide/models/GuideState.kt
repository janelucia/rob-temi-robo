package de.fhkiel.temi.robogguide.models

/**
 * GuideState: represents the state of the guide from start to end
 */
enum class GuideState {
    TransferStart, // robot leads guest to next exhibit
    TransferGoing,
    TransferError,
    Exhibit,     // robot is at exhibit
    End
}