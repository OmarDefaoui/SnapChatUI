package com.nordef.snapchatui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.nordef.snapchatui.fragments.ChatFragment
import com.nordef.snapchatui.fragments.EmptyFragment
import com.nordef.snapchatui.fragments.StoryFragment

class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        //tabview position
        when (position) {
            0 -> return ChatFragment()
            1 -> return EmptyFragment()
            2 -> return StoryFragment()
            else -> return EmptyFragment()
        }
    }

    override fun getCount(): Int {
        //number of screen
        return 3
    }
}