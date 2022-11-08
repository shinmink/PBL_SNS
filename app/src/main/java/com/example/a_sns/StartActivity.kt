package com.example.a_sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.a_sns.ui.RequestPermissionFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class StartActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        auth = FirebaseAuth.getInstance()

        // start LoginFragment
        //changeFragment(LoginFragment())
        // start RequestPermissionFragment
        changeFragment(RequestPermissionFragment())
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    // change Fragment
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            .setReorderingAllowed(true)
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun moveMainPage(user: FirebaseUser?){
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}
