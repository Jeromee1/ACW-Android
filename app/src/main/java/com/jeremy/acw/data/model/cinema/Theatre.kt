package com.jeremy.acw.data.model.cinema

data class Theatre(
    val id: String = ""
) {
    fun toMap(): Map<String, Any> = mutableMapOf(
        "id" to id
    )
}