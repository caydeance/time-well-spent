package com.example.timewellspent

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault


class SessionAdapter(var sessionList: List<Session>) : RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    companion object{
        val EXTRA_SESSION = "session"
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView
        val textViewDate: TextView
        val textViewHeartrate: TextView
        val textViewTimeSpent: TextView
        val textViewEmotion: TextView
        val layout: ConstraintLayout

        init {
            textViewName = itemView.findViewById(R.id.textView_sessionEntry_name)
            textViewDate = itemView.findViewById(R.id.textView_sessionEntry_date)
            textViewHeartrate = itemView.findViewById(R.id.textView_sessionEntry_bpm)
            textViewTimeSpent = itemView.findViewById(R.id.textView_sessionEntry_timeSpent)
            textViewEmotion = itemView.findViewById(R.id.textView_sessionEntry_emotion)
            layout = itemView.findViewById(R.id.layout_sessionEntry)
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
        val context = holder.layout.context

        holder.textViewName.text = session.name

        // formats the date nicely to show just the day month and year
        var unformattedDate = session.date.toString()
        var dayAndMonth = ""
        var dayAndMonthEndIndex = 0
        var year = ""
        for (i in 0..unformattedDate.length){
            if(dayAndMonthEndIndex == 0 && unformattedDate[i] == ':') {
                dayAndMonth = unformattedDate.substring(0,i-3)
                dayAndMonthEndIndex = i
            }
            if(
                i > dayAndMonthEndIndex
                && unformattedDate[i].isUpperCase()
                && unformattedDate[i+1] == ' ') {
                year = unformattedDate.substring(i + 2, unformattedDate.length)
                break
            }
        }
        holder.textViewDate.text = "[" + dayAndMonth + ", " + year + "]"

        // formats the time to show it in hours and minutes
        var elapsedHrs = session.elapsedTime / 60
        var elapsedSec = session.elapsedTime % 60
        holder.textViewTimeSpent.text = "[" + elapsedHrs + "h " + elapsedSec + "min]"

        // formats the heartbeat nicely for it to display like "79 BPM"
        holder.textViewHeartrate.text = session.heartRate.toString() + " BPM"

        //displays the emoji
        holder.textViewEmotion.text = try {
            Session.EMOTION.valueOf(session.emotion).emoji
        } catch (ex: IllegalArgumentException) {
            "¯\\_(ツ)_/¯"
        }

        holder.layout.isLongClickable = true
        holder.layout.setOnLongClickListener {
            // the textview you want the PopMenu to be anchored to should be added below replacing holder.textViewName
            val popMenu = PopupMenu(context, holder.textViewName)
            popMenu.inflate(R.menu.menu_session_list_context)
            popMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_session_delete -> {
                        deleteFromBackendless(position)
                        true
                    }
                    else -> true
                }
            }
            popMenu.show()
            true
        }

        holder.layout.setOnClickListener {
            val intent = Intent(context, Session::class.java)
            // add the hero to the extras of the intent
            Toast.makeText(context, "SessionAdapter activatedddd", Toast.LENGTH_SHORT).show()


            intent.putExtra(EXTRA_SESSION, "session")
            context.startActivity(intent)

        }
    }

    private fun deleteFromBackendless(position: Int) {
        Log.d("SessionAdapter", "deleteFromBackendless: Trying to delete ${sessionList[position]}")
        val session: Session = Session()

//        session.name = sessionList[position].name
//        session.name = "TEST NAME"
//        session.elapsedTime = 0
//        session.emotion = "NEUTRAL"
//        session.heartRate = 0
//        session.sessionDate = "03/25/2027"
        Backendless.Data.of<Session>(Session::class.java)
            .save(sessionList[position], object : AsyncCallback<Session> {
                override fun handleResponse(savedContact: Session) {
                    Backendless.Data.of<Session>(Session::class.java).remove(
                        savedContact,
                        object : AsyncCallback<Long?> {
                            override fun handleResponse(response: Long?) {
                                // Contact has been deleted. The response is the
                                // time in milliseconds when the object was deleted
                                sessionList.removeAt(position)
                            }

                            override fun handleFault(fault: BackendlessFault?) {
                                // an error has occurred, the error code can be
                                // retrieved with fault.getCode()
                            }
                        })
                }

                override fun handleFault(fault: BackendlessFault?) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                }
            })
        // put in the code to delete the item using the callback from Backendless
        // in the handleResponse, we'll need to also delete the item from the sessionList
        //(will need to update the variable to make it mutable)
        // and make sure that the recyclerview is updated using notifyDatasetChanged
        // you can instead use notifyItemRemoved because we know what position was removed. it's more efficient to do that
    }
}