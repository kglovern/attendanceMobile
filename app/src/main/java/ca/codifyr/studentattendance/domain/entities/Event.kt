package ca.codifyr.studentattendance.domain.entities

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Event(val id: Int, val level: EventLevel, val message: String, val createdAt: Date) {
    enum class EventLevel {
        DEBUG, INFO, WARN, ERROR
    }

    companion object: JSONable<Event> {

        override fun fromJSON(json: JSONObject): Event {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = formatter.parse(json.getString("created_at"))
            return Event(
                json.getInt("id"),
                EventLevel.valueOf(json.getString("level").toUpperCase()),
                json.getString("message"),
                date
            )
        }

        override fun toJSON(item: Event): String {
            TODO("not implemented")
        }
    }
}