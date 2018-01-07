package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import com.beibeilab.kkquiz.model.Track
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

    lateinit var trackList: List<Track>
    val songCount = 1

    private lateinit var webView: WebView
    private lateinit var startButton: ImageView
    private lateinit var checkAnswerButton: Button

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
    }

    private fun findViews(view: View) {
        webView = view.findViewById(R.id.webView)
        startButton = view.findViewById(R.id.buttonStart)
        checkAnswerButton = view.findViewById(R.id.buttonCheckAnswer)
    }

    private fun setupClickEvent() {
        startButton.setOnClickListener {
            showLayout(LAYOUT_PLAY)
        }

        checkAnswerButton.setOnClickListener {
            showLayout(LAYOUT_ANSWER)
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

    private fun showLayout(flag: Int) {
        when(flag){
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
                loadMusicUrl(getWholeUrl(pickSongs()))
            }
            LAYOUT_ANSWER -> {
                includePrepare.visibility = View.GONE
                includePlay.visibility = View.GONE
                includeAnswer.visibility = View.VISIBLE
                loadMusicUrl("about:blank")
            }
        }
    }

    private fun pickSongs(): List<String> {
        var index1: Int
        var index2: Int
        val pickedSongList = ArrayList<String>()
        do {
            index1 = (Math.random() * trackList.size).toInt()
            index2 = -1
            if (songCount == 2)
                index2 = (Math.random() * trackList.size).toInt()
        } while (index1 == index2)

        pickedSongList.add(trackList[index1].id)
        if (index2 != -1)
            pickedSongList.add(trackList[index2].id)

        return pickedSongList
    }

    private fun getWholeUrl(pickSongList: List<String>): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(urlGithub)
        pickSongList.forEach {
            stringBuilder.append("&song=")
            stringBuilder.append(it)
        }

        return stringBuilder.toString()
    }


}// Required empty public constructor
