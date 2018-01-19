package com.beibeilab.kkquiz.prepare

import android.content.Context
import com.beibeilab.kkquiz.BuildConfig
import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kkbox.openapideveloper.api.Api
import com.kkbox.openapideveloper.api.SearchFetcher
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by david on 2018/1/19.
 */
class PrepareInteractor(val context: Context,var listener: PrepareInteractorListener? = null) {

    interface PrepareInteractorListener{
        fun onLoadTrackListFinished(list: List<Track>)
    }

    private var trackList: List<Track>? = null
    private val searchFetcher: SearchFetcher

    init {
        val api = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
        searchFetcher = api.searchFetcher
    }

    fun getTrackList(artist: Artist): Single<List<Track>> {

        if (trackList != null)
            return Single.just(trackList)

        return Flowable.just(artist.name)
                .subscribeOn(Schedulers.io())
                .map {
                    searchFetcher.setSearchCriteria(it, "track")
                            .fetchSearchResult(50)
                            .get()
                }
                .map {
                    it.getAsJsonObject("tracks")
                            .getAsJsonArray("data")
                            .toString()
                }
                .map {
                    val gson = Gson()
                    gson.fromJson<List<Track>>(it, object : TypeToken<List<Track>>() {}.type)
                }
                .map {
                    Collections.shuffle(it)
                    it
                }
                .flatMap {
                    Flowable.fromIterable(it)
                }
                .filter {
                    it.album.artist.id == artist.id
                }
                .take(PrepareFragment.LOOP_COUNT)
                .toList()
                .doOnSuccess {
                    //  在這裡處理資料流的原因是，不想要透過 View or Presenter 傳遞資料
                    //  所以乾脆直接透過 Listener 來給
                    listener!!.onLoadTrackListFinished(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

}