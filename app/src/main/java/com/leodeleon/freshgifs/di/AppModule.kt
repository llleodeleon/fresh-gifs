package com.leodeleon.freshgifs.di

import com.leodeleon.data.remote.GiphyApi
import com.leodeleon.freshgifs.explore.ExploreViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    viewModel { ExploreViewModel(get()) }
    single { GiphyApi() }
}