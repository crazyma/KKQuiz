package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.beibeilab.kkquiz.Utils.FragmentUtils
import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import com.google.gson.Gson
import com.kkbox.openapideveloper.api.Api
import com.kkbox.openapideveloper.api.SearchFetcher
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.fragment_search.*


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
    lateinit var searchString: String

    private lateinit var searchButton: Button
    private lateinit var editText: EditText
    private lateinit var artistText: TextView
    private lateinit var artistImage: ImageView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view!!)
        setupKKboxApiClient()
        searchButton.setOnClickListener {
            searchString = editText.text.toString().trim()
            if (searchString.isNotBlank() && searchString.isNotEmpty())
                jump2PreparePage(searchString)
        }
    }

    private fun findViews(view: View) {
        searchButton = view.findViewById(R.id.searchButton)
        editText = view.findViewById(R.id.editText)
        artistText = view.findViewById(R.id.textArtist)
        artistImage = view.findViewById(R.id.imageArtist)
    }

    private fun setupKKboxApiClient() {
        //  TODO("Need Dagger 2 here")
        val api = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
        searchFetcher = api.searchFetcher
    }

    private fun doSearch(string: String) {

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
                        Log.d("crazyma", "!!!!!!!  " + artist.id + " " + artist.name + " " + artist.images.last().url)
                        showLayout()
                        setupArtist(artist)
                    }

                    override fun onError(t: Throwable) {

                    }

                    override fun onComplete() {

                    }

                })


//        Flowable.just(string)
//                .subscribeOn(Schedulers.io())
//                .map {
//                    searchFetcher.setSearchCriteria(it, "track")
//                            .fetchSearchResult(50)
//                            .get()
//                }
//                .map {
//                    it.getAsJsonObject("tracks").getAsJsonArray("data").toString()
//                }
//                .map(Function<String, List<Track>> { t ->
//                    val gson = Gson()
//                    val type = object : TypeToken<List<Track>>() {}.type
//                    gson.fromJson(t, type)
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSubscriber<List<Track>>() {
//
//                    override fun onNext(trackList: List<Track>?) {
//                        Log.d("crazyma", "track " + trackList!![0].name)
//                        Log.d("crazyma", "track " + trackList!![1].name)
//                        jump2PlayPage(trackList)
//                    }
//
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onError(t: Throwable?) {
//
//                    }
//                })
    }

    private fun showLayout() {
        includeSearch.visibility = View.GONE
        includePrepare.visibility = View.VISIBLE
    }

    private fun setupArtist(artist: Artist) {
        artistText.text = artist.name
        Picasso.with(context)
                .load(artist.images.last().url)
                .into(artistImage)
    }

    private fun jump2PreparePage(searchString: String){
        FragmentUtils.switchFragment(
                activity,
                PrepareFragment.newInstance(searchString),
                R.id.fragment_content,
                FragmentUtils.FRAGMENT_TAG_PREPARE
        )
    }


    private fun jump2PlayPage(trackList: List<Track>) {
        FragmentUtils.switchFragment(
                activity,
                PlayPageFragment.newInstance(searchString, trackList),
                R.id.fragment_content,
                FragmentUtils.FRAGMENT_TAG_SEARCH
        )
    }

}// Required empty public constructor
