package de.fhkiel.temi.robogguide.models

/**
 * Enum class for the level of detail of a tour
 * The parameters need to be filled at a later point, when the db is analyzed.
 * These Level of Details need to be set for each new Place.
 *
 * @param _lengthInMinutes the length of the tour in minutes
 * @param _nrOfExhibits the number of exhibits in the tour
 */
enum class LevelOfDetail(private var _lengthInMinutes: Int?, private var _nrOfExhibits: Int?) {
    EVERYTHING_DETAILED(null, null),
    EVERYTHING_CONCISE(null, null),
    ONLY_IMPORTANT_DETAILED(null, null),
    ONLY_IMPORTANT_CONCISE(null, null);

    fun setLengthInMinutes(lengthInMinutes: Int) {
        this._lengthInMinutes = lengthInMinutes
    }

    fun setNrOfExhibits(nrOfExhibits: Int) {
        this._nrOfExhibits = nrOfExhibits
    }


    fun getLengthInMinutes(): Int {
        if (_lengthInMinutes == null) {
            throw IllegalStateException("Length in minutes is not set for ${this.name}!")
        }
        return _lengthInMinutes!!
    }

    fun getNrOfExhibits(): Int {
        if (_nrOfExhibits == null) {
            throw IllegalStateException("Nr of Exhibits is not set for ${this.name}!")
        }
        return _nrOfExhibits!!
    }

    fun isDetailed(): Boolean {
        return this == EVERYTHING_DETAILED || this == ONLY_IMPORTANT_DETAILED
    }
}