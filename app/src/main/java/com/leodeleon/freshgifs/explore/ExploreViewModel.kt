package com.leodeleon.freshgifs.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.leodeleon.data.paging.GiphyDataSourceFactory
import com.leodeleon.data.remote.GiphyApi
import com.leodeleon.data.remote.GiphyService
import com.leodeleon.domain.Giphy
import com.leodeleon.freshgifs.base.BaseViewModel
import io.reactivex.disposables.CompositeDisposable

class ExploreViewModel(api: GiphyApi) : BaseViewModel() {

    private val factory: GiphyDataSourceFactory = GiphyDataSourceFactory(api.service, subscriptions)

    val gifList: LiveData<PagedList<Giphy>>


    val adapter = ExploreAdapter()

    init {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(25)
            .setInitialLoadSizeHint(25)
            .build()

        gifList = LivePagedListBuilder<Int, Giphy>(factory,config).build()

    }
}