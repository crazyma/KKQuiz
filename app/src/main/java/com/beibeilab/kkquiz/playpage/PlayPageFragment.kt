package com.beibeilab.kkquiz.playpage


import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import com.beibeilab.kkquiz.R
import com.beibeilab.kkquiz.prepare.PrepareFragment.Companion.LOOP_COUNT
import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_play_page.*
import kotlinx.android.synthetic.main.content_answer.*
import kotlinx.android.synthetic.main.content_play.*
import java.lang.StringBuilder

/**
 * A simple [Fragment] subclass.
 */
class PlayPageFragment : Fragment(), PlayPageView {

    interface PlayPageListener {
        fun onGameRoundEnd(artist: Artist)
    }

    companion object {
        val urlGithubSample = "https://wubaibai.github.io/kkGame/?song=OseG-8qU8UtszwJlXm&song=4ql_l_98WUFosMGFiW&autoplay=true"
        val urlGithub = "https://wubaibai.github.io/kkGame/?autoplay=true"
        val urlKKBoxWidget = "https://widget.kkbox.com/v1/?type=song&terr=TW&autoplay=true&loop=false"
        val BLANK_PAGE = "about:blank"
        fun newInstance(artist: Artist, trackList: List<Track>): PlayPageFragment {
            val fragment = PlayPageFragment()
            fragment.trackList = trackList
            fragment.artist = artist
            return fragment
        }
    }

    //  TODO trackList & artist 除了做出 Model 已經沒有功用，應該可以移除
    lateinit var trackList: List<Track>
    lateinit var artist: Artist
    var playPageListener: PlayPageListener? = null

    private lateinit var leftYoyo: YoYo.YoYoString
    private lateinit var rightYoyo: YoYo.YoYoString

    private lateinit var playPagePresenter: PlayPagePresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity is PlayPageListener) {
            playPageListener = activity as PlayPageListener
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_play_page, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playPageModel = PlayPageModel(trackList, artist)
        playPagePresenter = PlayPagePresenter(
                this,
                playPageModel,
                playPageListener)
        playPagePresenter.initialize()

    }

    override fun onResume() {
        super.onResume()
        playPagePresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        playPagePresenter.onPause()
    }

    override fun onDestroy() {
        playPagePresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        playPageListener = null
        super.onDetach()
    }

    override fun setupWebview() {
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = WebChromeClient()
    }

    override fun loadUrlInWebview(urlString: String) {
        webView.loadUrl(urlString)
    }

    override fun showPlayLayout() {
        includePlay.visibility = View.VISIBLE
        includeAnswer.visibility = View.GONE
    }

    override fun showAnswerLayout() {
        includePlay.visibility = View.GONE
        includeAnswer.visibility = View.VISIBLE
    }

    override fun setCountText(index: Int) {
        val builder = StringBuilder()
        builder.append(index + 1)
        builder.append(" / ")
        builder.append(LOOP_COUNT)
        textCount.text = builder.toString()
    }

    override fun setTrackText(name: String) {
        textTrack.text = name
    }

    override fun setAlbumImage(urlString: String) {
        Picasso.with(context)
                .load(urlString)
                .into(imageAlbum)
    }

    override fun setAlbumText(name: String) {
        textAlbum.text = name
    }

    override fun startYoyoAnim() {
        leftYoyo = YoYo.with(Techniques.Pulse)
                .duration(600)
                .delay(300)
                .repeat(ValueAnimator.INFINITE)
                .playOn(imageLeftRipple)

        rightYoyo = YoYo.with(Techniques.Pulse)
                .duration(600)
                .delay(150)
                .repeat(ValueAnimator.INFINITE)
                .playOn(imageRightRipple)
    }

    override fun stopYoyoAnim() {
        leftYoyo.stop()
        rightYoyo.stop()
    }

    override fun setCheckButtonClickListener(foo: () -> Unit) {
        buttonCheckAnswer.setOnClickListener {
            foo()
        }
    }

    override fun setNextButtonClickListener(foo: () -> Unit) {
        buttonNext.setOnClickListener {
            foo()
        }
    }

}// Required empty public constructor
