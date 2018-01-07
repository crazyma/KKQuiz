package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beibeilab.kkquiz.model.Track
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kkbox.openapideveloper.api.Api
import com.kkbox.openapideveloper.api.SearchFetcher
import io.reactivex.Flowable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.fragment_search.*
import com.google.gson.reflect.TypeToken
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    lateinit var searchFetcher: SearchFetcher

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupKKboxApiClient()
        searchButton.setOnClickListener {
            val searchString = editText.text.toString().trim()
            doSearch(searchString)
        }
    }

    private fun setupKKboxApiClient() {
        //  TODO("Need Dagger 2 here")
        val api = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
        searchFetcher = api.searchFetcher
    }

    private fun doSearch(string: String) {
        val searchResult =
                searchFetcher.setSearchCriteria(string, "track")
                        .fetchSearchResult(50)
                        .get()

        Log.d("crazyma", "search result: $searchResult")

        Flowable.just(string)
                .subscribeOn(Schedulers.io())
                .map {
                    searchFetcher.setSearchCriteria(it, "track")
                            .fetchSearchResult(50)
                            .get()
                }
                .map {
                    it.getAsJsonObject("tracks").getAsJsonArray("data").toString()
                }
                .map(Function<String, List<Track>> { t ->
                    val gson = Gson()
                    val type = object : TypeToken<List<Track>>() {}.type
                    gson.fromJson(t, type)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Track>>() {

                    override fun onNext(trackList: List<Track>?) {
                        Log.d("crazyma", "track " + trackList!![0].name)
                        Log.d("crazyma", "track " + trackList!![1].name)
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }
                })
    }

}// Required empty public constructor
