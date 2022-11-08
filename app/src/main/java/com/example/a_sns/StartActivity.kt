package com.example.a_sns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.a_sns.ui.RequestPermissionFragment

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // start LoginFragment
        //changeFragment(LoginFragment())

        // start RequestPermissionFragment
        changeFragment(RequestPermissionFragment())
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
}
