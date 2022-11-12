package com.example.a_sns.alert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a_sns.AlertDTO
import com.example.a_sns.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_notify.view.*
import kotlinx.android.synthetic.main.item_comment.view.*

class NotifyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var notifySnapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notify, container, false)
        view.notifyframgent_recyclerview.adapter = NotifyRecyclerViewAdapter()
        view.notifyframgent_recyclerview.layoutManager = LinearLayoutManager(activity)

        return view
    }

    inner class NotifyRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val AlertDTOList = ArrayList<AlertDTO>()

        init {

            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            println(uid)
            FirebaseFirestore.getInstance()
                .collection("alarms")
                .whereEqualTo("destinationUid", uid)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    AlertDTOList.clear()
                    if(querySnapshot == null)return@addSnapshotListener
                    for (snapshot in querySnapshot.documents) {
                        AlertDTOList.add(snapshot.toObject(AlertDTO::class.java)!!)
                    }
                    AlertDTOList.sortByDescending { it.timestamp }
                    notifyDataSetChanged()
                }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val profileImage = holder.itemView.comment_imageview_profile
            val commentTextView = holder.itemView.comment_textview_comment

            FirebaseFirestore.getInstance().collection("profileImages")
                .document(AlertDTOList[position].uid!!).get().addOnCompleteListener {
                        task ->
                    if(task.isSuccessful){
                        val url = task.result["image"]
                        activity?.let {
                            Glide.with(it)
                                .load(url)
                                .apply(RequestOptions().circleCrop())
                                .into(profileImage)
                        }
                    }
                }

            when (AlertDTOList[position].kind) {
                0 -> {
                    val str_0 = AlertDTOList[position].userId + getString(R.string.favorite)
                    commentTextView.text = str_0
                }

                1 -> {
                    val str_1 = AlertDTOList[position].userId +
                            getString(R.string.user_id) +
                            AlertDTOList[position].message + getString(R.string.user_id)
                    commentTextView.text = str_1
                }

                2 -> {
                    val str_2 = AlertDTOList[position].userId + getString(R.string.follow)
                    commentTextView.text = str_2
                }
            }
        }

        override fun getItemCount(): Int {

            return AlertDTOList.size
        }
        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }
}