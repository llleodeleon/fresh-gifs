package com.leodeleon.freshgifs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding3.appcompat.queryTextChangeEvents
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_explore.*
import java.util.concurrent.TimeUnit

class ExploreFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_view.queryTextChangeEvents()
            .skipInitialValue()
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                val isEmpty = it.queryText.isEmpty()
                if(isEmpty){
                    toolbar_title.text = getString(R.string.trending)
                }

                !isEmpty
            }
            .subscribe {
                if(it.isSubmitted){
                    toolbar_title.text = it.queryText.toString()
                    search_view.clearFocus()
                }
            }
            .addTo(subscriptions)


    }

}
