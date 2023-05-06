package com.example.storyapp.ui.signIn

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
import com.example.storyapp.databinding.ActivitySignInBinding
import com.example.storyapp.ui.login.LoginActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignInBinding
    private lateinit var dataPreferences: UserPreferences
    private lateinit var mySignEmailText: SignInEmailCustomText
    private lateinit var mySignPasswordText: SignInPasswordCustomText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)

        mySignEmailText = binding.edRegisterEmail
        mySignPasswordText = binding.edRegisterPassword
        dataPreferences = UserPreferences(this)

        showLoading(false)

        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        mySignEmailText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        mySignPasswordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.btnSignin.setOnClickListener{
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if(name.isEmpty()) {
                binding.edRegisterName.error ="Fill must be filled!"
            }

            if(email.isEmpty()) {
                binding.edRegisterEmail.error = "Fill must be filled!"
            }

            if(password.isEmpty()) {
                binding.edRegisterPassword.error = "Fill must be filled!"
            }

            if(name.isNotEmpty() && email.isNotEmpty()
                && password.isNotEmpty() && mySignEmailText.isValidEmail(email) && password.length >= 8)
                {
                authViewModel.signIn(this, name, email, password)
            }

            authViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            authViewModel.response.observe(this) {response ->
                moveToLogin(response)
            }
        }

        binding.btnToLogin.setOnClickListener{
            startActivity(Intent(this@SignInActivity, LoginActivity::class.java))
        }

        playAnimation()
    }

    private fun playAnimation() {
        val nameSign = ObjectAnimator.ofFloat(binding.textSign, View.ALPHA, 1f).setDuration(500)
        val nameText = ObjectAnimator.ofFloat(binding.name, View.ALPHA, 1f).setDuration(500)
        val nameInput = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val emailText = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val passwordText = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val buttonSignIn = ObjectAnimator.ofFloat(binding.btnSignin, View.ALPHA, 1f).setDuration(500)
        val atau = ObjectAnimator.ofFloat(binding.textOr, View.ALPHA, 1f).setDuration(500)
        val loginText = ObjectAnimator.ofFloat(binding.btnToLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(nameSign, nameText, nameInput, emailText, emailEditText, passwordText,passwordEditText, buttonSignIn, atau, loginText)
            start()
        }
    }

    private fun moveToLogin(response : String) {
        if (response == "Email is already taken") {
            Toast.makeText(this@SignInActivity,response, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@SignInActivity,response, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@SignInActivity, LoginActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}