package com.example.a_sns

import com.google.android.gms.common.api.Response
import com.google.common.net.MediaType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

data class AlertDTO (
    var destinationUid: String? = null,
    var userId: String? = null,
    var uid: String? = null,
    var kind: Int = 0, //0 : 좋아요, 1: 메세지, 2: 팔로우
    var message: String? = null,
    var timestamp: Long? = null
)

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

data class FollowingDTO(

    var followerCount: Int = 0,
    var followers: MutableMap<String, Boolean> = HashMap(),

    var followingCount: Int = 0,
    var followings: MutableMap<String, Boolean> = HashMap()
)

data class PushInDTO(var to: String? = null,
                   var notification: Notification? = Notification()) {
    data class Notification(var body: String? = null,
                            var title: String? = null)
}

class FcmPush() {
    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    val url = "https://fcm.googleapis.com/fcm/send"
    val serverKey =
        "AAAAwiX7fiM:APA91bF6iZLUPXgD5oTZ0Nc4RJd7kr-amKEOMvyCkvhthHgoFVLRHwIs_YGewCYg-92C1tNzZaIUtyL0xpx0APshPEKbuPgjbg3EqsecXfAIdIJz8yAaOy-iojR22QQwTN8x8rKXpmZX"

    var okHttpClient: OkHttpClient? = null
    var gson: Gson? = null

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid: String, title: String, message: String) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var token = task.result["pushtoken"].toString()
                    println(token)
                    var pushinDTO = PushInDTO()
                    pushinDTO.to = token
                    pushinDTO.notification?.title = title
                    pushinDTO.notification?.body = message

                    var body = RequestBody.create(JSON, gson?.toJson(pushinDTO).toString())
                    var request = Request
                        .Builder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + serverKey)
                        .url(url)
                        .post(body)
                        .build()
                    okHttpClient?.newCall(request)?.enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                        }

                        override fun onResponse(call: Call, response: okhttp3.Response) {
                            println(response.body?.string())
                        }
                    })
                }
            }
    }
}