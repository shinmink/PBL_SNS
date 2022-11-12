package com.example.a_sns.Alert


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a_sns.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_notify.*
import kotlinx.android.synthetic.main.item_comment.view.*


class NotifyActivity : AppCompatActivity() {

    var contentUid: String? = null
    var user: FirebaseUser? = null
    var destinationUid: String? = null
    var fcmPush: FcmPush? = null
    var commentSnapshot: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)

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

        var message =
            user?.email + getString(R.string.follower) + message + getString(R.string.follower)
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
                            .apply(RequestOptions().circleCrop())
                            .into(view.comment_imageview_profile)
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



    //@@@@@@@@@@@@@@@@@@@@
/*

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.show_notification -> showNotification()
            R.id.show_noti_bigtext -> showNotificationBigText()
            R.id.show_noti_bigpicture -> showNotificationBigPicture()
            R.id.show_noti_progress -> showNotificationProgress()
            R.id.show_noti_button -> showNotificationButton()
            R.id.show_noti_reg_activity -> showNotificationRegularActivity()
            R.id.show_noti_special_activity -> showNotificationSpecialActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private val channelID = "default"

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelID, "default channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "description text of this channel."
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private var myNotificationID = 1
        get() = field++

    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification Title")
            .setContentText("Notification body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(this)
            .notify(myNotificationID, builder.build())
    }

    private fun showNotificationBigText() {
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification Title")
            .setContentText("Notification body")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(resources.getString(R.string.long_notification_body)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(this)
            .notify(myNotificationID, builder.build())
    }

    private fun showNotificationBigPicture() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.baseline_add_a_photo_24)
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(bitmap)
            .setContentTitle("Notification Title")
            .setContentText("Notification body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null))  // hide largeIcon while expanding
        NotificationManagerCompat.from(this)
            .notify(myNotificationID, builder.build())
    }

    private fun showNotificationButton() {
        val intent = Intent(this, NotifyActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification Title")
            .setContentText("Notification body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.baseline_add_a_photo_24, "Action", pendingIntent)
        NotificationManagerCompat.from(this)
            .notify(myNotificationID, builder.build())
    }

    private fun showNotificationProgress() {
        val progressNotificationID = myNotificationID
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Progress")
            .setContentText("In progress")
            .setProgress(100, 0, false)
            .setPriority(NotificationCompat.PRIORITY_LOW)  // need to change channel importance LOW for Android 8.0 or higher.
        NotificationManagerCompat.from(this)
            .notify(progressNotificationID, builder.build())

        CoroutineScope(Dispatchers.Default).apply {
            launch {
                for (i in (1..100).step(10)) {
                    Thread.sleep(1000)
                    builder.setProgress(100, i, false)
                    NotificationManagerCompat.from(applicationContext)
                        .notify(progressNotificationID, builder.build())
                }
                builder.setContentText("Completed")
                    .setProgress(0, 0, false)
                NotificationManagerCompat.from(applicationContext)
                    .notify(progressNotificationID, builder.build())
            }
        }
    }

    private fun showNotificationRegularActivity() {
        val intent = Intent(this, NotifyActivity::class.java)
        val pendingIntent = with (TaskStackBuilder.create(this)) {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification Title")
            .setContentText("Notification body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // auto remove this notification when user touches it
        NotificationManagerCompat.from(this)
            .notify(myNotificationID, builder.build())
    }

    private fun showNotificationSpecialActivity() {
        val intent = Intent(this, NotifyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification Title")
            .setContentText("Notification body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // auto remove this notification when user touches it
        NotificationManagerCompat.from(this)
            .notify(myNotificationID, builder.build())
    }

     */
