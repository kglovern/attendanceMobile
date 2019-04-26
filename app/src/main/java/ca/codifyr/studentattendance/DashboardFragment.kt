package ca.codifyr.studentattendance

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import ca.codifyr.studentattendance.domain.APIRequestFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DashboardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val healthBtn = getView()!!.findViewById(R.id.btnHealth) as Button
        healthBtn.setOnClickListener {
            val healthText = getView()!!.findViewById(R.id.apiHealthText) as TextView
            doAsync {
                val request = APIRequestFactory.buildGetRequest("_health")
                val response = request.execute()
                uiThread {
                    if (response.isSuccessful) {
                        healthText.setText(R.string.text_api_available)
                        Toast.makeText(it.activity, "Successfully pinged API", Toast.LENGTH_SHORT).show()
                    } else {
                        healthText.setText(R.string.text_api_unavailable)
                        Toast.makeText(it.activity, "Unable to contact API", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }
}
