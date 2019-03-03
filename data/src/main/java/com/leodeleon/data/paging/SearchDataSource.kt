package com.leodeleon.data.paging

import android.app.DownloadManager
import android.util.Log
import androidx.paging.PositionalDataSource
import com.leodeleon.data.ISchedulerProvider
import com.leodeleon.data.remote.GiphyService
import com.leodeleon.domain.entities.Giphy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class SearchDataSource(
    private val service: GiphyService,
    private val subscriptions: CompositeDisposable
) : PositionalDataSource<Giphy>() {

    var query: String = ""

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Giphy>) {
        if(query.isEmpty()) return
        service.getSearch(query,0).subscribeBy {
            callback.onResult(it.data,it.pagination.offset, it.pagination.total_count)
        }.addTo(subscriptions)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Giphy>) {
        if(query.isEmpty()) return
        service.getSearch(query, params.startPosition)
            .subscribeBy{
                callback.onResult(it.data)
            }.addTo(subscriptions)
    }
}