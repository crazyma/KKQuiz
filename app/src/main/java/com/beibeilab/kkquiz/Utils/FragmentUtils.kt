package com.beibeilab.kkquiz.Utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.transition.*


/**
 * Created by david on 2018/1/7.
 *
 *
 */

class FragmentUtils {
    companion object {
        val FRAGMENT_TAG_SEARCH = "search"
        val FRAGMENT_TAG_PLAY = "play"
        val FRAGMENT_TAG_PREPARE = "prepare"
        val FRAGMENT_TAG_RESULT = "result"

        val FADE_DEFAULT_TIME: Long = 500
        val EXPLODE_DEFAULT_TIME: Long = 300

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

        fun switchFragmentWithFade(activity: FragmentActivity, nextFragment: Fragment, layoutID: Int, backStackName: String? = null) {
            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val previousFragment = fragmentManager.findFragmentById(layoutID)

            val exitFade = Fade()
            exitFade.duration = FADE_DEFAULT_TIME
            previousFragment.exitTransition = exitFade


            val enterFade = Fade()
            enterFade.startDelay = FADE_DEFAULT_TIME / 2
            enterFade.duration = FADE_DEFAULT_TIME
            nextFragment.enterTransition = enterFade

            fragmentTransaction.replace(layoutID, nextFragment)
            fragmentTransaction.addToBackStack(backStackName)
            fragmentTransaction.commit()
        }

        fun switchFragmentWithExplode(activity: FragmentActivity, previousFragment: Fragment, nextFragment: Fragment, layoutID: Int, backStackName: String? = null) {
            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val exitFade = Explode()
            exitFade.duration = EXPLODE_DEFAULT_TIME
            previousFragment.exitTransition = exitFade


            val enterFade = Fade()
            enterFade.startDelay = EXPLODE_DEFAULT_TIME
            enterFade.duration = FADE_DEFAULT_TIME
            nextFragment.enterTransition = enterFade

            fragmentTransaction.replace(layoutID, nextFragment)
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