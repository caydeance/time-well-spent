package com.example.timewellspent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.timewellspent.databinding.ActivitySessionDetailBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SessionDetailActivity : AppCompatActivity() {

    companion object {
        val TAG = "SessionDetailActivity"
    }

    private lateinit var binding: ActivitySessionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySessionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(SessionAdapter.Companion.TAG, "reached SessionDetailActivity!!!")
        val sessionEntry = intent.getParcelableExtra<Session>(SessionAdapter.EXTRA_SESSION_ENTRY) ?: Session()
        Log.d(SessionAdapter.Companion.TAG, "intent received in SessionDetailActivity!!!")

//        val sessionEntry = intent.getParcelableExtra<Session>(EXTRA_SESSION_ENTRY) ?: Session()


        binding.editTextSessionDetailName.setText(sessionEntry.name)
        Log.d(SessionAdapter.Companion.TAG, "name edited to: ${sessionEntry.name}")
        binding.editTextSessionDetailHeartRate.setText("${sessionEntry.heartRate}")
        Log.d(SessionAdapter.Companion.TAG, "heartRate edited to: ${sessionEntry.heartRate}")
        binding.sliderSessionDetailTimeSpent.value = sessionEntry.elapsedTime.toFloat()
        Log.d(SessionAdapter.Companion.TAG, "elapsedTime edited to: ${sessionEntry.elapsedTime}")
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        binding.textViewSessionDetailDate.text = format.format(sessionEntry.date)
        Log.d(SessionAdapter.Companion.TAG, "date edited to: ${sessionEntry.date}")

        var spinnerItems = Session.EMOTION.entries.map { it.emoji }
        var emotions = Session.EMOTION.entries.map { it.name }
        Log.d(TAG, "onCreate: $spinnerItems\n$sessionEntry")
        binding.spinnerSessionDetailEmotion.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        var position = emotions.indexOf(sessionEntry.emotion)
        if(position < 0) {
            position = 0
        }
        binding.spinnerSessionDetailEmotion.setSelection(position)

        binding.textViewSessionDetailDate.setOnClickListener {
            val selection = binding.textViewSessionDetailDate.text.toString()
            val date: Date = format.parse(selection)
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(date.time) // requires milliseconds
                .setTitleText("Select a Date")
                .build()

            datePicker.addOnPositiveButtonClickListener { millis ->
                val newDate = Date(millis+24*60*60*1000)
                binding.textViewSessionDetailDate.setText(format.format(newDate))
            }

            datePicker.show(supportFragmentManager,"date picker")

        }

        binding.buttonSessionDetailSave.setOnClickListener {

            Backendless.UserService.CurrentUser(true, object : AsyncCallback<BackendlessUser?> {
                override fun handleResponse(response: BackendlessUser?) {
                    Log.d(TAG, "SAVE BUTTON cLIcKED, reponse is: ${response}")
                    if(response != null) {
                        // some additional logic for reloaded user
                        sessionEntry.ownerId = response.userId
                        sessionEntry.name = binding.editTextSessionDetailName.text.toString()

                        sessionEntry.heartRate =
                            binding.editTextSessionDetailHeartRate.text.toString().toInt()
                        sessionEntry.elapsedTime =
                            binding.sliderSessionDetailTimeSpent.value.toInt()
                        sessionEntry.date =
                            format.parse(binding.textViewSessionDetailDate.text.toString())
                                ?: Date()
                        sessionEntry.emotion =
                            Session.EMOTION.entries.find { it.emoji == binding.spinnerSessionDetailEmotion.selectedItem.toString() }!!.name

                        saveToBackendless(sessionEntry)
                    }
                }

                override fun handleFault(fault: BackendlessFault?) {
                    // error handling logic
                    Log.d(TAG, "handleFault: ${fault?.message}")
                    Toast.makeText(this@SessionDetailActivity, "Error Retrieving User, Try Again", Toast.LENGTH_SHORT).show()
                }
            })


        }

    }

    private fun saveToBackendless(sessionEntry: Session) {
        // code here to save to backendless
        Log.d(TAG, "saveToBackendless()")
        Log.d(TAG, "SAVING SEsSION: ${sessionEntry}")

        if(intent.hasExtra(SessionAdapter.EXTRA_SESSION_ENTRY)) {
            
        }
        else {
            Backendless.Data.of<Session>(Session::class.java)
                .save(sessionEntry, object : AsyncCallback<Session> {
                    override fun handleResponse(response: Session) {
                        // new Contact instance has been saved
                        val context =
                        val sessionListIntent = Intent(context, SessionListActivity::class.java)
                        startActivity(sessionListIntent)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                        Log.d(TAG, "handleFault: ${fault?.message}")
                    }
                })
        }
    }


}