package com.loitp.model

import java.io.Serializable

data class History(
        var distance: String? = null,
        var avgSpeed: String? = null,
        var timer: String? = null,
        var fileName: String? = null
) : Serializable
