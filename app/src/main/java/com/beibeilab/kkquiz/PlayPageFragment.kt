package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
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
        fun newInstance(trackList: List<Track>): PlayPageFragment {
            val fragment = PlayPageFragment()
            fragment.trackList = trackList
            return fragment
        }
    }

    lateinit var trackList: List<Track>
    val songCount = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_play_page, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebview()
        loadMusicUrl()
    }

    private fun setupWebview() {
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = WebChromeClient()
    }

    private fun loadMusicUrl() {
        webView.loadUrl(getWholeUrl(pickSongs()))
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
