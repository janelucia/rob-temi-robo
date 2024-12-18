package de.fhkiel.temi.robogguide.models

import java.net.URL

/**
 * Media: holds an url that can either point at a image or video.
 */
class Media(val url: URL, val type: MediaType)