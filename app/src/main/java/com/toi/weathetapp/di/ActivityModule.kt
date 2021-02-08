package com.toi.weathetapp.di

import com.toi.weathetapp.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributesMainActivity(): MainActivity

}