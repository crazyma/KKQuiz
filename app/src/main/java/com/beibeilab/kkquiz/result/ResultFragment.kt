package com.beibeilab.kkquiz.result


import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.beibeilab.kkquiz.R
import com.beibeilab.kkquiz.model.Artist
import kotlinx.android.synthetic.main.fragment_result.*


/**
 * A simple [Fragment] subclass.
 */
class ResultFragment : Fragment(), ResultView {
    override fun setupWebview() {
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

    override fun webViewLoadUrl(urlString: String) {
        webView.loadUrl(urlString)
    }

    override fun setupPlayAgainButton(foo: () -> Unit) {
        buttonPlayAgain.setOnClickListener {
            activity.onBackPressed()
        }
    }

    override fun setupKKBoxButton(foo: () -> Unit) {
        buttonKKBox.setOnClickListener { foo() }
    }

    override fun setupArtistText(artistStr: String) {
        textArtist.text = artistStr
    }

    companion object {
        val DEEP_LINK = "https://event.kkbox.com/content/artist/"
        fun newInstance(artist: Artist): ResultFragment {
            val fragment = ResultFragment()
            fragment.artist = artist
            return fragment
        }
    }

    lateinit var resultPresenter: ResultPresenter
    lateinit var artist: Artist

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultPresenter = ResultPresenter(this, artist)
        resultPresenter.initialize()
    }

    override fun onResume() {
        super.onResume()
        resultPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        resultPresenter.onPause()
    }

    override fun onDestroy() {
        resultPresenter.onDestroy()
        super.onDestroy()
    }

}// Required empty public constructor
