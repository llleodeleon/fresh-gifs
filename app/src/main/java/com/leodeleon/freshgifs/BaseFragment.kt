package com.leodeleon.freshgifs

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment: Fragment() {

    protected val subscriptions = CompositeDisposable()


    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
    }
}