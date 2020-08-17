package com.loitp.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorResponse(

        @SerializedName("message")
        @Expose
        val message: String? = null,

        @SerializedName("code")
        @Expose
        val code: Int? = null
)