package com.beibeilab.kkquiz.playpage

/**
 * Created by david on 2018/1/20.
 */
interface PlayPageView {
    fun setupWebview()
    fun loadUrlInWebview(urlString: String)
    fun showPlayLayout()
    fun showAnswerLayout()
    fun setCountText(index: Int)
    fun setTrackText(name: String)
    fun setAlbumImage(urlString: String)
    fun setAlbumText(name: String)
    fun startYoyoAnim()
    fun stopYoyoAnim()
    fun setCheckButtonClickListener(foo: () -> Unit)
    fun setNextButtonClickListener(foo: () -> Unit)

}