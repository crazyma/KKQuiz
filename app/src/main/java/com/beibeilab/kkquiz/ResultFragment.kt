package com.beibeilab.kkquiz


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beibeilab.kkquiz.Utils.FragmentUtils
import kotlinx.android.synthetic.main.fragment_result.*


/**
 * A simple [Fragment] subclass.
 */
class ResultFragment : Fragment() {

    companion object {
        fun newInstance(artist: String): ResultFragment {
            val fragment = ResultFragment()
            fragment.artist = artist
            return fragment
        }
    }

    lateinit var artist: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textArtist.text = artist
        buttonPlayAgain.setOnClickListener {
            this@ResultFragment.activity.onBackPressed()
        }
    }

}// Required empty public constructor
