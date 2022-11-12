package com.example.a_sns

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.a_sns.databinding.ActivityPostingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PostingActivity : AppCompatActivity() {}


    /*
    lateinit var binding: ActivityPostingBinding
    val PICK_IMAGE_FROM_ALBUM = 0

    var photoUri: Uri? = null

    var storage: FirebaseStorage? = null
    var firestore: FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 사용자 셋팅 _____________________
        // Firebase storage
        storage = FirebaseStorage.getInstance()
        // Firebase Database
        firestore = FirebaseFirestore.getInstance()
        // Firebase Auth
        auth = FirebaseAuth.getInstance()
        //_________________________________
        //Intent intent = new Intent("com.android.camera.action.CROP");
        //intent.setDataAndType(pictureUri, "image/*");

        binding.SubmitBtn.setOnClickListener {

            // submit button 누르면 게시글 올리기
            contentUpload()
        }

        binding.BackBtn.setOnClickListener {
            onBackPressed()
        }

    }

    data class ContentDTO(var explain: String? = null,
                          var imageUrl: String? = null,
                          var uid: String? = null,
                          var userId: String? = null,
                          var timestamp: Long? = null,
                          var favoriteCount: Int = 0,
                          var favorites: MutableMap<String, Boolean> = HashMap()) {

        data class Comment(var uid: String? = null,
                           var userId: String? = null,
                           var comment: String? = null,
                           var timestamp: Long? = null)
    }

    // 갤러리로 이동해서 이미지 골라오기
    private fun photoPicker() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        binding.AddImage1.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }
    }

    // 사진을 Storage에 업로드
    @RequiresApi(Build.VERSION_CODES.N)
    fun contentUpload(){
        //progress_bar.visibility = View.VISIBLE

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_.png"
        val storageRef = storage?.reference?.child("images")?.child(imageFileName)
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener{ taskSnapshot ->
            //progress_bar.visibility = View.GONE

            Toast.makeText(this, getString(R.string.upload_success),
                Toast.LENGTH_SHORT).show()

            val uri = taskSnapshot.downloadUrl
            //디비에 바인딩 할 위치 생성 및 컬렉션(테이블)에 데이터 집합 생성


            //시간 생성
            val contentDTO = ContentDTO()

            //이미지 주소
            contentDTO.imageUrl = uri!!.toString()
            //유저의 UID
            contentDTO.uid = auth?.currentUser?.uid
            //게시물의 설명
            contentDTO.explain = addphoto_edit_explain.text.toString()
            //유저의 아이디
            contentDTO.userId = auth?.currentUser?.email
            //게시물 업로드 시간
            contentDTO.timestamp = System.currentTimeMillis()

            //게시물을 데이터를 생성 및 엑티비티 종료
            firestore?.collection("images")?.document()?.set(contentDTO)

            setResult(Activity.RESULT_OK)
            finish()
        }
            ?.addOnFailureListener {
                //progress_bar.visibility = View.GONE

                Toast.makeText(this, getString(R.string.upload_fail),
                    Toast.LENGTH_SHORT).show()
            }
    }


}


@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



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

    // 이미지 크롭하기
    private fun cropImage(uri: Uri?){
        //CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
        //.setCropShape(CropImageView.CropShape.RECTANGLE)
        //사각형 모양으로 자른다
        //.start(this)
    }


 */