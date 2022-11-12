package com.example.a_sns

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.a_sns.ui.LoginFragment
import com.example.a_sns.ui.RequestPermissionFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class StartActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    private var perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) // Android 10.0 이상
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    else
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        // if permission is granted, start LoginFragment
        val requestPerms = perms.filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
        if (requestPerms.isNotEmpty()) { // 권한이 하나라도 없으면
            changeFragment(RequestPermissionFragment())
        }
        else { // 모든 권한이 허용되어 있으면
            if (auth?.currentUser != null) { // 로그인 되어 있으면 메인 화면으로
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else { // 로그인 되어 있지 않으면 로그인 화면으로
                changeFragment(LoginFragment())
            }
        }

    }

    // change Fragment
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            //.setCustomAnimations(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            .setReorderingAllowed(true)
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    fun changeFragmentBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            //.setCustomAnimations(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            .setReorderingAllowed(true)
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }


}
