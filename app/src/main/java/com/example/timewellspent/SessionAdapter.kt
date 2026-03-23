package com.example.timewellspent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SessionAdapter(var sessionList: List<Session>) : RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView
        val textViewDate: TextView
        val textViewHeartrate: TextView
        val textViewTimeSpent: TextView
        val textViewEmotion: TextView

        init {
            textViewName = itemView.findViewById(R.id.textView_sessionEntry_name)
            textViewDate = itemView.findViewById(R.id.textView_sessionEntry_date)
            textViewHeartrate = itemView.findViewById(R.id.textView_sessionEntry_bpm)
            textViewTimeSpent = itemView.findViewById(R.id.textView_sessionEntry_timeSpent)
            textViewEmotion = itemView.findViewById(R.id.textView_sessionEntry_emotion)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessionList[position]
        holder.textViewName.text = session.name
        // TODO: format the date nicely to show just the day month and year
        holder.textViewDate.text = session.date.toString()
        // TODO: format the time to show it in hours and minutes
        holder.textViewTimeSpent.text = session.elapsedTime.toString()
        // TODO: format the money nicely to show it like 79 BPM
        holder.textViewHeartrate.text = session.heartRate.toString()
        // TODO: verify this works in displaying the emoji
        holder.textViewEmotion.text = try {
            Session.EMOTION.valueOf(session.emotion).emoji
        } catch (ex: IllegalArgumentException) {
            "¯\\_(ツ)_/¯"
        }
    }
}