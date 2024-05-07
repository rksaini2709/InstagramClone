package com.example.instagramclone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    // Initialize mutable lists to hold fragments and their titles
    val fragmentList = mutableListOf<Fragment>()
    val titleList = mutableListOf<String>()

    // Get the count of fragments in the list
    override fun getCount(): Int {
        return fragmentList.size
    }

    // Return the fragment at the specified position
    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    // Get the title of the fragment at the specified position
    override fun getPageTitle(position: Int): CharSequence? {
        // Ensure that the title is not null, and if it is, return an empty string
        return super.getPageTitle(position)
    }

    // Function to add fragments to the adapter
    fun addFragments(fragment: Fragment, title: String){
        fragmentList.add(fragment)
        titleList.add(title)
    }
}