package tech.rabbit.weather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import tech.rabbit.weather.data.WeatherRepositoryImpl
import tech.rabbit.weather.data.local.PreferenceDataSourceImpl
import tech.rabbit.weather.data.remote.RemoteDataSourceImpl


@Module
@InstallIn(ViewModelComponent::class)
internal object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideRepositoryImpl(remoteDataSource: RemoteDataSourceImpl, preferenceDataSourceImpl: PreferenceDataSourceImpl) =
        WeatherRepositoryImpl(remoteDataSource, preferenceDataSourceImpl)
}