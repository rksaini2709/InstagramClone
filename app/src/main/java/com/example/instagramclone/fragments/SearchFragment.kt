package com.example.instagramclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.Models.User
import com.example.instagramclone.adapters.SearchAdapter
import com.example.instagramclone.databinding.FragmentSearchBinding
import com.example.instagramclone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

// Define a Fragment for searching users
class SearchFragment : Fragment() {

    // View binding object for the fragment layout
    lateinit var binding : FragmentSearchBinding

    lateinit var adapter: SearchAdapter

    // List to hold user objects
    var userList = ArrayList<User>()

    // Called to do initial creation of the fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Any initialization code for the fragment can be done here
    }

    // Create the view hierarchy associated with the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        // Return the root view of the inflated layout

        // Set up RecyclerView with LinearLayoutManager
        binding.searchList.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter with an empty user list and set it to the RecyclerView
        adapter = SearchAdapter(requireContext(), userList)
        binding.searchList.adapter = adapter

        // Fetch user data from Firestore and populate the user list
        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            var tempList = ArrayList<User>()
            userList.clear()
            for (i in it.documents){
                // Check if the document is not the current user's data
                if (!i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())) {
                    // Convert Firestore document to User object and add to temporary list
                    var user : User = i.toObject<User>()!!
                    tempList.add(user)
                }
            }
            // Update the user list and notify the adapter of changes
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        // Set up search button click listener
        binding.searchButton.setOnClickListener {
            // Get the text entered in the search view
            var text = binding.searchView.text.toString()
            // Query Firestore for users matching the search query
            Firebase.firestore.collection(USER_NODE).whereEqualTo("name",text).get().addOnSuccessListener {
                var tempList = ArrayList<User>()
                userList.clear()
                if(it.isEmpty){
                    // Handle case where no users match the search query
                } else {
                    for (i in it.documents){
                        // Check if the document is not the current user's data
                        if (!i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())) {
                            // Convert Firestore document to User object and add to temporary list
                            var user : User = i.toObject<User>()!!
                            tempList.add(user)
                        }
                    }
                    // Update the user list and notify the adapter of changes
                    userList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        return binding.root
    }

    companion object {
        // Any companion object members can be defined here
    }
}