package com.example.a_sns.ui

import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.a_sns.StartActivity
import com.example.a_sns.R

class RequestPermissionFragment : Fragment(R.layout.request_permission_fragment) {
    private var perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) // Android 10.0 이상
        arrayOf(POST_NOTIFICATIONS)
    else
        arrayOf(READ_EXTERNAL_STORAGE)

    // initialization of the request permission launcher
    private val requestPermLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        val noPerms = it.filter { item -> !item.value }.keys
        if (noPerms.isNotEmpty()) { // there is a permission which is not granted!
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("경고")
                //setMessage("앱을 사용하려면 권한을 허용해야 합니다.")
                setMessage(getString(R.string.no_permission, noPerms))
                setPositiveButton("확인") { _, _ -> }
            }.show()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        // if permission is granted, start LoginFragment
        val requestPerms = perms.filter { checkSelfPermission(requireActivity(), it) != PackageManager.PERMISSION_GRANTED } // 여러 권한 확인
        if (requestPerms.isEmpty()) {
            (requireActivity() as StartActivity).changeFragment(LoginFragment())
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val allowBtn = view.findViewById<View>(R.id.requestPermission_allowBtn)
        allowBtn.setOnClickListener {
            requestMultiplePermission(perms)

            // if permission is granted, start LoginFragment
            val requestPerms = perms.filter { checkSelfPermission(requireActivity(), it) != PackageManager.PERMISSION_GRANTED } // 여러 권한 확인
            if (requestPerms.isEmpty())
                (requireActivity() as StartActivity).changeFragment(LoginFragment())
        }
    }

    private fun requestMultiplePermission(perms: Array<String>) {
        val requestPerms = perms.filter { checkSelfPermission(requireActivity(), it) != PackageManager.PERMISSION_GRANTED } // 여러 권한 확인
        if (requestPerms.isEmpty())
            return

        val showRationalePerms = requestPerms.filter { shouldShowRequestPermissionRationale(it) } // 권한 요청을 거절한 적이 있는지 확인
        if (showRationalePerms.isNotEmpty()) {
            // you should explain the reason why this app needs the permission.
            AlertDialog.Builder(requireActivity()).apply {
                setTitle("경고")
                setMessage("허용해야하는 이유")
                setPositiveButton("확인") { _, _ -> requestPermLauncher.launch(perms) }
            }.show()
        } else {
            requestPermLauncher.launch(requestPerms.toTypedArray())
        }
    }
}