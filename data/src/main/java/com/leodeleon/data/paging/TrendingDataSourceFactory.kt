package com.leodeleon.data.paging

import androidx.paging.DataSource
import com.jakewharton.rxrelay2.PublishRelay
import com.leodeleon.data.DataState
import com.leodeleon.data.remote.GiphyService
import com.leodeleon.domain.entities.Giphy
import io.reactivex.disposables.CompositeDisposable

class TrendingDataSourceFactory(
    private val service: GiphyService,
    private val state: PublishRelay<DataState>,
    private val subscriptions: CompositeDisposable
): DataSource.Factory<Int, Giphy>() {

    lateinit var source: TrendingDataSource
    private set

    override fun create(): DataSource<Int, Giphy> {
        source = TrendingDataSource(service, state, subscriptions)
        return source
    }
}