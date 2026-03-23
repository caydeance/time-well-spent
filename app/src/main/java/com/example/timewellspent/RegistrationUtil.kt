package com.example.timewellspent

object RegistrationUtil {

    var testUsernames = listOf("spongebob", "jinx", "madHatter")
    var testEmails = listOf(
        "underTheSea@gmail.com",
        "getjinxed@gmail.com",
        "iluvbothkindsoftea@gmail.com")

    // NAME
    // -isn't empty
    fun validateName(name: String) : Boolean {
        return name.isNotEmpty()
    }

    // USERNAME
    // -len < 3
    fun validateUsername(username:String) : Boolean {
        if(username.length<3){
            return false
        }
        for(i in 0..testUsernames.size-1){
            if(username.equals(testUsernames.get(i))){
                return false
            }
        }
        return true
    }

    // PASSWORD, CONFIRM PASSWORD
    // -len >=8
    // -1+ digit
    // -1+ capital letter
    // -password matches confirm password
    // model password: SixSeven67
    fun validatePassword(password : String, confirmPassword: String) : Boolean {
        val digits = setOf('0','1','2','3','4','5','6','7','8','9')
        val capitals = setOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')

        return password.length>=8 && password.any { it in digits } && password.any { it in capitals } && password == confirmPassword
        return true
    }

    // EMAIL
    // -isn't empty
    // -make sure the email isn't used
    // -make sure it's in the proper email format user@domain.tld
    fun validateEmail(email: String) : Boolean {
        if(email.isEmpty()) return false
        if(email.indexOf("@")!=-1 && email.indexOf(".")!=-1) {
            if (email.indexOf("@") < email.indexOf(".") -1 && email.indexOf(".") < email.length - 1) {
                for (i in 0..testEmails.size - 1) {
                    if (email == testEmails.get(i)) {
                        return false
                    }
                }
                return true
            }
        }
        return false
    }
}