package com.example.instagramclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramclone.Models.UploadPost
import com.example.instagramclone.adapters.UploadPostOnProfileAdapter
import com.example.instagramclone.databinding.FragmentMyPostBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class MyPostFragment : Fragment() {

    private lateinit var binding : FragmentMyPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using data binding
        binding = FragmentMyPostBinding.inflate(inflater, container, false)

        var postList = ArrayList<UploadPost>()
        var adapter = UploadPostOnProfileAdapter(requireContext(), postList)
        binding.uploadedPostOnProfile.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        binding.uploadedPostOnProfile.adapter = adapter

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {it ->
            var tempList = arrayListOf<UploadPost>()
            for (i in it. documents){
                var post : UploadPost = i.toObject<UploadPost>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)

            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged()
        }

        return binding.root
        }

    companion object {

    }
}