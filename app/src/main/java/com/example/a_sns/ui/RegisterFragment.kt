package com.example.a_sns.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.a_sns.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment(R.layout.register_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // register Button
        val registerBtn = view.findViewById<Button>(R.id.register_submitBtn)
        registerBtn.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.register_editEmail).text.toString()
            val password = view.findViewById<EditText>(R.id.register_editPasswd).text.toString()
            val passwordCheck = view.findViewById<EditText>(R.id.register_editPasswordConfirm).text.toString()

            // null exception
            if (email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
                Toast.makeText(activity, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // id validation
            if (!email.contains("@")) {
                Toast.makeText(activity, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val regex = Regex("^(?=.*[a-zA-Z])(?=.*[0-9]).{6,12}\$")
            // password validation
            if (password.length < 6 || password.length > 12 || !password.matches(regex)) {
                Toast.makeText(activity, "비밀번호 조건을 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // password check
            if (password != passwordCheck) {
                Toast.makeText(activity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // register
            createUser(email, password)

        }
    }
    //fun firebase register
    private fun createUser(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공
                    AlertDialog.Builder(requireContext())
                        .setTitle("회원가입 성공")
                        .setMessage("로그인 화면으로 이동합니다.")
                        .setPositiveButton("확인") { _, _ ->
                            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                        .show()
                } else {
                    // 회원가입 실패
                    Toast.makeText(activity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

}