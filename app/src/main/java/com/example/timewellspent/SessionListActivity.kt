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

    private val queryBuilder = DataQueryBuilder.create()
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
        Log.d(TAG, "ONCREATE: WE HERE RN")
//        val queryBuilder = DataQueryBuilder.create()







    }

    override fun onStart() {
        super.onStart()
// asynch api:
//        val result: MutableList<Session> =
//            Backendless.Data.of<Session>(Session::class.java).find(queryBuilder)
        //testing out basic object retrieval stuff
        Backendless.UserService.CurrentUser(true, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(response: BackendlessUser?) {
                Log.d(TAG, "CURRENTUSER: WE HERE RN")

                // some additional logic for reloaded user
                val whereClause = "ownerId = '${response?.userId}'"
                Log.d(TAG, "WHERECLAUSE CONSTRUCTRYCTED: ${whereClause}")

                Log.d(TAG, "USERID FOUND: ${response?.userId}")

                queryBuilder.setWhereClause(whereClause)
                Log.d(TAG, "QUERYBUILDER SET: ${queryBuilder}")
//                  val sessionList = SessionAdapter(object)
//                  val recyclerView = binding.recyclerViewSessionListRecyclerView
//                  recyclerView.layoutManager = LinearLayoutManager(this)
//                  recyclerView.adapter = sessionList

//                Log.d(TAG, "FINDING FILTERED SESSIONS: ${result}")
                Backendless.Data.of<Session>(Session::class.java).find(
                    queryBuilder,
                    object : AsyncCallback<MutableList<Session>> {
                        override fun handleResponse(foundSession: MutableList<Session>) {

                            Log.d(TAG, "RETRIEVING FILTERED SESSION.........: ${foundSession}")
                            // the "foundContact" collection now contains instances of the Contact class.
                            // each instance represents an object stored on the server.
                            val sessionList = SessionAdapter(foundSession)

                            Log.d(TAG, "FILTERED SESSION LIST:${sessionList}")
                            val recyclerView = binding.recyclerViewSessionListRecyclerView
                            recyclerView.layoutManager = LinearLayoutManager(this@SessionListActivity)
                            recyclerView.adapter = sessionList
                        }

                        override fun handleFault(fault: BackendlessFault?) {
                            // an error has occurred, the error code can be retrieved with fault.getCode()
                        }
                    })


            }

            override fun handleFault(fault: BackendlessFault?) {
                // error handling logic
            }
        })

//
//        Backendless.Data.of<Session>(Session::class.java)
//            .find(object : AsyncCallback<MutableList<Session>> {
//                override fun handleResponse(foundSession: MutableList<Session>) {
//                    // all Contact instances have been found
//
//                    Log.d(TAG, "RESTARTING SESSION.........: ${foundSession}")
//                    val sessionList = SessionAdapter(foundSession)
//                    Log.d(TAG, "RESTARTED SESSION LIST: ${sessionList}")
//                    val recyclerView = binding.recyclerViewSessionListRecyclerView
//                    recyclerView.layoutManager = LinearLayoutManager(this@SessionListActivity)
//                    recyclerView.adapter = sessionList
//                }
//
//                override fun handleFault(fault: BackendlessFault?) {
//                    // an error has occurred, the error code can be retrieved with fault.getCode()
//                    Log.d(TAG, "myfaultbro:( ${fault?.code}")
//                }
//            })
    }
}