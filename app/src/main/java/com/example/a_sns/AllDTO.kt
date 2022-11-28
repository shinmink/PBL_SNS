package com.example.a_sns
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

data class AlarmDTO(
    var destinationUid: String? = null,
    var userId: String? = null,
    var uid: String? = null,

    // 0 : like alarm
    // 1 : commemt alarm
    // 2 : follow alarm
    var kind: Int? = null,
    var message: String? = null,
    var timestamp: Long? = null

)

data class ContentDTO(
    var explain: String? = null,
    var imageUrl: String? = null,
    var uid: String? = null,
    var userId: String? = null,
    var timestamp: Long? = null,
    var favoriteCount: Int = 0,
    var favorites: MutableMap<String, Boolean> = HashMap()) {
    data class Comment(var uid: String? = null,
                       var userId: String? = null,
                       var comment : String? = null,
                       var timestamp: Long? = null)
}

data class FollowDTO (
    var followerCount : Int = 0,
    var followers : MutableMap<String, Boolean> = HashMap(),
    var followingCount : Int = 0,
    var followings : MutableMap<String, Boolean> = HashMap()

)

data class PushDTO(
    var to: String? = null,
    var notification: Notification = Notification()
) {
    data class Notification(
        var body: String? = null,
        var title: String? = null
    )
}

class FcmPush {

    var JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AIzaSyDJkXmpDemGhlKOGMSNZ_yO4FQXsNkZct8"
    var gson : Gson? = null
    var okHttpClient : OkHttpClient? = null
    companion object{
        var instance = FcmPush()
    }

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }
    fun sendMessage(destinationUid: String, title: String, message: String) {
        FirebaseFirestore.getInstance().collection("pushtokens")
            .document(destinationUid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var token = task.result["pushtoken"].toString()
                println(token)
                var pushDTO = PushDTO()
                pushDTO.to = token
                pushDTO.notification?.title = title
                pushDTO.notification?.body = message

                var body = gson?.toJson(pushDTO)?.let { RequestBody.create(JSON, it) }
                var request = body?.let {
                    Request
                        .Builder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + serverKey)
                        .url(url)
                        .post(it)
                        .build()
                }
                if (request != null) {
                    okHttpClient?.newCall(request)?.enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                        }

                        override fun onResponse(call: Call, response: Response) {
                            println(response.body?.string())
                        }
                    })
                }
            }
        }
    }
}