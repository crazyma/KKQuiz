package com.beibeilab.kkquiz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.beibeilab.kkquiz.Utils.FragmentUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FragmentUtils.setupFragment(
                this,
                SearchFragment.newInstance(),
                R.id.fragment_content
        )

    }

    override fun onBackPressed() {
        val fragment = FragmentUtils.getCurrnetFragment(this, R.id.fragment_content)

        if (fragment is ResultFragment) {
            FragmentUtils.backFragment(
                    this, FragmentUtils.FRAGMENT_TAG_SEARCH)
        }else{
            super.onBackPressed()
        }
    }
}
