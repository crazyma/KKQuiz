package com.beibeilab.kkquiz.playpage

import com.beibeilab.kkquiz.base.Presenter
import com.beibeilab.kkquiz.playpage.PlayPageFragment.Companion.BLANK_PAGE

/**
 * Created by david on 2018/1/20.
 */
class PlayPagePresenter(
        private val playPageView: PlayPageView,
        private val playPageModel: PlayPageModel,
        private var playPageListener: PlayPageFragment.PlayPageListener? = null) : Presenter {

    fun initialize() {
        playPageView.setupWebview()
        setCheckButton()
        setNextButton()
    }

    override fun onResume() {
        playMusic()
    }

    override fun onPause() {
        playPageView.loadUrlInWebview(BLANK_PAGE)
    }

    override fun onDestroy() {

    }

    private fun playMusic() {
        playPageView.showPlayLayout()
        playPageView.setCountText(playPageModel.index)
        playPageView.loadUrlInWebview(
                playPageModel.getCurrentTrackUrl())
        playPageView.startYoyoAnim()
    }

    private fun showAnswer() {
        playPageView.showAnswerLayout()
        playPageView.setTrackText(playPageModel.getCurrentTrackName())
        playPageView.setAlbumImage(playPageModel.getCurrentAlbumImageUrl())
        playPageView.setAlbumText(playPageModel.getCurrentAlbumName())
        playPageView.loadUrlInWebview(BLANK_PAGE)
        playPageView.stopYoyoAnim()
    }

    private fun setCheckButton() {
        playPageView.setCheckButtonClickListener {
            showAnswer()
        }
    }

    private fun setNextButton() {
        playPageView.setNextButtonClickListener {
            if (playPageModel.nextSong()) {
                playMusic()
            } else {
                playPageListener!!.onGameRoundEnd(playPageModel.artist)
            }
        }
    }
}