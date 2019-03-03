package com.leodeleon.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.leodeleon.data.remote.GiphyService
import com.leodeleon.domain.entities.Giphy
import io.reactivex.disposables.CompositeDisposable

class TrendingDataSourceFactory(
    private val service: GiphyService,
    private val subscriptions: CompositeDisposable
): DataSource.Factory<Int, Giphy>() {

    val sourceLiveData = MutableLiveData<TrendingDataSource>()

    override fun create(): DataSource<Int, Giphy> {
        val source = TrendingDataSource(service, subscriptions)
        sourceLiveData.postValue(source)
        return source
    }
}