// Import necessary packages and libraries
package com.example.instagramclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instagramclone.R

// TagFragment class extending Fragment
class TagFragment : Fragment() {

    // Function called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Any initialization code for the fragment can be placed here
    }

    // Function called to create the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using the specified layout resource ID
        return inflater.inflate(R.layout.fragment_tag, container, false)
    }

    companion object {
        // You can define any companion objects or static methods/constants here
    }
}
