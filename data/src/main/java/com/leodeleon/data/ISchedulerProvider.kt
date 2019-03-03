package com.leodeleon.data

import io.reactivex.Scheduler

interface ISchedulerProvider {
    fun main(): Scheduler
    fun io(): Scheduler
}