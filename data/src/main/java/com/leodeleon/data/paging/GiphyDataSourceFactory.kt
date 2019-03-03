package com.leodeleon.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.leodeleon.data.remote.GiphyService
import com.leodeleon.domain.Giphy
import io.reactivex.disposables.CompositeDisposable

class GiphyDataSourceFactory(
    private val service: GiphyService,
    private val subscriptions: CompositeDisposable
): DataSource.Factory<Int,Giphy>() {

    private val sourceLiveData = MutableLiveData<GiphyDataSource>()

    override fun create(): DataSource<Int, Giphy> {
        val source = GiphyDataSource(service, subscriptions)
        sourceLiveData.postValue(source)
        return source
    }
}