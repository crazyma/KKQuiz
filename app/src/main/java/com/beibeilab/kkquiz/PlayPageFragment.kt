package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.fragment_play_page.*


/**
 * A simple [Fragment] subclass.
 */
class PlayPageFragment : Fragment() {

    companion object {
        val urlGithub = "https://wubaibai.github.io/kkGame/?song=OseG-8qU8UtszwJlXm&song=4ql_l_98WUFosMGFiW&autoplay=true"
        fun newInstance(): PlayPageFragment{
            return PlayPageFragment()
        }
    }

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
        webView.loadUrl(urlGithub)
    }

}// Required empty public constructor
