package com.phantom.banguminote.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement

data class InfoData(
    val key: String,
    val value: JsonElement?,
    var actualValue: InfoValueData?
) {

    companion object {
        val valueGson = GsonBuilder()
            .registerTypeAdapter(
                InfoValueData::class.java,
                JsonDeserializer { json, typeOfT, context ->
                    try {
                        var value: String? = null
                        var values: List<ValuesData>? = null
                        var type: InfoDataType? = null
                        if (json.isJsonArray) {
                            val jsonArray = json.asJsonArray
                            values = jsonArray.map {
                                if (it.isJsonObject) {
                                    ValuesData(it.asJsonObject.get("v").asString)
                                } else {
                                    type = InfoDataType.UNKNOWN
                                    ValuesData("")
                                }
                            }
                        } else {
                            value = json.asString
                        }
                        return@JsonDeserializer InfoValueData(
                            value,
                            values,
                            type ?: when {
                                value != null -> InfoDataType.SINGLE
                                values != null -> InfoDataType.LIST
                                else -> InfoDataType.UNKNOWN
                            }
                        )
                    } catch (e: Exception) {
                        return@JsonDeserializer null
                    }
                })
            .create()
    }
}

data class InfoValueData(
    val value: String?,
    val values: List<ValuesData>?,
    val type: Enum<InfoDataType>
)

enum class InfoDataType {
    SINGLE, LIST, UNKNOWN
}

data class ValuesData(
    val v: String
)
