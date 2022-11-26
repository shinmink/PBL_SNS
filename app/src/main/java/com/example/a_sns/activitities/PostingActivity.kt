package com.example.a_sns.activitities

import LoadingDialogFragment
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.a_sns.R
import com.example.a_sns.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_posting.*
import java.text.SimpleDateFormat
import java.util.*

class PostingActivity : AppCompatActivity() {
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null

    //Initialize Loader
    private val loadingDialogFragment by lazy { LoadingDialogFragment() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        //Initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //Open the album
        addphoto_btn_addimage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            imageResult.launch(photoPickerIntent)
        }

        //add image upload event
        addphoto_btn_upload.setOnClickListener {
            //Show Loader
            if (!loadingDialogFragment.isAdded) {
                loadingDialogFragment.show(supportFragmentManager, "loader")
            }
            contentUpload()
            //Hide Loader
            if (loadingDialogFragment.isAdded) {
                loadingDialogFragment.dismissAllowingStateLoss()
            }
        }

    }

    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                photoUri = data?.data
                Glide.with(this).load(photoUri).into(addphoto_image)
                // Toast
            } else {
                finish()
            }
        }


    private fun contentUpload() {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_" + timestamp + "_.png"

        val storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //promise method
        storageRef?.putFile(photoUri!!)?.continueWithTask {
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            val contentDTO = ContentDTO()

            //Insert downloadUrl of image
            contentDTO.imageUrl = uri.toString()

            //Insert uid of user
            contentDTO.uid = auth?.currentUser?.uid

            //Insert userId
            contentDTO.userId = auth?.currentUser?.email

            //Insert explain of content
            contentDTO.explain = addphoto_edit_explain.text.toString()

            //Insert timestamp
            contentDTO.timestamp = System.currentTimeMillis()

            firestore?.collection("images")?.document()?.set(contentDTO)

            setResult(Activity.RESULT_OK)
            Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}