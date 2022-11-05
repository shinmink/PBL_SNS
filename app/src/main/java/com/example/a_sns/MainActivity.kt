package com.example.a_sns

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.a_sns.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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

        // 알림 허용 설정
        requestSinglePermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY)

        // 사용자 셋팅
        auth = Firebase.auth

        /* searchview dropdown
        val searchAutoComplete: SearchView.SearchAutoComplete =
            binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        searchAutoComplete.setTextColor(Color.BLACK)
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white)

        //가라로 데이터 초기화
         val datas = arrayOf(
             "피카츄",
             "꼬부기",
             "파이리"
         )

        // 참고 예시 아래
        //val SeoulBusdatas = RouteTools.getRouteNames().toList()

        searchAutoComplete.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                datas // 가라 데이터
            )
       )
         */
    }

    // 알림 허가 받는 함수
    private fun requestSinglePermission(permission: String) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
            return

        val requestPermLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it == false) { // permission is not granted!
                AlertDialog.Builder(this).apply {
                    setTitle("Warning")
                    setMessage(getString(R.string.no_permission, permission))
                }.show()
            }
        }

        if (shouldShowRequestPermissionRationale(permission)) {
            // you should explain the reason why this app needs the permission.
            AlertDialog.Builder(this).apply {
                setTitle("Reason")
                setMessage(getString(R.string.req_permission_reason, permission))
                setPositiveButton("Allow") { _, _ -> requestPermLauncher.launch(permission) }
                setNegativeButton("Deny") { _, _ -> }
            }.show()
        } else {
            // should be called in onCreate()
            requestPermLauncher.launch(permission)
        }
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
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_home_24)  // 왼쪽 버튼 아이콘 설정
        supportActionBar!!.setTitle("HANSUNG SNS")

    }


    // 4. 툴바 메뉴 좌측 상단 버튼 클릭 됐을 때 클릭 // @@
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> { // 메인 ( 즉, 유저 메뉴에서 좌측 상단 버튼은 시작 페이지로가는 버튼 )
                Toast.makeText(this,"Main",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity,MainActivity::class.java))
            }

            R.id.Posting_icon -> { // 알림 페이지로 이동
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

    // + 검색 실행 시 검색 전용 페이지로 넘어가기
    // + 검색 전용 페이지에서 ( 자동완성이나 검색기록 구현 )
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                //TODO("검색어 변경 이벤트, 입력 할때마다 반응")
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                //TODO("키보드의 검색 버튼을 클릭한 순간의 이벤트,  즉 입력 완료")
                Toast.makeText(this@MainActivity, "검색 결과 : $query", Toast.LENGTH_SHORT).show()
                // 검색 전용 페이지로 넘어가면서 검색 결과 보내기
                startActivity(Intent(this@MainActivity, SearchingActivity::class.java).also {
                    it.putExtra("queryanswer", query)
                })

                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }






}