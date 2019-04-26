package ca.codifyr.studentattendance.domain.entities

import org.json.JSONObject

class Attend {
    val barcode: String

    constructor(barcode: String) {
        this.barcode = barcode
    }

    companion object: JSONable<Attend> {
        override fun toJSON(item: Attend): String {
            val jsonObject = JSONObject()
            jsonObject.put("barcode", item.barcode)
            return jsonObject.toString()
        }

        override fun fromJSON(json: JSONObject): Attend {
            return Attend(json.getString("barcode"))
        }
    }
}