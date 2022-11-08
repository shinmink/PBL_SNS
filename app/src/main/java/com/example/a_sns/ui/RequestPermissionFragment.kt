package com.example.a_sns.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.a_sns.StartActivity
import com.example.a_sns.R

class RequestPermissionFragment : Fragment(R.layout.request_permission_fragment) {
    // initialization
    private val requestPermLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { // 권한 요청 컨트랙트
        if (it == false) { // permission is not granted!
            AlertDialog.Builder(requireContext()).apply {
                setTitle("경고")
                setMessage("앱을 사용하려면 권한을 허용해야 합니다.")
                setPositiveButton("확인") { _, _ ->
                    // 권한 요청 다시하기
                    requestSinglePermission(READ_EXTERNAL_STORAGE)
                }
            }.show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // if permission is granted, start LoginFragment
        if (checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            (requireActivity() as StartActivity).changeFragment(LoginFragment())
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        val allowBtn = view.findViewById<View>(R.id.requestPermission_allowBtn)
        allowBtn.setOnClickListener {
            requestSinglePermission(READ_EXTERNAL_STORAGE)
        }
    }

    private fun requestSinglePermission(permission: String) { // 한번에 하나의 권한만 요청하는 예제
        if (checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED) // 권한 유무 확인
            return

        if (shouldShowRequestPermissionRationale(permission)) { // 권한 설명 필수 여부 확인
            // you should explain the reason why this app needs the permission.
            AlertDialog.Builder(requireContext()).apply {
                setTitle("권한 요청")
                setMessage("이 앱은 사진을 불러오기 위해 권한을 요청합니다.")
                setPositiveButton("허용") { _, _ -> requestPermLauncher.launch(permission) }
                setNegativeButton("거부") { _, _ ->
                    (requireActivity() as StartActivity).finish()
                }
            }.show()
        } else {
            // should be called in onCreate()
            requestPermLauncher.launch(permission) // 권한 요청 시작
        }
    }
}