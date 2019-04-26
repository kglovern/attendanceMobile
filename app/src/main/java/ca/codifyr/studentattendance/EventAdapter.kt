package ca.codifyr.studentattendance

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.codifyr.studentattendance.domain.entities.Event
import kotlinx.android.synthetic.main.event_list_item.view.*
import org.jetbrains.anko.backgroundColor

class EventAdapter(private val eventList: List<Event>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    class EventViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        val mView = v
        val mEventMessage = v.eventMessage
        val mEventDate = v.eventDate
        val mEventIcon = v.eventIcon
        val mEventItemContainer = v.eventItemContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_list_item, parent, false))
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: EventAdapter.EventViewHolder, position: Int) {
        val event = eventList.get(position)
        holder.mEventMessage.text = event.message
        holder.mEventDate.text = event.createdAt.toString()

        when (event.level) {
            Event.EventLevel.ERROR -> {
                holder.mEventIcon.setImageResource(R.drawable.ic_error_black_24dp)
                holder.mEventItemContainer.setCardBackgroundColor(holder.mView.resources.getColor(R.color.colourError))
            }
            Event.EventLevel.WARN ->  {
                holder.mEventIcon.setImageResource(R.drawable.ic_warning_black_24dp)
                holder.mEventItemContainer.setCardBackgroundColor(holder.mView.resources.getColor(R.color.colourWarning))
            }
            Event.EventLevel.INFO -> {
                holder.mEventIcon.setImageResource(R.drawable.ic_info_black_24dp)
                holder.mEventItemContainer.setCardBackgroundColor(holder.mView.resources.getColor(R.color.colourInfo))
            }
            else ->  {
                holder.mEventIcon.setImageResource(R.drawable.ic_bug_report_black_24dp)
                holder.mEventItemContainer.setCardBackgroundColor(holder.mView.resources.getColor(R.color.colourDebug))
            }
        }
    }
}