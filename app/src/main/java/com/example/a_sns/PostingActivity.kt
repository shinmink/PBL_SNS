package com.example.a_sns

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a_sns.databinding.ActivityPostingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable.start
import java.util.*


class PostingActivity : AppCompatActivity() {

    lateinit var binding: ActivityPostingBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 사용자 셋팅
        auth = Firebase.auth

    }

}

    //Intent intent = new Intent("com.android.camera.action.CROP");
    //intent.setDataAndType(pictureUri, "image/*");

    /*
    // 갤러리로 이동하기
    private fun goGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*" //선택한 파일의 종류를 지정해준다 (이미지만 지정하겠다는 뜻)
        intent.putExtra("crop", true)
        //startActivityForResult(galleryIntent, PICK_IMAGE_FROM_ALBUM)
    }

    // 이미지 크롭하기
    private fun cropImage(uri: Uri?){
        //CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
            //.setCropShape(CropImageView.CropShape.RECTANGLE)
            //사각형 모양으로 자른다
            .start(this)
    }

    // 사진을 Storage에 업로드
    private fun contentUpload() {
        //현재 시간을 String으로 만들기
        //20210306_141238
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        //만약 나라를 지정하지않고 단말기의 설정된 시간으로 하고 싶다면 Locale.getDefault(), 한국으로 하고 싶으면 Locale.KOREA
        val fileName = "IMAGE_$timestamp.png"

        //서버 스토리지에 접근하기!
        val storageRef = storage?.reference?.child("images")?.child(fileName)

        // 서버 스토리지에 파일 업로드하기!
        storageRef?.putFile(photoUri!!)?.continueWithTask() {
            return@continueWithTask storageRef.downloadUrl
            //나중에 이미지를 다른 곳에서 불러오고 할떄 url을 가져올수있게해놓음
        }?.addOnSuccessListener {
            upload(it, fileName)
            Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show()
        }?.addOnCanceledListener {
            //업로드 취소 시
        }?.addOnFailureListener {
            //업로드 실패 시
        }
    }

    // Storage 업로드 된 사진의 uri를 가지고
// Firestore에 게시물 업로드
    private fun upload(uri: Uri, fileName: String) {
        //contentDto 객체를 생성한다.
        val contentDto = ContentDto(
            imageUrl = uri.toString(),
            uid = auth?.currentUser?.uid,
            userId = auth?.currentUser?.email,
            explain = contentEditText.text.toString(),
            timestamp = System.currentTimeMillis().toLong(),
            imageStorage = fileName
        )

        //Dto를 Firestore에 추가
        firestore?.collection("images")?.document()?.set(contentDto)

        //액티비티 종료
        setResult(Activity.RESULT_OK)
        finish()
    }
     */