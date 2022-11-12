package com.example.a_sns

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