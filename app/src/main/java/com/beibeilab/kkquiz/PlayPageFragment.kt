package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.beibeilab.kkquiz.Utils.FragmentUtils
import com.beibeilab.kkquiz.model.Album
import com.beibeilab.kkquiz.model.Track
import com.google.gson.Gson
import com.kkbox.openapideveloper.api.Api
import com.kkbox.openapideveloper.api.TrackFetcher
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.fragment_play_page.*
import java.lang.StringBuilder


/**
 * A simple [Fragment] subclass.
 */
class PlayPageFragment : Fragment() {

    companion object {
        val urlGithubSample = "https://wubaibai.github.io/kkGame/?song=OseG-8qU8UtszwJlXm&song=4ql_l_98WUFosMGFiW&autoplay=true"
        val urlGithub = "https://wubaibai.github.io/kkGame/?autoplay=true"
        val LAYOUT_PREPARE = 0
        val LAYOUT_PLAY = 1
        val LAYOUT_ANSWER = 2
        fun newInstance(trackList: List<Track>): PlayPageFragment {
            val fragment = PlayPageFragment()
            fragment.trackList = trackList
            return fragment
        }
    }

    private val totalTrackNumber = 2
    private var index = 0

    lateinit var trackList: List<Track>

    private lateinit var selectedTrackId: String
    private lateinit var selectedTrackName: String
    lateinit var artist: String


    private lateinit var trackFetcher: TrackFetcher

    private lateinit var webView: WebView
    private lateinit var startButton: ImageView
    private lateinit var checkAnswerButton: Button
    private lateinit var nextButton: Button
    private lateinit var trackNameText: TextView
    private lateinit var albumText: TextView
    private lateinit var albumImage: ImageView
    private lateinit var artistText: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_play_page, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view!!)
        setupClickEvent()
        setupWebview()
        setupKKBoxClint()

        artistText.text = artist
    }

    private fun findViews(view: View) {
        webView = view.findViewById(R.id.webView)
        startButton = view.findViewById(R.id.buttonStart)
        checkAnswerButton = view.findViewById(R.id.buttonCheckAnswer)
        nextButton = view.findViewById(R.id.buttonNext)
        artistText = view.findViewById(R.id.textArtist)
        trackNameText = view.findViewById(R.id.textTrack)
        albumText = view.findViewById(R.id.textAlbum)
        albumImage = view.findViewById(R.id.imageAlbum)
    }

    private fun setupClickEvent() {
        startButton.setOnClickListener {
            showLayout(LAYOUT_PLAY)
        }

        checkAnswerButton.setOnClickListener {
            showLayout(LAYOUT_ANSWER)
        }

        nextButton.setOnClickListener {

            if(index++ < totalTrackNumber){
                showLayout(LAYOUT_PLAY)
            }else {
                FragmentUtils.switchFragment(
                        this@PlayPageFragment.activity,
                        ResultFragment.newInstance(artist),
                        R.id.fragment_content,
                        FragmentUtils.FRAGMENT_TAG_PLAY)
            }
        }

    }

    private fun setupWebview() {
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = WebChromeClient()
    }

    private fun loadMusicUrl(urlString: String) {
        webView.loadUrl(urlString)
    }

    private fun getSelectedTrack() {
        val selectedIndex = (Math.random() * trackList.size).toInt()

        selectedTrackId = trackList[selectedIndex].id
        selectedTrackName = trackList[selectedIndex].name

        trackNameText.text = selectedTrackName
    }

    private fun getWidgetUrl(id: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(urlGithub)
        stringBuilder.append("&song=")
        stringBuilder.append(id)

        return stringBuilder.toString()
    }

    private fun showLayout(flag: Int) {
        when (flag) {
            LAYOUT_PREPARE -> {
                includePrepare.visibility = View.VISIBLE
                includePlay.visibility = View.GONE
                includeAnswer.visibility = View.GONE
                loadMusicUrl("about:blank")
            }
            LAYOUT_PLAY -> {
                includePrepare.visibility = View.GONE
                includePlay.visibility = View.VISIBLE
                includeAnswer.visibility = View.GONE

                getSelectedTrack()

                loadTrackInfo(selectedTrackId)
                loadMusicUrl(getWidgetUrl(selectedTrackId))
            }
            LAYOUT_ANSWER -> {
                includePrepare.visibility = View.GONE
                includePlay.visibility = View.GONE
                includeAnswer.visibility = View.VISIBLE
                loadMusicUrl("about:blank")
            }
        }
    }

    private fun setupKKBoxClint() {
        trackFetcher = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
                .trackFetcher
    }

    private fun loadTrackInfo(id: String) {
        val result = trackFetcher.setTrackId(id)
                .fetchMetadata()
                .get()

        Log.d("crazyma", "result : $result")


        Flowable.just(id)
                .subscribeOn(Schedulers.io())
                .map {
                    trackFetcher.setTrackId(id)
                            .fetchMetadata()
                            .get()
                }
                .map {
                    it.getAsJsonObject("album")
                }
                .map {
                    val gson = Gson()
                    gson.fromJson(it, Album::class.java)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<Album>(){
                    override fun onNext(t: Album) {
                        Log.d("crazyma","!!!!!! " + t.id + ", " + t.name + ", " + t.images[t.images.size - 1].url)

                        albumText.text = t.name
                        Picasso.with(this@PlayPageFragment.context)
                                .load(t.images[t.images.size - 1].url)
                                .into(albumImage)
                    }

                    override fun onError(t: Throwable?) {

                    }

                    override fun onComplete() {

                    }
                })
    }

}// Required empty public constructor
