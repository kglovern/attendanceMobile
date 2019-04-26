package ca.codifyr.studentattendance.domain.entities

import org.json.JSONObject

interface JSONable<T> {
    fun toJSON(item: T): String
    fun fromJSON(json: JSONObject): T
}