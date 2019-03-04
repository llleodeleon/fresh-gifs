package com.leodeleon.freshgifs.favorites

import androidx.databinding.ObservableBoolean
import com.leodeleon.data.ISchedulerProvider
import com.leodeleon.domain.IFavoriteRepository
import com.leodeleon.domain.entities.Giphy
import com.leodeleon.freshgifs.BR
import com.leodeleon.freshgifs.R
import com.leodeleon.freshgifs.base.BaseViewModel
import com.leodeleon.freshgifs.utils.getColorFromPosition
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList

class FavoritesViewModel(private val repository: IFavoriteRepository, private val schedulers: ISchedulerProvider): BaseViewModel() {

    val favorites = DiffObservableList<FaveItemViewModel>(object: DiffObservableList.Callback<FaveItemViewModel>{
        override fun areContentsTheSame(oldItem: FaveItemViewModel, newItem: FaveItemViewModel) = true

        override fun areItemsTheSame(oldItem: FaveItemViewModel, newItem: FaveItemViewModel): Boolean {
            return oldItem.item.id == newItem.item.id
        }
    })

    val itemBinding : ItemBinding<FaveItemViewModel> = ItemBinding.of(BR.viewModel, R.layout.item_gif_grid)
    val isEmpty = ObservableBoolean()

    fun loadData(){
        repository.getFavorites()
            .observeOn(schedulers.main())
            .subscribeBy { faves ->
                isEmpty.set(faves.isEmpty())
                val items = faves.mapIndexed { index: Int, giphy: Giphy ->
                    FaveItemViewModel(giphy, getColorFromPosition(index), ::onDelete) }
                favorites.update(items)
            }.addTo(subscriptions)
    }

    private fun onDelete(gif: Giphy){
        repository.removeFavorite(gif)
            .observeOn(schedulers.main())
            .subscribe {
                val newItems = favorites.filter { it.item.id != gif.id }
                isEmpty.set(newItems.isEmpty())
                favorites.update(newItems)
            }
            .addTo(subscriptions)
    }
}