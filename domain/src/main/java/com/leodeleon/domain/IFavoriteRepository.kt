package com.leodeleon.domain

import com.leodeleon.domain.entities.Giphy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IFavoriteRepository {
    fun getFavorites(): Single<List<Giphy>>

    fun saveFavorite(gif: Giphy): Completable

    fun removeFavorite(gif: Giphy): Completable

}