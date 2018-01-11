package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        fun newInstance(artist: Artist): PrepareFragment{
            val fragment = PrepareFragment()
            fragment.artist = artist
            return fragment
        }

        val LOOP_COUNT:Long = 5
    }

    lateinit var searchFetcher: SearchFetcher
    private lateinit var artist: Artist
    private lateinit var trackList: List<Track>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_prepare, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupKKboxApiClient()
        setupArtist(artist)
        searchTracks(artist)
        buttonStart.setOnClickListener{
            jump2PlayPage()
        }
    }

    private fun setupKKboxApiClient() {
        //  TODO("Need Dagger 2 here")
        val api = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
        searchFetcher = api.searchFetcher
    }

    private fun searchTracks(artist: Artist){
        progressBar.visibility = View.VISIBLE
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
                        progressBar.visibility = View.GONE
                        buttonStart.isClickable = true
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(this@PrepareFragment.context, "出現錯誤" ,Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.VISIBLE
                        buttonStart.isClickable = false
                    }

                })

    }

    private fun jump2PlayPage() {
        FragmentUtils.switchFragment(
                activity,
                PlayPageFragment.newInstance(artist.name, trackList),
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
