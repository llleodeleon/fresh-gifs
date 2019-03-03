package com.leodeleon.freshgifs.explore


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.appcompat.queryTextChangeEvents
import com.leodeleon.freshgifs.base.BaseFragment
import com.leodeleon.freshgifs.R
import com.leodeleon.freshgifs.databinding.FragmentExploreBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_explore.*
import org.koin.android.viewmodel.ext.android.viewModel

class ExploreFragment : BaseFragment() {

    private val viewModel: ExploreViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentExploreBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearch()

        observe(viewModel.gifList){
            viewModel.adapter.submitList(it)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun setupSearch(){
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
