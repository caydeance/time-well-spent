package com.example.timewellspent

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.timewellspent.databinding.ActivitySessionListBinding


class SessionListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySessionListBinding
    private lateinit var adapter: SessionAdapter
    companion object {
        val TAG = "session_list_activity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySessionListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Backendless.Data.of<Session>(Session::class.java)
            .find(object : AsyncCallback<MutableList<Session>> {
                override fun handleResponse(foundContacts: MutableList<Session>) {
                    // all Contact instances have been found
                    val sessionList = SessionAdapter(foundContacts)
                    val recyclerView = binding.recyclerViewSessionListRecyclerView
                    recyclerView.layoutManager = LinearLayoutManager(this@SessionListActivity)
                    recyclerView.adapter = sessionList
                }

                override fun handleFault(fault: BackendlessFault?) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Log.d(TAG, "myfaultbro:( ${fault?.code}")
                }
            })

        Backendless.UserService.CurrentUser(true, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(response: BackendlessUser?) {
                // some additional logic for reloaded user
//                val sessionList = SessionAdapter(object)
//                val recyclerView = binding.recyclerViewSessionListRecyclerView
//                recyclerView.layoutManager = LinearLayoutManager(this)
//                recyclerView.adapter = sessionList
            }

            override fun handleFault(fault: BackendlessFault?) {
                // error handling logic
            }
        })



    }
}