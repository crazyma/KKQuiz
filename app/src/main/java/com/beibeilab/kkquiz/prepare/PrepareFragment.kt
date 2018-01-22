package com.beibeilab.kkquiz.prepare


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import com.beibeilab.kkquiz.R
import com.beibeilab.kkquiz.model.Artist
import com.beibeilab.kkquiz.model.Track
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_prepare.*
import com.beibeilab.kkquiz.base.DisposableFragment


/**
 * A simple [Fragment] subclass.
 */
class PrepareFragment : DisposableFragment(),
        PrepareView, PrepareInteractor.PrepareInteractorListener {

    interface PrepareFragmentListener {
        fun onPreparationFinished(artist: Artist, list: List<Track>)
    }

    companion object {
        fun newInstance(artist: Artist): PrepareFragment {
            val fragment = PrepareFragment()
            fragment.artist = artist
            return fragment
        }

        val LOOP_COUNT: Long = 5
    }

    lateinit var preparePresenter: PreparePresenter
    lateinit var prepareInteractor: PrepareInteractor
    var prepareFragmentListener: PrepareFragmentListener? = null

    private lateinit var artist: Artist
    private lateinit var trackList: List<Track>

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity is PrepareFragmentListener) {
            prepareFragmentListener = activity as PrepareFragmentListener
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_prepare, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareInteractor = PrepareInteractor(activity, this)
        preparePresenter = PreparePresenter(this, prepareInteractor, artist)

    }

    override fun onResume() {
        super.onResume()
        preparePresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        preparePresenter.onPause()
    }

    override fun onDestroy() {
        preparePresenter.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        prepareFragmentListener = null
        super.onDetach()
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
        textProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
        textProgress.visibility = View.GONE
    }

    override fun showErrorToast() {
        Toast.makeText(this@PrepareFragment.context, "出現錯誤", Toast.LENGTH_LONG).show()
    }

    override fun setArtistName(name: String) {
        textArtist.text = name
    }

    override fun setArtistImage(urlString: String) {
        Picasso.with(context)
                .load(urlString)
                .into(imageArtist)
    }

    override fun enablePlayButton(foo: () -> Unit) {
        buttonStart.setOnClickListener {
            foo()
        }
    }

    override fun disablePlayButton() {
        buttonStart.setOnClickListener(null)
    }

    override fun startRippleAnim() {
        val cx = (buttonStart.x + buttonStart.width / 2).toInt()
        val cy = (buttonStart.y + buttonStart.height / 2).toInt()
        val finalRadius = Math.max(rippleView.width, rippleView.height)
        val anim = ViewAnimationUtils.createCircularReveal(rippleView, cx, cy, 0f, finalRadius.toFloat())
        rippleView.visibility = View.VISIBLE
        anim.duration = 500
        anim.interpolator = AccelerateInterpolator()
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(p0: Animator?) {
                //  跳轉
                prepareFragmentListener!!.onPreparationFinished(artist, trackList)
            }
        })
        anim.start()
    }

    override fun onLoadTrackListFinished(list: List<Track>) {
        trackList = list
    }


}// Required empty public constructor
