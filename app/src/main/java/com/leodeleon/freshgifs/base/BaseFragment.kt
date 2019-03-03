package com.leodeleon.freshgifs.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment: Fragment() {

    protected val subscriptions = CompositeDisposable()

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
    }

    inline fun <reified T> Fragment.observe(data: LiveData<T>, crossinline onValue: (T) -> Unit) {
        data.observe(viewLifecycleOwner, Observer { onValue(it) })
    }

}