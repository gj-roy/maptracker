package com.loitp.model

data class ActionData<T>(
        var isDoing: Boolean? = null,
        var isSuccess: Boolean? = null,
        var isNetworkOffline: Boolean? = null,
        var isSwipeToRefresh: Boolean? = null,
        var data: T? = null,

        var message: String? = null,
        val errorResponse: ErrorResponse? = null
)