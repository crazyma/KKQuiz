package com.beibeilab.kkquiz.prepare

import com.beibeilab.kkquiz.base.Presenter
import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver

/**
 * Created by david on 2018/1/19.
 */
class PreparePresenter(private val prepareView: PrepareView,
                       private val prepareInteracor: PrepareInteractor,
                       private val artist: Artist) : Presenter {

    var disposable: Disposable? = null

    override fun onResume() {
        prepareView.setArtistImage(artist.images.last().url)
        prepareView.setArtistName(artist.name)
        getTrackList(artist)
    }

    override fun onPause() {}

    override fun onDestroy() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    private fun getTrackList(artist: Artist) {
        prepareView.showProgress()
        disposable = prepareInteracor.getTrackList(artist)
                .subscribeWith(object : DisposableSingleObserver<List<Track>>() {
                    override fun onSuccess(list: List<Track>) {
                        prepareView.hideProgress()
                        prepareView.enablePlayButton {
                            prepareView.startRippleAnim()
                        }
                    }

                    override fun onError(e: Throwable) {
                        prepareView.hideProgress()
                        prepareView.showErrorToast()
                        prepareView.disablePlayButton()
                    }

                })
    }


}