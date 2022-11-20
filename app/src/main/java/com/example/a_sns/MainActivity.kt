package com.example.a_sns

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.a_sns.alert.NotifyFragment
import com.example.a_sns.databinding.ActivityMainBinding
import com.example.a_sns.search.SearchFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.messaging.FirebaseMessagingService
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

        // 권한 요청
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        // back버튼 누르면 홈으로
        /*
        toolbar_btn_back.setOnClickListener {
            val homeFragment = HomeFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_content,homeFragment)
                .commit()
        }
        */

        // 네비게이션 기본값
        bottom_navigation.selectedItemId = R.id.action_home

        // 네비게이션 클릭리스너
        bottom_navigation.setOnNavigationItemReselectedListener {
            setToolbarDefault()
            when (it.itemId) {
                R.id.action_home -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content,homeFragment)
                        .commit()

                    return@setOnNavigationItemReselectedListener
                }
                R.id.action_search -> {
                    val searchFragment = SearchFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content,searchFragment)
                        .commit()

                    return@setOnNavigationItemReselectedListener
                }
                R.id.action_posting -> {
                    val intent = Intent(this, PostingActivity::class.java)
                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        startActivity(intent)
                    }

                    return@setOnNavigationItemReselectedListener
                }
                R.id.action_alarm -> {
                    val notifiyFragment = NotifyFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content,notifiyFragment)
                        .commit()

                    return@setOnNavigationItemReselectedListener
                }
                R.id.action_account -> {
                    val userFragment = UserFragment()
                    val bundle = Bundle()
                    val uid = FirebaseAuth.getInstance().currentUser?.uid

                    bundle.putString("destinationUid", uid)
                    userFragment.arguments = bundle
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content, userFragment)
                        .commit()

                    return@setOnNavigationItemReselectedListener
                }
            }
        }
        //registerPushToken()
    }

    fun setToolbarDefault(){
        //toolbar_username.visibility = View.GONE
        toolbar_btn_back.visibility = View.VISIBLE
        toolbar_title_image.visibility = View.VISIBLE
    }
    /*
    fun registerPushToken(){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                task ->
            val token = task.result?.token
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val map = mutableMapOf<String,Any>()
            map["pushToken"] = token!!

            FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
        }
    }
    */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == UserFragment.PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK){
            var imageUri = data?.data
            var uid = FirebaseAuth.getInstance().currentUser?.uid
            var storageRef = FirebaseStorage.getInstance().reference.child("userProfileImages").child(uid!!)
            storageRef.putFile(imageUri!!).continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener { uri ->
                var map = HashMap<String,Any>()
                map["image"] = uri.toString()
                FirebaseFirestore.getInstance().collection("profileImages").document(uid).set(map)
            }
        }
    }
}
