package com.beibeilab.kkquiz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.beibeilab.kkquiz.Utils.FragmentUtils
import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import com.beibeilab.kkquiz.playpage.PlayPageFragment
import com.beibeilab.kkquiz.prepare.PrepareFragment
import com.beibeilab.kkquiz.result.ResultFragment
import com.beibeilab.kkquiz.search.SearchFragment

class MainActivity : AppCompatActivity(),
        SearchFragment.SearchFragmentListener,
        PrepareFragment.PrepareFragmentListener,
        PlayPageFragment.PlayPageListener {

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
        } else {
            super.onBackPressed()
        }
    }

    override fun onSearchFinished(artist: Artist) {
        FragmentUtils.switchFragmentWithFade(
                this,
                PrepareFragment.newInstance(artist),
                R.id.fragment_content,
                FragmentUtils.FRAGMENT_TAG_SEARCH
        )
    }

    override fun onPreparationFinished(artist: Artist, list: List<Track>) {
        FragmentUtils.switchFragmentWithFade(
                this,
                PlayPageFragment.newInstance(artist, list),
                R.id.fragment_content,
                FragmentUtils.FRAGMENT_TAG_PREPARE
        )
    }

    override fun onGameRoundEnd(artist: Artist) {
        FragmentUtils.switchFragmentWithFade(
                this,
                ResultFragment.newInstance(artist),
                R.id.fragment_content,
                FragmentUtils.FRAGMENT_TAG_PREPARE
        )
    }
}
