package com.jeremy.acw.data.model.cinema

data class Theatre(
    val id: String = "",
    val name: String = ""
) {
    fun toMap(): Map<String, Any> = mutableMapOf(
        "id" to id,
        "name" to name
    )
}