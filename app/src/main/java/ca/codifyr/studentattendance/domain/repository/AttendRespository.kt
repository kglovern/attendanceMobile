package ca.codifyr.studentattendance.domain.repository

import ca.codifyr.studentattendance.domain.APIRequestFactory
import ca.codifyr.studentattendance.domain.entities.Attend
import okhttp3.Response
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AttendRespository: IRepository<Attend> {
    override fun add(item: Attend): Boolean {
        val attendPayload = Attend.toJSON(item)
        APIRequestFactory.buildPostRequest("attend", attendPayload).execute().use {response ->
            if (response.isSuccessful) {
                return true
            }
            return false
        }
    }

    override fun getSingle(): Attend {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCollection(): List<Attend> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSingle(id: Int): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateSingle(item: Attend): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}