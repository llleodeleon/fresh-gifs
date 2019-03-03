package com.leodeleon.data.paging

import android.app.DownloadManager
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.leodeleon.data.remote.GiphyService
import com.leodeleon.domain.entities.Giphy
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory(
    private val service: GiphyService,
    private val subscriptions: CompositeDisposable
): DataSource.Factory<Int, Giphy>() {

    var source: SearchDataSource? = null
    var query: String = ""

    val sourceLiveData = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, Giphy> {
        source = SearchDataSource(service, subscriptions)
        source?.query = query
        sourceLiveData.postValue(source)
        return source!!
    }
}