package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kkbox.openapideveloper.api.Api
import com.kkbox.openapideveloper.api.SearchFetcher
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.fragment_prepare.*
import com.beibeilab.kkquiz.Utils.FragmentUtils
import io.reactivex.observers.DisposableSingleObserver
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class PrepareFragment : Fragment() {

    companion object {
        fun newInstance(searchString: String): PrepareFragment{
            val fragment = PrepareFragment()
            fragment.searchArtistString = searchString
            return fragment
        }

        val LOOP_COUNT:Long = 5
    }

    lateinit var searchFetcher: SearchFetcher
    private lateinit var searchArtistString: String
    private lateinit var trackList: List<Track>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_prepare, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupKKboxApiClient()
        searchArtist(searchArtistString)
        buttonStart.setOnClickListener{
            jump2PlayPage()
        }
    }

    private fun setupKKboxApiClient() {
        //  TODO("Need Dagger 2 here")
        val api = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
        searchFetcher = api.searchFetcher
    }

    private fun searchArtist(artistString: String) {

        Flowable.just(artistString)
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
                        searchTracks(artist)
                    }

                    override fun onError(t: Throwable) {

                    }

                    override fun onComplete() {

                    }

                })
    }

    private fun searchTracks(artist: Artist){
        Flowable.just(artist.name)
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
                .map{
                    val gson = Gson()
                    gson.fromJson<List<Track>>(it, object : TypeToken<List<Track>>() {}.type)
                }
                .map{
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Track>>(){
                    override fun onSuccess(list: List<Track>) {
                        trackList = list
                        Log.d("crazyma","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }

                    override fun onError(e: Throwable) {

                    }

                })

    }

    private fun jump2PlayPage() {
        FragmentUtils.switchFragment(
                activity,
                PlayPageFragment.newInstance(searchArtistString, trackList),
                R.id.fragment_content,
                FragmentUtils.FRAGMENT_TAG_PREPARE
        )
    }

    private fun setupArtist(artist: Artist) {
        textArtist.text = artist.name
        Picasso.with(context)
                .load(artist.images.last().url)
                .into(imageArtist)
    }

}// Required empty public constructor
