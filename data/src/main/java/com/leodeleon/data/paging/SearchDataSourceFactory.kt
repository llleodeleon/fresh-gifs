package com.leodeleon.data.paging

import android.app.DownloadManager
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jakewharton.rxrelay2.PublishRelay
import com.leodeleon.data.DataState
import com.leodeleon.data.remote.GiphyService
import com.leodeleon.domain.entities.Giphy
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory(
    private val service: GiphyService,
    private val state: PublishRelay<DataState>,
    private val subscriptions: CompositeDisposable
): DataSource.Factory<Int, Giphy>() {

    lateinit var source: SearchDataSource
    private set
    var query: String = ""

    override fun create(): DataSource<Int, Giphy> {
        source = SearchDataSource(service, state, subscriptions)
        source.query = query
        return source
    }
}