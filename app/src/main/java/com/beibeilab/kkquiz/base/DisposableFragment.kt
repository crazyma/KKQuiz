package com.beibeilab.kkquiz.base

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

/**
 * A simple [Fragment] subclass.
 */
open class DisposableFragment : Fragment() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }


}// Required empty public constructor
