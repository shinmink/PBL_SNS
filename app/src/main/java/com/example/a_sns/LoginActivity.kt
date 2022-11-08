package com.example.a_sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        email_login_button.setOnClickListener {
            signinAndSignup()
        }

        Firebase.auth.signInWithEmailAndPassword("a@a.com", "123456")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    println("########## Login Success")
                    println(Firebase.auth.currentUser?.uid)
                } else {
                    it.exception?.message
                    println("########## Login Failed ${it.exception?.message}")
                }
            }

        bottom_navigation.selectedItemId = R.id.action_home

        bottom_navigation.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_home ->{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_account ->{
                    var userFragment = UserFragment()
                    var bundle = Bundle()
                    var uid = FirebaseAuth.getInstance().currentUser?.uid

                    bundle.putString("destinationUid",uid)
                    userFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,userFragment).commit()
                }
            }
        }


    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth.currentUser)
    }

    fun signinAndSignup(){
        auth.createUserWithEmailAndPassword(email_edittext.text.toString(),password_edittext.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //Creating a user account
                    // 계정 만들기, 회원가입하기
                    moveMainPage(task.result?.user)
                }else if(task.exception?.message.isNullOrEmpty()){
                    //Show the error message
                    // 로그인 실패로 인한 에러 메세지 출력
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                }else{
                    //Login if you have account
                    // 계정이 이미 있다면 !
                    signinEmail()
                }
            }
    }

    fun signinEmail(){
        auth.signInWithEmailAndPassword(email_edittext.text.toString(),password_edittext.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //Login
                    // 로그인 성공
                    moveMainPage(task.result?.user)
                }else{
                    //Show the error message
                    // 로그인 실패로 인한 에러 메세지 출력
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                }
            }
    }

    fun moveMainPage(user:FirebaseUser?){
        // 로그인이 성공적일 경우 메인화면으로 넘어가기
        if(user != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}
