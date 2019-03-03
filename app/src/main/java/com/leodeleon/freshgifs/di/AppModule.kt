package com.leodeleon.freshgifs.di

import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.leodeleon.data.ISchedulerProvider
import com.leodeleon.data.local.FavoriteRepository
import com.leodeleon.data.remote.GiphyApi
import com.leodeleon.domain.IFavoriteRepository
import com.leodeleon.freshgifs.explore.ExploreViewModel
import com.leodeleon.freshgifs.favorites.FavoritesViewModel
import com.leodeleon.freshgifs.utils.SchedulerProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    viewModel { ExploreViewModel(get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
    single { GiphyApi() }
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    single { RxSharedPreferences.create(get()) }
    single { FavoriteRepository(get(), get(), get()) as IFavoriteRepository }
    single { SchedulerProvider() as ISchedulerProvider }
}