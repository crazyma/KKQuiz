package com.beibeilab.kkquiz.search

import com.beibeilab.kkquiz.model.Artist

/**
 * Created by david on 2018/1/19.
 */
interface SearchView {
    fun enableButton()
    fun disableButton()
    fun showProgressBar()
    fun hideProgressBar()
    fun getEditTextString(): String?
    fun showErrorToast()
    fun onSearchFinished(artist: Artist)
}