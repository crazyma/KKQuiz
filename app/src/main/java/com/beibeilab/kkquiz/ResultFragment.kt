package com.beibeilab.kkquiz


import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.beibeilab.kkquiz.Utils.FragmentUtils
import com.beibeilab.kkquiz.model.Artist
import kotlinx.android.synthetic.main.fragment_result.*


/**
 * A simple [Fragment] subclass.
 */
class ResultFragment : Fragment() {

    companion object {
        val DEEP_LINK = "https://event.kkbox.com/content/artist/"
        fun newInstance(artist: Artist): ResultFragment {
            val fragment = ResultFragment()
            fragment.artist = artist
            return fragment
        }
    }

    lateinit var artist: Artist

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textArtist.text = artist.name

        setupWebView()
        setupButtonEvent()
    }

    private fun setupButtonEvent() {
        buttonPlayAgain.setOnClickListener {
            this@ResultFragment.activity.onBackPressed()
        }
        buttonKKBox.setOnClickListener {
//            val uri = Uri.parse(DEEP_LINK + artist.id)
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
            Log.d("crazyma","url url url " + (DEEP_LINK + artist.id))
            webView.loadUrl(DEEP_LINK + artist.id)
        }
    }

    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (url!!.startsWith("intent://")) {
                    try {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        startActivity(intent)
                    } catch (error: ActivityNotFoundException) {
                    }
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                Log.i("crazyma","@@@@@@@@@@@@@@@@@@@@@@@@@  $url")
                if (url.startsWith("intent://")) {
                    try {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        startActivity(intent)
                    } catch (error: ActivityNotFoundException) {
                        return false
                    }
                    return true
                }
                return false
            }
        }
    }

}// Required empty public constructor
