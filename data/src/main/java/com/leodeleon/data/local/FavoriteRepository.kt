package com.leodeleon.data.local

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.leodeleon.data.ISchedulerProvider
import com.leodeleon.data.entities.GiphyEntity
import com.leodeleon.domain.IFavoriteRepository
import com.leodeleon.domain.entities.Giphy
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class FavoriteRepository(
    private val rxPrefs: RxSharedPreferences,
    private val sharedPrefs: SharedPreferences,
    private val schedulers: ISchedulerProvider
): IFavoriteRepository {

    private companion object {
        const val FAVORITES = "FAVORITES"
    }

    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, GiphyEntity::class.java)
    private val adapter: JsonAdapter<List<GiphyEntity>> = moshi.adapter(type)

    private val converter = object : Preference.Converter<List<GiphyEntity>> {
        override fun deserialize(serialized: String): List<GiphyEntity> {
            return adapter.fromJson(serialized)!!
        }

        override fun serialize(value: List<GiphyEntity>): String {
            return adapter.toJson(value)
        }
    }

    override fun getFavorites(): Single<List<Giphy>> {
        return rxPrefs.getObject(FAVORITES, emptyList(), converter)
            .asObservable()
            .firstOrError()
            .map {
                it.map { it.unwrap() }
            }
            .subscribeOn(schedulers.io())
    }

    override fun saveFavorite(gif: Giphy): Completable {
        return getFavorites().flatMapCompletable {
            val newList = it.map { GiphyEntity(it) } + GiphyEntity(gif)
            save(converter.serialize(newList))
            Completable.complete()
        }.subscribeOn(schedulers.io())
    }

    override fun removeFavorite(gif: Giphy): Completable {
        return getFavorites().flatMapCompletable {
            val newList = it.filter { it.id != gif.id }.map { GiphyEntity(it) }
            save(converter.serialize(newList))
            Completable.complete()
        }.subscribeOn(schedulers.io())
    }

    private fun save(value: String){
        sharedPrefs.edit().putString(FAVORITES,value).apply()
    }
}