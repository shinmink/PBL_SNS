package com.example.a_sns

import android.content.Intent
import android.Manifest
import androidx.viewbinding.ViewBinding
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.a_sns.databinding.ActivityMainBinding
import com.example.a_sns.ui.RequestPermissionFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var user : String = "User"
    lateinit var auth: FirebaseAuth

    //var user : String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSetting()

        // 사용자 셋팅
        auth = Firebase.auth

        // start LoginFragment
        //changeFragment(LoginFragment())
        // start RequestPermissionFragment

        changeFragment(RequestPermissionFragment())

        // 임시 로그인 액티비티
        //startActivity(Intent(this,LoginActivity::class.java))

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

    // change Fragment
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            .setReorderingAllowed(true)
            .replace(R.id.main_content, fragment)
            .addToBackStack(null)
            .commit()

    }


    //TODO : 화면 초기 셋팅값 설정 함수
    private fun checkLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationPermission && coarseLocationPermission
    }


    //TODO : 화면 초기 셋팅값 설정 함수
    private fun initSetting(){
        // 1. 사용자 이름 받아오기
        user = "User"
        // 2. 툴바 사용 설정
        setSupportActionBar(binding.MainToolbar)
        // 3. 툴바 왼쪽 버튼 설정
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        //supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_home_24)  // 왼쪽 버튼 아이콘 설정
        //supportActionBar!!.setTitle("HANSUNG SNS")

    }


    // 4. 툴바 메뉴 좌측 상단 버튼 클릭 됐을 때 클릭
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId) {
            R.id.Back_icon -> { // 뒤로
                Toast.makeText(this, "Post My Story", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,PostingActivity::class.java))
            }

            R.id.Alert_icon -> { // 알림 페이지로 이동
            Toast.makeText(this, "Notify", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,NotifyActivity::class.java))
        }
            R.id.Account_icon -> { // 내 프로필로 이동
                Toast.makeText(this, "My Page", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this,SearchingActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }



}

