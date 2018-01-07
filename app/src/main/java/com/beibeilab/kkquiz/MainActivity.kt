package com.beibeilab.kkquiz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.beibeilab.kkquiz.Utils.FragmentUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val songList = ArrayList<String>()
        songList.add("OseG-8qU8UtszwJlXm")
        songList.add("4ql_l_98WUFosMGFiW")

        FragmentUtils.setupFragment(
                this,
                SearchFragment.newInstance(),
                R.id.fragment_content
        )

        val apiKey = BuildConfig.KKBOX_ACCESS_TOKEN

    }
}
