package com.beibeilab.kkquiz.result

/**
 * Created by david on 2018/1/23.
 */
interface ResultView {
    fun setupWebview()
    fun webViewLoadUrl(urlString: String)
    fun setupPlayAgainButton(foo: () -> Unit)
    fun setupKKBoxButton(foo: () -> Unit)
    fun setupArtistText(artistStr: String)
}