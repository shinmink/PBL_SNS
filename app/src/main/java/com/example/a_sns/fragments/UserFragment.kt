package com.example.a_sns.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a_sns.*
import com.example.a_sns.activitities.LoginActivity
import com.example.a_sns.activitities.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_posting.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import java.util.*
import kotlin.collections.ArrayList

class UserFragment : Fragment() {
    var storage : FirebaseStorage? = null
    var fragmentView : View? = null
    var photoUri : Uri? = null
    var firestore : FirebaseFirestore? = null
    var uid : String? = null
    private var auth : FirebaseAuth? = null
    private var currentUserUid : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_user, container, false)
        uid = arguments?.getString("destinationUid")
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUserUid = auth?.currentUser?.uid


        if(uid == currentUserUid){
            //MyPage
            fragmentView?.account_btn_follow_signout?.text = getString(R.string.signout)
            fragmentView?.account_btn_follow_signout?.setOnClickListener{
                activity?.finish()
                startActivity(Intent(activity, LoginActivity::class.java))
                auth?.signOut()
            }
        }else{
            //OtherUserPage
            fragmentView?.account_btn_follow_signout?.text = getString(R.string.follow)
            val mainactivity = (activity as MainActivity)
            mainactivity.toolbar_username?.text = arguments?.getString("userId")
            mainactivity.toolbar_btn_back?.setOnClickListener {
                mainactivity.bottom_navigation.selectedItemId = R.id.action_home
            }
            mainactivity.toolbar_title_image?.visibility = View.GONE
            mainactivity.toolbar_username?.visibility = View.VISIBLE
            mainactivity.toolbar_btn_back?.visibility = View.VISIBLE
            fragmentView?.account_btn_follow_signout?.setOnClickListener {
                requestFollow()
            }

        }
        fragmentView?.account_recyclerview?.adapter = UserFragmentRecyclerViewAdapter()
        fragmentView?.account_recyclerview?.layoutManager = GridLayoutManager(requireActivity(),3)

        fragmentView?.account_iv_profile?.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            imageResult.launch(photoPickerIntent)

            // TODO profileImages에 프로필 사진 업로드
        }
        getProfileImage()
        getFollowerAndFollowing()
        return fragmentView
    }

    override fun onResume() {
        super.onResume()
        getProfileImage()
    }

    private val imageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            photoUri = data?.data
            Glide.with(this).load(photoUri).into(account_iv_profile)

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val storageRef = FirebaseStorage.getInstance().reference.child("userProfileImages").child(uid!!)
            storageRef.putFile(photoUri!!).continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener { uri ->
                val map = HashMap<String,Any>()
                map["image"] = uri.toString()
                FirebaseFirestore.getInstance().collection("profileImages").document(uid).set(map)
            }
        }
        getProfileImage()
    }

    private fun getProfileImage(){
        firestore?.collection("profileImages")?.document(uid!!)?.addSnapshotListener{ documentSnapshot, _ ->
            if(documentSnapshot == null) return@addSnapshotListener
            if(documentSnapshot.data != null){
                val url = documentSnapshot.data!!["image"]
                Glide.with(requireActivity()).load(url).apply(RequestOptions().circleCrop()).into(fragmentView?.account_iv_profile!!)
            }
        }
    }


    private fun getFollowerAndFollowing(){
        firestore?.collection("users")?.document(uid!!)?.addSnapshotListener { documentSnapshot, _ ->
            if (documentSnapshot == null) return@addSnapshotListener

            val followDTO = documentSnapshot.toObject(FollowDTO::class.java)

            if (followDTO?.followingCount != null){
                fragmentView?.account_tv_following_count?.text = followDTO.followingCount.toString()

            }
            if (followDTO?.followerCount != null){
                fragmentView?.account_tv_follower_count?.text = followDTO.followerCount.toString()
                if (followDTO.followers.containsKey(currentUserUid!!)){
                    fragmentView?.account_btn_follow_signout?.text = getString(R.string.follow_cancel)
                    //TODO
                    fragmentView?.account_btn_follow_signout?.background?.setColorFilter(ContextCompat.getColor(requireActivity(),
                        R.color.colorLightGray
                    ),PorterDuff.Mode.MULTIPLY)
                }else{
                    if(uid != currentUserUid) {
                        fragmentView?.account_btn_follow_signout?.text = getString(R.string.follow)
                        fragmentView?.account_btn_follow_signout?.background?.colorFilter = null
                    }
                }

            }
        }
    }

    private fun requestFollow(){
        //Save data to my account
        val tsDocFollowing = firestore?.collection("users")?.document(currentUserUid!!)
        firestore?.runTransaction { transition ->
            var followDTO = transition.get(tsDocFollowing!!).toObject(FollowDTO::class.java)
            if (followDTO == null){
                followDTO = FollowDTO()
                followDTO.followingCount = 1
                followDTO.followers[uid!!] = true

                transition.set(tsDocFollowing, followDTO)
                return@runTransaction
            }

            if (followDTO.followings.containsKey(uid)){
                //It remove following third person when a third person follow me
                followDTO.followingCount = followDTO.followingCount -1
                followDTO.followers.remove(uid)

            }else {
                //It add following third person when a third person do not follow me
                followDTO.followingCount = followDTO.followingCount + 1
                followDTO.followers[uid!!] = true

            }
            transition.set(tsDocFollowing, followDTO)
            return@runTransaction
        }

        //Save data to third person
        val tsDocFollower = firestore?.collection("users")?.document(uid!!)
        firestore?.runTransaction { transition ->
            var followDTO = transition.get(tsDocFollower!!).toObject(FollowDTO::class.java)
            if (followDTO == null){
                followDTO = FollowDTO()
                followDTO!!.followerCount = 1
                followDTO!!.followers[currentUserUid!!] = true
                followAlarm(uid!!)

                transition.set(tsDocFollower, followDTO!!)
                return@runTransaction
            }

            if (followDTO!!.followers.containsKey(currentUserUid)){
                //It cancel my follower when I follow a third person
                followDTO!!.followerCount = followDTO!!.followerCount -1
                followDTO!!.followers.remove(currentUserUid!!)
            }else {
                //It add m follower when I don't follow a third person
                followDTO!!.followerCount = followDTO!!.followerCount +1
                followDTO!!.followers[currentUserUid!!] = true
                followAlarm(uid!!)
            }
            transition.set(tsDocFollower, followDTO!!)
            return@runTransaction
        }
    }

    private fun followAlarm(destinationUid : String){
        val alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = auth?.currentUser?.email
        alarmDTO.uid = auth?.currentUser?.uid
        alarmDTO.kind = 2
        alarmDTO.timestamp = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)

        // TODO
        val message = auth?.currentUser?.email + getString(R.string.alarm_follow)
        FcmPush.instance.sendMessage(destinationUid,"Howlstagram" , message )
    }


    inner class UserFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        init {
            firestore?.collection("images")?.whereEqualTo("uid", uid)?.addSnapshotListener { querySnapshot, _ ->
                //Sometimes, This code return null of querySnapshot when it signout
                if(querySnapshot == null) return@addSnapshotListener

                //Get data
                for (snapshot in querySnapshot.documents){
                    contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
                }
                fragmentView?.account_tv_post_count?.text = contentDTOs.size.toString()
                notifyDataSetChanged()
            }
        }


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            val width = resources.displayMetrics.widthPixels / 3

            val imageview = ImageView(p0.context)
            imageview.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
            return CustomViewHolder(imageview)
        }

        inner class  CustomViewHolder(var imageview : ImageView) : RecyclerView.ViewHolder(imageview){

        }

        override fun getItemCount(): Int {
            return contentDTOs.size

        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            val imageview = (p0 as CustomViewHolder).imageview
            Glide.with(p0.itemView.context).load(contentDTOs[p1].imageUrl).apply(RequestOptions().centerCrop()).into(imageview)
        }



    }
}