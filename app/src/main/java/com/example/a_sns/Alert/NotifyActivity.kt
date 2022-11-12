package com.example.a_sns.Alert



import android.content.Intent

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.example.a_sns.*
import com.example.a_sns.databinding.ActivityNotifyBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class NotifyActivity : AppCompatActivity() {

    lateinit var binding: ActivityNotifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)



        data class NotificationDto(
            var destinationUid: String? = "",
            var sender: String? = "",
            var senderUid: String? = "",
            var type: Int? = null,
            var timestamp: Long? = null,
            var timeInfo: String? = "",
            var contentUid: String? = ""
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8.0
            //createNotificationChannel()
        }

        bottom_navigation.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_home -> { // 홈 버튼 클릭
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_search -> { // 서치 버튼 클릭
                    val intent = Intent(this, SearchingActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_add_photo -> {
                    val intent = Intent(this, PostingActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_favorite_alarm -> { // 하트, 알람 버튼 클릭
                    val intent = Intent(this, NotifyActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_account -> {
                    var userFragment = UserFragment()
                    var bundle = Bundle()
                    var uid = FirebaseAuth.getInstance().currentUser?.uid

                    bundle.putString("destinationUid", uid)
                    userFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, userFragment).commit()
                }
            }
        }

    }

    data class NotifyDto(
        var destinationUid: String? = null,
        var userId: String? = null,
        var uid: String? = null,
        var kind: Int = 0, //0 : 좋아요, 1: 메세지, 2: 팔로우
        var message: String? = null,
        var timestamp: Long? = null
    )

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notify_menu, menu)
        return super.onCreateOptionsMenu(menu)
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
