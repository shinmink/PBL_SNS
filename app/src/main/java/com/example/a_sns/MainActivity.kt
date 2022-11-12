package com.example.a_sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a_sns.databinding.ActivityMainBinding
import com.example.a_sns.ui.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var user: String = "User"
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 사용자 셋팅
        auth = Firebase.auth

        // 임시 로그인 액티비티
        //startActivity(Intent(this,LoginActivity::class.java))

        /*
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
         */


        // 네비게이션 기본값
        bottom_navigation.selectedItemId = R.id.action_home

        // 네비게이션 클릭리스너
        bottom_navigation.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_search -> {
                    val intent = Intent(this, SearchingActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_posting -> {
                    val intent = Intent(this, PostingActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_alarm -> {
                    val intent = Intent(this, NotifyActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_account -> {
                    val userFragment = UserFragment()
                    val bundle = Bundle()
                    val uid = FirebaseAuth.getInstance().currentUser?.uid

                    bundle.putString("destinationUid", uid)
                    userFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, userFragment).commit()
                }
            }
        }
    }

    fun gotoLoginFragment() {
        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, loginFragment).commit()
    }
}
