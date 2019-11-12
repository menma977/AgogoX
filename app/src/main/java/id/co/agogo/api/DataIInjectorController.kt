package id.co.agogo.api

import org.json.JSONObject

class DataIInjectorController {
    fun jsonObjectToUrlEndCode(value: JSONObject): String {
        var statement = ""
        for (i in 0 until value.names().length()) {
            statement += if (i == value.names().length() - 1) {
                "${value.names()[i]}=${value.getString(value.names()[i].toString())}"
            } else {
                "${value.names()[i]}=${value.getString(value.names()[i].toString())}&"
            }
        }
        return statement
    }
}