package com.example.a_sns.activitities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.a_sns.*
import com.example.a_sns.FcmPush
import com.example.a_sns.fragments.DetailViewFragment
import com.example.a_sns.fragments.NotifyFragment
import com.example.a_sns.fragments.SearchFragment
import com.example.a_sns.fragments.UserFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 권한 요청
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        bottom_navigation.setOnItemSelectedListener {
            setToolbarDefault()
            when (it.itemId) {
                R.id.action_home -> {
                    val detailViewFragment = DetailViewFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, detailViewFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_search -> {
                    val searchFragment = SearchFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, searchFragment)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_add_photo -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startActivity(Intent(this, PostingActivity::class.java))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.action_favorite_alarm -> {
                    val notifyFragment = NotifyFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, notifyFragment)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_account -> {
                    val userFragment = UserFragment()
                    val bundle = Bundle()
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    bundle.putString("destinationUid", uid)
                    userFragment.arguments = bundle

                    supportFragmentManager.beginTransaction().replace(R.id.main_content, userFragment)
                        .commit()
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        //Set default screen
        bottom_navigation.selectedItemId = R.id.action_home
    }

    override fun onStop() {
        super.onStop()

        FcmPush.instance.sendMessage("rG78toed3lMw2aTa7JMlkPlcbD92","test", "test")
    }

    private fun setToolbarDefault() {
        toolbar_username.visibility = View.GONE
        toolbar_btn_back.visibility = View.GONE
        toolbar_title_image.visibility = View.VISIBLE

    }
}