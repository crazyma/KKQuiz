package com.beibeilab.kkquiz.Utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by david on 2018/1/7.
 *
 *
 */

class FragmentUtils {
    companion object {
        fun setupFragment(activity: FragmentActivity, fragment: Fragment, layoutID: Int) {
            // TODO("Think about how to improve this class by Dagger2")

            val fragmentManager = activity.supportFragmentManager

            if (fragmentManager.findFragmentById(layoutID) == null) {
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(layoutID, fragment)
                fragmentTransaction.commit()
            }
        }

        fun switchFragment(activity: FragmentActivity, fragment: Fragment, layoutID: Int) {
            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(layoutID, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}