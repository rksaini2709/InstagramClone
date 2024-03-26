package com.example.instagramclone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    // Initialize mutable lists to hold fragments and their titles
    private val fragmentList = mutableListOf<Fragment>()
    private val titleList = mutableListOf<String>()

    // Return the total number of fragments in the adapter
    override fun getItemCount(): Int = fragmentList.size

    // Create and return a fragment for the given position
    override fun createFragment(position: Int): Fragment = fragmentList[position]

    // Add a new fragment to the adapter along with its title
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }

    // Return the title of the fragment at the specified position
    fun getPageTitle(position: Int): CharSequence = titleList[position]
}
