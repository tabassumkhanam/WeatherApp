package com.toi.weathetapp.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelModule {

//    @Binds
//    @IntoMap
//    @ViewModelKey(RepositoryViewModel::class)
//    internal abstract fun bindTrendingRepositoryViewModel(listViewModel: RepositoryViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}