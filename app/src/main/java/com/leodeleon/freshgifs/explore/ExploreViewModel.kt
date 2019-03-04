package com.leodeleon.freshgifs.explore

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.jakewharton.rxrelay2.PublishRelay
import com.leodeleon.data.DataState
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
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class ExploreViewModel(api: GiphyApi, private val repository: IFavoriteRepository, private val schedulers: ISchedulerProvider) : BaseViewModel() {

    private val stateRelay: PublishRelay<DataState> = PublishRelay.create()
    private val trendingFactory = TrendingDataSourceFactory(api.service, stateRelay, subscriptions)
    private val searchFactory = SearchDataSourceFactory(api.service, stateRelay, subscriptions)

    val gifList: Observable<PagedList<Giphy>>
    val searchGifList: Observable<PagedList<Giphy>>
    val isFavorite = ObservableBoolean()
    val showPicture = ObservableBoolean()

    val showName = ObservableBoolean()
    val showSource = ObservableBoolean()
    val name = ObservableField<String>()
    val source = ObservableField<String>()

    val showEmpty = ObservableBoolean()
    val showError = ObservableBoolean()
    val showSuccess = ObservableBoolean()
    val showLoading = ObservableBoolean()
    var currentGif: Giphy? = null

    private var favorites: MutableList<Giphy> = mutableListOf()
    private var hasSearched = false

    init {
        gifList = RxPagedListBuilder<Int,Giphy>(trendingFactory, 25).buildObservable()
        searchGifList = RxPagedListBuilder<Int,Giphy>(searchFactory, 25).buildObservable()
        observeState()
    }

    fun loadFavorites(){
        repository.getFavorites()
            .observeOn(schedulers.main())
            .subscribeBy{
                favorites.clear()
                favorites.addAll(it)
                isFavorite.set(favorites.any { it.id == currentGif?.id })
            }
            .addTo(subscriptions)
    }

    fun onCenterGif(gif: Giphy){
        currentGif = gif
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
                    if(!favorites.any { it.id == gif.id }){
                        favorites.add(gif)
                    }
                } else {
                   favorites.find { it.id == gif.id }?.let {
                       favorites.remove(it)
                   }
                }
                this.isFavorite.set(isFavorite)
            })
            .addTo(subscriptions)
    }

    fun onClearSearch() {
        searchFactory.query = ""
        if(hasSearched){
            trendingFactory.source.invalidate()
            hasSearched = false
        }
    }

    fun onSearch(query: String){
        resetFields()
        searchFactory.query = query
        searchFactory.source.invalidate()
        hasSearched = true
    }

    private fun observeState(){
        stateRelay.subscribe {
            showLoading.set(it == DataState.LOADING)
            showError.set( it == DataState.ERROR)
            showSuccess.set( it == DataState.SUCCESS)
            showEmpty.set( it == DataState.EMPTY)
        }.addTo(subscriptions)
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