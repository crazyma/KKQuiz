package com.beibeilab.kkquiz


import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.beibeilab.kkquiz.Utils.FragmentUtils
import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.kkbox.openapideveloper.api.Api
import com.kkbox.openapideveloper.api.TrackFetcher
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_play_page.*
import java.lang.StringBuilder


/**
 * A simple [Fragment] subclass.
 */
class PlayPageFragment : Fragment() {

    companion object {
        val urlGithubSample = "https://wubaibai.github.io/kkGame/?song=OseG-8qU8UtszwJlXm&song=4ql_l_98WUFosMGFiW&autoplay=true"
        val urlGithub = "https://wubaibai.github.io/kkGame/?autoplay=true"
        val urlKKBoxWidget = "https://widget.kkbox.com/v1/?type=song&terr=TW&autoplay=true&loop=false"
        val BLANK_PAGE = "about:blank"
        val LAYOUT_PLAY = 1
        val LAYOUT_ANSWER = 2
        fun newInstance(artist: Artist, trackList: List<Track>): PlayPageFragment {
            val fragment = PlayPageFragment()
            fragment.trackList = trackList
            fragment.artist = artist
            return fragment
        }
    }

    private var index = 0

    lateinit var trackList: List<Track>

    private lateinit var selectedTrackId: String
    private lateinit var selectedTrackName: String
    lateinit var artist: Artist


    private lateinit var trackFetcher: TrackFetcher
    private lateinit var leftYoyo: YoYo.YoYoString
    private lateinit var rightYoyo: YoYo.YoYoString

    private lateinit var webView: WebView
    private lateinit var checkAnswerButton: Button
    private lateinit var nextButton: Button
    private lateinit var trackNameText: TextView
    private lateinit var albumText: TextView
    private lateinit var albumImage: ImageView
    private lateinit var leftRippleImage: ImageView
    private lateinit var rightRippleImage: ImageView

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

    override fun onStop() {
        super.onStop()
        webView.loadUrl(BLANK_PAGE)
    }

    private fun findViews(view: View) {
        webView = view.findViewById(R.id.webView)
        checkAnswerButton = view.findViewById(R.id.buttonCheckAnswer)
        nextButton = view.findViewById(R.id.buttonNext)
        trackNameText = view.findViewById(R.id.textTrack)
        albumText = view.findViewById(R.id.textAlbum)
        albumImage = view.findViewById(R.id.imageAlbum)
        leftRippleImage = view.findViewById(R.id.imageLeftRipple)
        rightRippleImage = view.findViewById(R.id.imageRightRipple)
    }

    private fun setupClickEvent() {

        checkAnswerButton.setOnClickListener {
            showLayout(LAYOUT_ANSWER)
        }

        nextButton.setOnClickListener {

            if (index < trackList.size) {
                showLayout(LAYOUT_PLAY)
            } else {
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

        if (urlString == BLANK_PAGE) {
            leftYoyo.stop()
            rightYoyo.stop()
        } else {
            leftYoyo = YoYo.with(Techniques.Pulse)
                    .duration(600)
                    .delay(300)
                    .repeat(ValueAnimator.INFINITE)
                    .playOn(leftRippleImage)

            rightYoyo = YoYo.with(Techniques.Pulse)
                    .duration(600)
                    .delay(150)
                    .repeat(ValueAnimator.INFINITE)
                    .playOn(rightRippleImage)
        }


    }

    private fun setupSelectedTrack(index: Int) {
        selectedTrackId = trackList[index].id
        selectedTrackName = trackList[index].name

        trackNameText.text = selectedTrackName

        val album = trackList[index].album
        albumText.text = album.name
        Picasso.with(this@PlayPageFragment.context)
                .load(album.images[album.images.size - 1].url)
                .into(albumImage)
    }

    private fun getWidgetUrl(id: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(urlKKBoxWidget)
        stringBuilder.append("&id=")
        stringBuilder.append(id)

        return stringBuilder.toString()
    }

    private fun showLayout(flag: Int) {
        when (flag) {
            LAYOUT_PLAY -> {
                includePlay.visibility = View.VISIBLE
                includeAnswer.visibility = View.GONE

                setupSelectedTrack(index++)

                loadMusicUrl(getWidgetUrl(selectedTrackId))
            }
            LAYOUT_ANSWER -> {
                includePlay.visibility = View.GONE
                includeAnswer.visibility = View.VISIBLE

                loadMusicUrl(BLANK_PAGE)
            }
        }
    }

    private fun setupKKBoxClint() {
        trackFetcher = Api(BuildConfig.KKBOX_ACCESS_TOKEN, "TW", context)
                .trackFetcher
    }

}// Required empty public constructor
