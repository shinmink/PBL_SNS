package com.example.a_sns.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a_sns.MainActivity
import com.example.a_sns.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//LoginFragment
class LoginFragment : Fragment(R.layout.login_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //click submit button
        val submitBtn = view.findViewById<Button>(R.id.login_submit)
        submitBtn.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.login_email).text.toString()
            val password = view.findViewById<EditText>(R.id.login_password).text.toString()

            // null exception
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(activity, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            login(email, password)
        }

        //click register text
        val registerBtn = view.findViewById<TextView>(R.id.login_register)
        registerBtn.setOnClickListener {
            (activity as MainActivity).changeFragment(RegisterFragment())
        }
    }

    // login fun
    private fun login(email: String, password: String) {
        activity?.let {
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // 로그인이 성공했을 때
                        //val user = Firebase.auth.currentUser
                        Toast.makeText(activity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    } else { // 로그인이 실패했을 때
                        Toast.makeText(activity, "로그인 실패. 이메일과 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}