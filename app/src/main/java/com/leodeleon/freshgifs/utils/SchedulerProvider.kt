package com.leodeleon.freshgifs.utils

import com.leodeleon.data.ISchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider: ISchedulerProvider {
    override fun main() = AndroidSchedulers.mainThread()

    override fun io() = Schedulers.io()
}