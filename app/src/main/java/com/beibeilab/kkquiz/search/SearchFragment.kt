package com.beibeilab.kkquiz.search


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.beibeilab.kkquiz.R
import com.beibeilab.kkquiz.base.DisposableFragment
import com.beibeilab.kkquiz.model.Artist
import kotlinx.android.synthetic.main.content_search.*


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : DisposableFragment(),  SearchView{


    interface SearchFragmentListener {
        fun onSearchFinished(artist: Artist)
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    lateinit var searchPresenter: SearchPresenter
    var searchFragmentListener: SearchFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(activity is SearchFragmentListener){
            searchFragmentListener = activity as SearchFragmentListener
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchInteractor = SearchInteractor(context)
        searchPresenter = SearchPresenter(this, searchInteractor)

        searchButton.setOnClickListener {
            searchPresenter.onSearchButtonClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        searchPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        searchPresenter.onPause()
    }

    override fun onDestroy() {
        searchPresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        searchFragmentListener = null
        super.onDetach()
    }

    override fun onSearchFinished(artist: Artist) {
        searchFragmentListener!!.onSearchFinished(artist)
    }

    override fun showErrorToast() {
        Toast.makeText(this@SearchFragment.context, "找不到結果", Toast.LENGTH_LONG).show()
    }

    override fun getEditTextString(): String? {
        return editText.text.toString().trim()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun enableButton() {
        searchButton.isClickable = true
    }

    override fun disableButton() {
        searchButton.isClickable = false
    }

}// Required empty public constructor
