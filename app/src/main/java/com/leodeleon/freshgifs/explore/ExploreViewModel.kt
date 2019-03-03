package com.leodeleon.freshgifs.explore

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.leodeleon.data.ISchedulerProvider
import com.leodeleon.data.paging.SearchDataSourceFactory
import com.leodeleon.data.paging.TrendingDataSourceFactory
import com.leodeleon.data.remote.GiphyApi
import com.leodeleon.domain.IFavoriteRepository
import com.leodeleon.domain.entities.Giphy
import com.leodeleon.freshgifs.R
import com.leodeleon.freshgifs.base.BaseViewModel
import com.leodeleon.freshgifs.utils.getString
import com.leodeleon.freshgifs.utils.toast
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class ExploreViewModel(api: GiphyApi, private val repository: IFavoriteRepository, private val schedulers: ISchedulerProvider) : BaseViewModel() {

    private val trendingFactory = TrendingDataSourceFactory(api.service, subscriptions)
    private val searchFactory = SearchDataSourceFactory(api.service, subscriptions)
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(25)
        .setInitialLoadSizeHint(25)
        .build()


    val trendingList: LiveData<PagedList<Giphy>>
    val searchList: LiveData<PagedList<Giphy>>
    val isFavorite = ObservableBoolean()
    val showPicture = ObservableBoolean()
    val showName = ObservableBoolean()
    val showSource = ObservableBoolean()
    val name = ObservableField<String>()
    val source = ObservableField<String>()
    private var favorites: MutableList<Giphy> = mutableListOf()
    private var previousList: PagedList<Giphy>? = null
    private var hasSearched = false

    init {
        trendingList = LivePagedListBuilder<Int, Giphy>(trendingFactory,config).build()
        searchList = LivePagedListBuilder<Int,Giphy>(searchFactory,config).build()
    }

    fun loadFavorites(){
        repository.getFavorites()
            .observeOn(schedulers.main())
            .subscribeBy{
                favorites.addAll(it)
            }
            .addTo(subscriptions)
    }

    fun onCenterGif(gif: Giphy){
        val sourceName = gif.user?.let {
            if(it.username.isNotEmpty()) "@${it.username}" else ""
        }?: gif.source_tld
        val displayName = gif.user?.display_name ?: if(sourceName.isNotEmpty()) getString(R.string.source) else ""
        val avatarUrl = gif.user?.avatar_url?:""
        showPicture.set(avatarUrl.isNotEmpty() || sourceName.isNotEmpty())
        isFavorite.set(favorites.any { it.id == gif.id })
        name.set(displayName)
        source.set(sourceName)
        showName.set(displayName.isNotEmpty())
        showSource.set(sourceName.isNotEmpty())
    }

    fun onClickGif(view: View, gif: Giphy, isFavorite: Boolean){
       val call = if(isFavorite){
            repository.saveFavorite(gif)
        } else {
            repository.removeFavorite(gif)
        }
        call.observeOn(schedulers.main())
            .subscribeBy({
                view.context.toast(R.string.snack_error)
            },{
                if(isFavorite){
                    view.context.toast(R.string.snack_saved)
                    if(!favorites.any { it.id == gif.id }){
                        favorites.add(gif)
                    }
                } else {
                   favorites.find { it.id == gif.id }?.let {
                       favorites.remove(it)
                   }
                    view.context.toast(R.string.snack_removed)
                }
                this.isFavorite.set(isFavorite)
            })
            .addTo(subscriptions)
    }

    fun onClearSearch() {
        searchFactory.query = ""
        if(hasSearched){
            trendingFactory.sourceLiveData.value?.invalidate()
            hasSearched = false
        }
    }

    fun onSearch(query: String){
        resetFields()
        previousList = trendingList.value
        searchFactory.query = query
        searchFactory.sourceLiveData.value?.invalidate()
        hasSearched = true
    }

    private fun resetFields(){
        showPicture.set(false)
        isFavorite.set(false)
        name.set(null)
        source.set(null)
        showName.set(false)
        showSource.set(false)
    }
}