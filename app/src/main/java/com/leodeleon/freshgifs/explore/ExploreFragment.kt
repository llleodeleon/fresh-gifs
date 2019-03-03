package com.leodeleon.freshgifs.explore


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.appcompat.queryTextChangeEvents
import com.leodeleon.domain.Giphy
import com.leodeleon.freshgifs.base.BaseFragment
import com.leodeleon.freshgifs.R
import com.leodeleon.freshgifs.databinding.FragmentExploreBinding
import com.leodeleon.freshgifs.utils.BindingAdapters
import com.leodeleon.freshgifs.utils.listen
import com.leodeleon.freshgifs.utils.logd
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_explore.*
import org.koin.android.viewmodel.ext.android.viewModel

class ExploreFragment : BaseFragment() {

    private val viewModel: ExploreViewModel by viewModel()

    private val scaleAnimation: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.bounce) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentExploreBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearch()
        setupRecycler()
        observe(viewModel.gifList) {
            viewModel.adapter.submitList(it)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun setupRecycler() {
        val carouselLayoutManager = CarouselLayoutManager(CarouselLayoutManager.VERTICAL)
        carouselLayoutManager.apply {
            setPostLayoutListener(CarouselZoomPostLayoutListener())
            addOnItemSelectionListener {
                if (CarouselLayoutManager.INVALID_POSITION != it) {
                    viewModel.adapter.getGif(it)?.let {
                        onCenterGif(it)
                        logd(it.toString())
                    }
                }
            }
        }

        recycler_view.apply {
            layoutManager = carouselLayoutManager
            setHasFixedSize(true)
            addOnScrollListener(CenterScrollListener())
        }
    }

    private fun setupSearch() {
        search_view.queryTextChangeEvents()
            .skipInitialValue()
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                val isEmpty = it.queryText.isEmpty()
                if (isEmpty) {
                    toolbar_title.text = getString(R.string.trending)
                }
                !isEmpty
            }
            .subscribe {
                if (it.isSubmitted) {
                    toolbar_title.text = it.queryText.toString()
                    search_view.clearFocus()
                }
            }
            .addTo(subscriptions)
    }

    private fun onCenterGif(gif: Giphy) {
        val source = gif.user?.let {
            if(it.username.isNotEmpty()) "@${it.username}" else ""
        }?: gif.source_tld
        val name = gif.user?.display_name ?: if(source.isNotEmpty()) getString(R.string.source) else ""
        val avatar_url = gif.user?.avatar_url?:""
        display_name.text = name
        display_name.isVisible = name.isNotEmpty()
        username.text = source
        username.isVisible = source.isNotEmpty()

        if(!button_favorite.isVisible){
            button_favorite.isVisible = true
        }
        picture.isVisible = source.isNotEmpty()

        if(avatar_url.isNotEmpty()){
            BindingAdapters.loadImage(picture,avatar_url)
        } else if (source.isNotEmpty()){
            Glide.with(picture).load(R.drawable.bg_source).into(picture)
        }

        button_favorite.setOnClickListener { view ->
            view.startAnimation(scaleAnimation)
            view.animation.listen {
                view.isSelected = !view.isSelected
            }

        }
    }

}
