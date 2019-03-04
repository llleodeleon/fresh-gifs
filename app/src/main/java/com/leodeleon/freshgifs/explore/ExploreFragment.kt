package com.leodeleon.freshgifs.explore


import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.paging.PagedList
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.appcompat.queryTextChangeEvents
import com.leodeleon.domain.entities.Giphy
import com.leodeleon.freshgifs.R
import com.leodeleon.freshgifs.base.BaseFragment
import com.leodeleon.freshgifs.databinding.FragmentExploreBinding
import com.leodeleon.freshgifs.utils.BindingAdapters
import com.leodeleon.freshgifs.utils.getColorFromPosition
import com.leodeleon.freshgifs.utils.logd
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_explore.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.random.Random

class ExploreFragment : BaseFragment() {

    private val viewModel: ExploreViewModel by viewModel()
    private val adapter = ExploreAdapter()

    private val weakCallback = object : PagedList.Callback() {
        override fun onChanged(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}

        override fun onInserted(position: Int, count: Int) {
            if(position == 0 && adapter.itemCount > 0){
                adapter.getGif(0)?.let { onCenterGif(it) }
            }
        }
    }

    private val scaleAnimation: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.bounce) }
    private val carouselLayoutManager = CarouselLayoutManager(CarouselLayoutManager.VERTICAL)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentExploreBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearch()
        setupRecycler()
        setupButton()
        viewModel.loadFavorites()

        Observable.merge(viewModel.gifList, viewModel.searchGifList)
            .subscribe {
                it.addWeakCallback(it.snapshot(), weakCallback)
                adapter.submitList(it)
            }.addTo(subscriptions)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && adapter.itemCount > 0){
            viewModel.loadFavorites()
        }
    }

    private fun setupRecycler() {

        carouselLayoutManager.apply {
            setPostLayoutListener(CarouselZoomPostLayoutListener())
            addOnItemSelectionListener {
                    if (CarouselLayoutManager.INVALID_POSITION != it) {
                            onCenterGif(adapter.getGif(it))
                }
            }
        }

        recycler_view.apply {
            layoutManager = carouselLayoutManager
            adapter = this@ExploreFragment.adapter
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
                    viewModel.onClearSearch()
                    carouselLayoutManager.scrollToPosition(0)
                }
                !isEmpty
            }
            .subscribe {
                if (it.isSubmitted) {
                    val query = it.queryText.toString()
                    toolbar_title.text = query
                    search_view.clearFocus()
                    viewModel.onSearch(query)
                    carouselLayoutManager.scrollToPosition(0)
                }
            }
            .addTo(subscriptions)
    }

    private fun setupButton() {
        button_favorite.setOnClickListener { view ->
            view.startAnimation(scaleAnimation)
            val gif = adapter.getGif(carouselLayoutManager.centerItemPosition)!!
            viewModel.onClickGif(view, gif, !view.isSelected )
        }
    }

    private fun onCenterGif(gif: Giphy?) {
        gif?: return

        if(!button_favorite.isVisible){
            button_favorite.isVisible = true
        }
        val avatarUrl = gif.user?.avatar_url
        if(avatarUrl?.isNotEmpty() == true){
            val random = Random.nextInt(0,5)
            val placeholder = ColorDrawable(getColorFromPosition(random))
            BindingAdapters.loadImage(picture,avatarUrl,placeholder)
        } else {
            Glide.with(picture).load(R.drawable.bg_source).into(picture)
        }

        viewModel.onCenterGif(gif)
    }

}
