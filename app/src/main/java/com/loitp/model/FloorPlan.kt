package com.loitp.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
@Entity(tableName = "floorPlan")
data class FloorPlan(

        @Json(name = "id")
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String = "",

        @Json(name = "name")
        @ColumnInfo(name = "name")
        var name: String? = null,

        @Ignore //wont save to room
        var isCheck: Boolean = false

) : Serializable
