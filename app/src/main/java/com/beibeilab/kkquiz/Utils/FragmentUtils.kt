package com.beibeilab.kkquiz.Utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

/**
 * Created by david on 2018/1/7.
 *
 *
 */

class FragmentUtils {
    companion object {
        val FRAGMENT_TAG_SEARCH = "search"
        val FRAGMENT_TAG_PLAY = "play"
        val FRAGMENT_TAG_RESULT = "result"

        fun setupFragment(activity: FragmentActivity, fragment: Fragment, layoutID: Int) {
            // TODO("Think about how to improve this class by Dagger2")

            val fragmentManager = activity.supportFragmentManager

            if (fragmentManager.findFragmentById(layoutID) == null) {
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(layoutID, fragment)
                fragmentTransaction.commit()
            }
        }

        fun switchFragment(activity: FragmentActivity, fragment: Fragment, layoutID: Int, backStackName: String? = null) {
            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(layoutID, fragment)
            fragmentTransaction.addToBackStack(backStackName)
            fragmentTransaction.commit()
        }

        fun backFragment(activity: FragmentActivity, tag: String) {
            val fm = activity.supportFragmentManager
            fm.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        fun getCurrnetFragment(activity: FragmentActivity, id: Int): Fragment {
            val fm = activity.supportFragmentManager
            return fm.findFragmentById(id)
        }

        fun clearAllStack(activity: FragmentActivity) {
            val fm = activity.supportFragmentManager
            val backStackCount = fm.backStackEntryCount
            for (i in 0 until backStackCount) {
                val backStackId = fm.getBackStackEntryAt(i).id
                fm.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }
}