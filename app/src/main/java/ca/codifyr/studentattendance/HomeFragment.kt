package ca.codifyr.studentattendance

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.codifyr.studentattendance.domain.APIRequestFactory
import ca.codifyr.studentattendance.domain.entities.Event
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray


class HomeFragment : Fragment() {
    val mEventList = mutableListOf<Event>()
    var mEventAdapter: EventAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        val fragmentView = inflater.inflate(R.layout.fragment_home, container, false)
        val mEventRecycler = fragmentView.findViewById(R.id.eventRecycler) as RecyclerView

        populateEvents()
        mEventRecycler.layoutManager = LinearLayoutManager(context)
        mEventAdapter = EventAdapter(mEventList)
        mEventRecycler.adapter = mEventAdapter

        return fragmentView
    }

    fun populateEvents() {
        val request = APIRequestFactory.buildGetRequest("events/limit/7")

        doAsync {
            val response = request.execute()
            uiThread {
                if (response.isSuccessful) {
                    val jsonArray = JSONArray(response.body()!!.string())
                    for (i in 0 until jsonArray.length()) {
                        val event = Event.fromJSON(jsonArray.getJSONObject(i))
                        mEventList.add(event)
                    }
                    mEventAdapter!!.notifyDataSetChanged()
                } else {
                    toast("Unable to retrieve events")
                }
            }
        }
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}
