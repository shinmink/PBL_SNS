package com.example.a_sns.activitities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.example.a_sns.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        //click submit button
        val submitBtn = findViewById<Button>(R.id.login_submit)
        submitBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.login_email).text.toString()
            val password = findViewById<EditText>(R.id.login_password).text.toString()

            // null exception
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            login(email, password)
        }

        //click register text
        val registerBtn = findViewById<TextView>(R.id.login_register)
        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // login fun
    private fun login(email: String, password: String) {
        let {
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // 로그인이 성공했을 때
                        //val user = Firebase.auth.currentUser
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                        moveMainPage(auth?.currentUser)
                    } else { // 로그인이 실패했을 때
                        Toast.makeText(this, "로그인 실패. 이메일과 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}