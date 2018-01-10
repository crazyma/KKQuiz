package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beibeilab.kkquiz.model.Artist
import com.google.gson.Gson
import com.kkbox.openapideveloper.api.Api
import com.kkbox.openapideveloper.api.SearchFetcher
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.fragment_prepare.*


/**
 * A simple [Fragment] subclass.
 */
class PrepareFragment : Fragment() {

    companion object {
        fun newInstance(searchString: String): PrepareFragment{
            val fragment = PrepareFragment()
            fragment.searchString = searchString
            return fragment
        }
    }

    lateinit var searchFetcher: SearchFetcher
    private lateinit var searchString: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_prepare, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupKKboxApiClient()
        searchArtist(searchString)
    }

    private fun setupKKboxApiClient() {
        //  TODO("Need Dagger 2 here")
        val api = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
        searchFetcher = api.searchFetcher
    }

    private fun searchArtist(string: String) {

        Flowable.just(string)
                .subscribeOn(Schedulers.io())
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
                .map {
                    val gson = Gson()
                    gson.fromJson(it, Artist::class.java)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<Artist>() {
                    override fun onNext(artist: Artist) {
                        setupArtist(artist)
                    }

                    override fun onError(t: Throwable) {

                    }

                    override fun onComplete() {

                    }

                })
    }

    private fun setupArtist(artist: Artist) {
        textArtist.text = artist.name
        Picasso.with(context)
                .load(artist.images.last().url)
                .into(imageArtist)
    }

}// Required empty public constructor
