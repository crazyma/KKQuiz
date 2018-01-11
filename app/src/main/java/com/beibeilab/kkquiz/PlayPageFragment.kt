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
        val LAYOUT_PLAY = 1
        val LAYOUT_ANSWER = 2
        fun newInstance(artistString: String,trackList: List<Track>): PlayPageFragment {
            val fragment = PlayPageFragment()
            fragment.trackList = trackList
            fragment.artistString = artistString
            return fragment
        }
    }

    private var index = 0

    lateinit var trackList: List<Track>

    private lateinit var selectedTrackId: String
    private lateinit var selectedTrackName: String
    lateinit var artistString: String


    private lateinit var trackFetcher: TrackFetcher

    private lateinit var webView: WebView
    private lateinit var checkAnswerButton: Button
    private lateinit var nextButton: Button
    private lateinit var trackNameText: TextView
    private lateinit var albumText: TextView
    private lateinit var albumImage: ImageView

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

        showLayout(LAYOUT_PLAY)
    }

    private fun findViews(view: View) {
        webView = view.findViewById(R.id.webView)
        checkAnswerButton = view.findViewById(R.id.buttonCheckAnswer)
        nextButton = view.findViewById(R.id.buttonNext)
        trackNameText = view.findViewById(R.id.textTrack)
        albumText = view.findViewById(R.id.textAlbum)
        albumImage = view.findViewById(R.id.imageAlbum)
    }

    private fun setupClickEvent() {

        checkAnswerButton.setOnClickListener {
            showLayout(LAYOUT_ANSWER)
        }

        nextButton.setOnClickListener {

            if(index < trackList.size){
                showLayout(LAYOUT_PLAY)
            }else {
                FragmentUtils.switchFragment(
                        this@PlayPageFragment.activity,
                        ResultFragment.newInstance(artistString),
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

    private fun getSelectedTrack(index: Int) {
        selectedTrackId = trackList[index].id
        selectedTrackName = trackList[index].name

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
            LAYOUT_PLAY -> {
                includePlay.visibility = View.VISIBLE
                includeAnswer.visibility = View.GONE

                getSelectedTrack(index++)

                loadTrackInfo(selectedTrackId)
                loadMusicUrl(getWidgetUrl(selectedTrackId))
            }
            LAYOUT_ANSWER -> {
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
