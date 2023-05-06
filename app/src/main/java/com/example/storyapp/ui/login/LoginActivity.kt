package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.auth.AuthViewModel
import com.example.storyapp.auth.UserPreferences
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.home.HomeActivity
import com.example.storyapp.ui.signIn.SignInActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dataPreferences: UserPreferences
    private lateinit var myLoginEmailText: LoginEmailCustomText
    private lateinit var myLoginPasswordText: LoginPasswordCustomText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataPreferences = UserPreferences(this)

        showLoading(false)

        myLoginEmailText = binding.edLoginEmail
        myLoginPasswordText = binding.edLoginPassword

        myLoginEmailText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
               
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        myLoginPasswordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.btnLogin.setOnClickListener{
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if(email.isEmpty()) {
                binding.edLoginEmail.error ="Fill must be filled!"
            }

            if(password.isEmpty()) {
                binding.edLoginPassword.error = "Fill must be filled!"
            }

            if(email.isNotEmpty() && password.isNotEmpty() && myLoginEmailText.isValidEmail(email) && password.length >= 8) {
                authViewModel.login(this,email, password)
            }
        }

        binding.btnToSignin.setOnClickListener{
            startActivity(Intent(this@LoginActivity, SignInActivity::class.java))
        }

        authViewModel.token.observe(this){token->
            showMoveToLogin(token)
        }

        authViewModel.isLoading.observe(this){
            showLoading(it)
        }

        authViewModel.response.observe(this) {response->
            showNotification(response)
        }

        playAnimation()
    }

    private fun playAnimation() {
        val nameLogin = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(500)
        val emailText = ObjectAnimator.ofFloat(binding.emailLogin, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordLogin, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val atau = ObjectAnimator.ofFloat(binding.textOr, View.ALPHA, 1f).setDuration(500)
        val signText = ObjectAnimator.ofFloat(binding.btnToSignin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(nameLogin, emailText, emailEditText, passwordText,passwordEditText, buttonLogin, atau, signText)
            start()
        }
    }


    private fun showMoveToLogin(token: String) {
        showLoading(true)
        dataPreferences.setInput(UserPreferences.TOKEN, token)
        dataPreferences.setLogin(UserPreferences.IS_LOGIN, true)
        moveActivity()
    }

    private fun showNotification(response: String) {
         Toast.makeText(this@LoginActivity, response, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        if(dataPreferences.getLogin(UserPreferences.IS_LOGIN)) {
            moveActivity()
        }
    }

    private fun moveActivity() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}