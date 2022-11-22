package com.example.a_sns.activitities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.a_sns.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val registerBtn = findViewById<Button>(R.id.register_submitBtn)
        registerBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.register_editEmail).text.toString()
            val password = findViewById<EditText>(R.id.register_editPasswd).text.toString()
            val passwordCheck = findViewById<EditText>(R.id.register_editPasswordConfirm).text.toString()

            // null exception
            if (email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
                Toast.makeText(this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // id validation
            if (!email.contains("@")) {
                Toast.makeText(this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val regex = Regex("^(?=.*[a-zA-Z])(?=.*[0-9]).{6,12}\$")
            // password validation
            if (password.length < 6 || password.length > 12 || !password.matches(regex)) {
                Toast.makeText(this, "비밀번호 조건을 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // password check
            if (password != passwordCheck) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // register
            createUser(email, password)
        }

    }

    private fun createUser(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공
                    AlertDialog.Builder(this)
                        .setTitle("회원가입 성공")
                        .setMessage("로그인 화면으로 이동합니다.")
                        .setPositiveButton("확인") { _, _ ->
                            Firebase.auth.signOut() // 회원가입하면 자동 login 되기 때문에 signOut
                            finish()
                        }
                        .show()
                } else {
                    // 회원가입 실패
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }


}