package com.example.a_sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.example.a_sns.Alert.NotifyActivity
import com.example.a_sns.databinding.ActivitySearchingBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_searching.*

class SearchingActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchingBinding

    private var user: String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottom_navigation.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_home -> { // 홈 버튼 클릭
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_search -> { // 서치 버튼 클릭
                    val intent = Intent(this, SearchingActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_add_photo -> {
                    val intent = Intent(this, PostingActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_favorite_alarm -> { // 하트, 알람 버튼 클릭
                    val intent = Intent(this, NotifyActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_account -> {
                    var userFragment = UserFragment()
                    var bundle = Bundle()
                    var uid = FirebaseAuth.getInstance().currentUser?.uid

                    bundle.putString("destinationUid", uid)
                    userFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, userFragment).commit()
                }
            }
        }
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
                // 검색 전용 페이지로 넘어가면서 검색 결과 보내기
                //startActivity(Intent(this@SearchingActivity, MainActivity::class.java).also {
                    //it.putExtra("queryanswer", query)
                //})

                return true
            }

        })
        return super.onCreateOptionsMenu(menu)

    }
}