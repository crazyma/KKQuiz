package com.beibeilab.kkquiz.result

import com.beibeilab.kkquiz.base.Presenter
import com.beibeilab.kkquiz.model.Artist

/**
 * Created by david on 2018/1/23.
 */
class ResultPresenter(val resultView: ResultView, val artist: Artist) : Presenter {

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onDestroy() {

    }

    fun initialize() {
        resultView.setupWebview()
        resultView.setupArtistText(artist.name)
        resultView.setupKKBoxButton {
            resultView.webViewLoadUrl(ResultFragment.DEEP_LINK + artist.id)
        }
        resultView.setupPlayAgainButton {  }
    }
}