package com.example.a_sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isEmpty
import com.example.a_sns.databinding.ActivitySearchingBinding

class SearchingActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchingBinding

    private var user: String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSetting()

        if (binding.recyclerView.isEmpty()){
            binding.textView1.text = "해당하는 검색 결과가 없습니다."
        }
    }

    //TODO : 화면 초기 셋팅값 설정 함수
    private fun initSetting() {

        val intent = getIntent()
        val Searchvalue = intent.getStringExtra("queryanswer")
        //binding.searchView.queryHint = Searchvalue

        // 툴바 사용 설정
        setSupportActionBar(binding.SearchingToolbar)
        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_home_24)  // 왼쪽 버튼 아이콘 설정
        supportActionBar!!.setTitle(Searchvalue)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                //TODO("검색어 변경 이벤트, 입력 할때마다 반응")
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                //TODO("키보드의 검색 버튼을 클릭한 순간의 이벤트,  즉 입력 완료")
                Toast.makeText(this@SearchingActivity, "검색 결과 : $query", Toast.LENGTH_SHORT).show()
                // 검색 전용 페이지로 넘어가면서 검색 결과 보내기
                startActivity(Intent(this@SearchingActivity, MainActivity::class.java).also {
                    it.putExtra("queryanswer", query)
                })

                return true
            }

        })
        return super.onCreateOptionsMenu(menu)

    }
}