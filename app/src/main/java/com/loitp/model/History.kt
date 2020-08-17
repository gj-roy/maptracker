package com.loitp.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
@Entity(tableName = "history")
data class History(

        @Json(name = "id")
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String = "",

        @Json(name = "distance")
        @ColumnInfo(name = "distance")
        var distance: String = "",

        @Json(name = "avgSpeed")
        @ColumnInfo(name = "avgSpeed")
        var avgSpeed: String = "",

        @Json(name = "timer")
        @ColumnInfo(name = "timer")
        var timer: String = "",

        @Json(name = "fileName")
        @ColumnInfo(name = "fileName")
        var fileName: String = ""
) : Serializable
