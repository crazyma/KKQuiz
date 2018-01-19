package com.beibeilab.kkquiz.search

import com.beibeilab.kkquiz.base.Presenter
import com.beibeilab.kkquiz.model.Artist
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by david on 2018/1/19.
 */
class SearchPresenter(private val searchView: SearchView, private val searchInteractor: SearchInteractor) : Presenter {

    var disposable: Disposable? = null

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onDestroy() {
        disposable!!.dispose()
    }

    fun onSearchButtonClicked() {
        val string = searchView.getEditTextString()
        if (string != null) {
            if (string.isNotEmpty() && string.isNotBlank()) {
                disposable = searchInteractor.doSearch(string)
                        .subscribeOn(Schedulers.io())
                        .map { it ->
                            val gson = Gson()
                            gson.fromJson(it, Artist::class.java)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSubscriber<Artist>() {
                            override fun onNext(artist: Artist) {
                                //  準備跳轉
                                searchView.onSearchFinished(artist)
                            }

                            override fun onError(t: Throwable) {
                                searchView.showErrorToast()
                                searchView.enableButton()
                                searchView.hideProgressBar()
                            }

                            override fun onComplete() {
                                searchView.enableButton()
                                searchView.hideProgressBar()
                            }

                        })
            }
        }
    }

}