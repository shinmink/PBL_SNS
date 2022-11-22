package com.example.a_sns

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_posting.*
import java.util.*

class PostingActivity : AppCompatActivity() {


    val PICK_IMAGE_FROM_ALBUM = 0

    var photoUri: Uri? = null

    var storage: FirebaseStorage? = null
    var firestore: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        Add_Image.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
            Add_Image.setImageURI(photoPickerIntent.data)
        }

        SubmitBtn.setOnClickListener {
            contentUpload()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 사진 고르고 세팅하기
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            if(resultCode == Activity.RESULT_OK){
                println(data?.data)
                photoUri = data?.data
                Add_Image.setImageURI(data?.data)
            }

            else{
                finish()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun contentUpload(){
        progress_bar.visibility = View.VISIBLE

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_.png"
        val storageRef = storage?.reference?.child("images")?.child(imageFileName)
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener{ taskSnapshot ->
            progress_bar.visibility = View.GONE

            Toast.makeText(this, "Uplode Success",
                Toast.LENGTH_SHORT).show()

            val uri = taskSnapshot.uploadSessionUri

            val contentDTO = ContentDTO()

            contentDTO.imageUrl = uri!!.toString()
            contentDTO.uid = auth?.currentUser?.uid
            contentDTO.explain = DailyText.text.toString()
            contentDTO.userId = auth?.currentUser?.email
            contentDTO.timestamp = System.currentTimeMillis()
            firestore?.collection("images")?.document()?.set(contentDTO)

            /*
            - 사진 위치
            - 이용자 uid
            - 사진의 edit text
            - 이용자 실제 id
            - 업로드 되는 시간
            - data 생성하고 finish
             */

            //FirebaseStorage.getInstance().setMaxUploadRetryTimeMillis(2000);
            setResult(Activity.RESULT_OK)
            finish()
        }
            ?.addOnFailureListener {
                progress_bar.visibility = View.GONE

                Toast.makeText(this, "Uplode Fail",
                    Toast.LENGTH_SHORT).show()
            }
    }


}
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



