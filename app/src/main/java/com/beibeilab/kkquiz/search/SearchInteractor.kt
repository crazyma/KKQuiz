package com.beibeilab.kkquiz.search

import android.content.Context
import com.beibeilab.kkquiz.BuildConfig
import com.kkbox.openapideveloper.api.Api
import com.kkbox.openapideveloper.api.SearchFetcher
import io.reactivex.Flowable

/**
 * Created by david on 2018/1/19.
 */
class SearchInteractor(context : Context) {

    private val searchFetcher: SearchFetcher

    init {
        val api = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
        searchFetcher = api.searchFetcher
    }

    fun doSearch(string: String) : Flowable<String>{
        return Flowable.just(string)
                .map {
                    searchFetcher.setSearchCriteria(it, "artist")
                            .fetchSearchResult(1)
                            .get()
                }
                .map {
                    it.getAsJsonObject("artists")
                            .getAsJsonArray("data")[0]
                            .toString()
                }
    }
}