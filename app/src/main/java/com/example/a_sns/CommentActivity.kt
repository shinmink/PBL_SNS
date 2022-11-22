package com.example.a_sns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_notify.*
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentActivity : AppCompatActivity() {

    var contentUid: String? = null
    var user: FirebaseUser? = null
    var destinationUid: String? = null
    var fcmPush: FcmPush? = null
    var commentSnapshot: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        user = FirebaseAuth.getInstance().currentUser
        destinationUid = intent.getStringExtra("destinationUid")
        contentUid = intent.getStringExtra("contentUid")
        fcmPush = FcmPush()

        comment_btn_send.setOnClickListener {
            val comment = ContentDTO.Comment()

            comment.userId = FirebaseAuth.getInstance().currentUser!!.email
            comment.comment = comment_edit_message.text.toString()
            comment.uid = FirebaseAuth.getInstance().currentUser!!.uid
            comment.timestamp = System.currentTimeMillis()

            FirebaseFirestore.getInstance()
                .collection("images")
                .document(contentUid!!)
                .collection("comments")
                .document()
                .set(comment)

            commentAlarm(destinationUid!!, comment_edit_message.text.toString())
            comment_edit_message.setText("")

        }

        comment_recyclerview.adapter = CommentRecyclerViewAdapter()
        comment_recyclerview.layoutManager = LinearLayoutManager(this)

    }



    override fun onStop() {
        super.onStop()
        commentSnapshot?.remove()
    }


    fun commentAlarm(destinationUid: String, message: String) {

        val alarmDTO = AlertDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = user?.email
        alarmDTO.uid = user?.uid
        alarmDTO.kind = 1
        alarmDTO.message = message
        alarmDTO.timestamp = System.currentTimeMillis()

        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)

        var message = user?.email + getString(R.string.alarm_comment) + message + getString(R.string.alarm_comment)
        fcmPush?.sendMessage(destinationUid, "알림 메세지 입니다.", message)
    }


    inner class CommentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val comments: ArrayList<ContentDTO.Comment>

        init {
            comments = ArrayList()
            commentSnapshot = FirebaseFirestore
                .getInstance()
                .collection("images")
                .document(contentUid!!)
                .collection("comments")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    comments.clear()
                    if (querySnapshot == null) return@addSnapshotListener
                    for (snapshot in querySnapshot?.documents!!) {
                        comments.add(snapshot.toObject(ContentDTO.Comment::class.java)!!)
                    }
                    notifyDataSetChanged()

                }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var view = holder.itemView

            // Profile Image
            FirebaseFirestore.getInstance()
                .collection("profileImages")
                .document(comments[position].uid!!)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    if (documentSnapshot?.data != null) {

                        val url = documentSnapshot?.data!!["image"]
                        Glide.with(holder.itemView.context)
                            .load(url)
                            .apply(RequestOptions().circleCrop()).into(view.comment_imageview_profile)
                    }
                }

            view.comment_textview_profile.text = comments[position].userId
            view.comment_textview_comment.text = comments[position].comment
        }

        override fun getItemCount(): Int {

            return comments.size
        }

        private inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

}