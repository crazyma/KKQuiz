package com.beibeilab.kkquiz.prepare

/**
 * Created by david on 2018/1/19.
 */
interface PrepareView {

    fun showProgress()
    fun hideProgress()
    fun showErrorToast()
    fun setArtistName(name: String)
    fun setArtistImage(urlString: String)
    fun enablePlayButton(foo: () -> Unit)
    fun disablePlayButton()
    fun startRippleAnim()   //  TODO: 這裡應該要放一個 Listener 當作 parameter
}