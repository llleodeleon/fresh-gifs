package com.leodeleon.data.paging

import android.util.Log
import androidx.paging.PositionalDataSource
import com.jakewharton.rxrelay2.PublishRelay
import com.leodeleon.data.DataState
import com.leodeleon.data.ISchedulerProvider
import com.leodeleon.data.remote.GiphyService
import com.leodeleon.domain.entities.Giphy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class TrendingDataSource(
    private val service: GiphyService,
    private val state: PublishRelay<DataState>,
    private val subscriptions: CompositeDisposable
) : PositionalDataSource<Giphy>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Giphy>) {
        service.getTrending(0)
            .doOnSubscribe {
                state.accept(DataState.LOADING)
            }
            .subscribeBy({
                state.accept(DataState.ERROR)
            },{
                state.accept(DataState.SUCCESS)
                callback.onResult(it.data,it.pagination.offset, it.pagination.total_count)
            }).addTo(subscriptions)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Giphy>) {
        service.getTrending(params.startPosition)
            .subscribeBy({
               Log.e(javaClass.simpleName, it.message)
            },{
                callback.onResult(it.data)
            }).addTo(subscriptions)
    }
}