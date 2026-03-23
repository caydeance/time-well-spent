package com.example.timewellspent

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.timewellspent.databinding.ActivityRegistrationBinding


class RegistrationActivity : AppCompatActivity() {
    companion object {
        val TAG = "registration_activity"
    }

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrieve any information from the intent using the extras keys
        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME) ?: ""
        val password = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD) ?: ""

        // prefill the username & password fields
        // for EditTexts, you actually have to use the setText functions
        binding.editTextRegistrationUsername.setText(username)
        binding.editTextTextPassword.setText(password)

        // register an account and send back the username & password
        // to the login activity to prefill those fields
        binding.buttonRegistrationRegister.setOnClickListener {
            val password = binding.editTextTextPassword.text.toString()
            val confirm = binding.editTextRegistrationConfirmPassword.text.toString()
            val username = binding.editTextRegistrationUsername.text.toString()
            val name = binding.editTextRegistrationName.text.toString()
            val email = binding.editTextRegistrationEmail.text.toString()

            if(RegistrationUtil.validatePassword(password, confirm) &&
                RegistrationUtil.validateUsername(username))  {  // && do the rest of the validations
                // apply lambda will call the functions inside it on the object
                // that apply is called on

                // register the user on backendless following the documentation
                // and in the handleResponse, that's where we make the resultIntent and go back
                // in the handleFailure, toast the failure and don't go back.


            }

            val user = BackendlessUser()
            user.setProperty("name",name)
            user.setProperty("username", username)
            user.setPassword(password)
            user.setProperty("email", email)

            Log.d(TAG, "we be registerin")
            Log.d(TAG, "email: ${user.email}")

            Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
                override fun handleResponse(registeredUser: BackendlessUser?) {
                    // user has been registered and now can login
                    Log.d(TAG, "we can register now ay")
                }

                override fun handleFault(fault: BackendlessFault?) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                }
            })


        }

    }
}