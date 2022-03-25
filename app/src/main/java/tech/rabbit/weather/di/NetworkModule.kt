package tech.rabbit.weather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tech.rabbit.weather.BuildConfig
import tech.rabbit.weather.data.remote.RemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    fun provideMoshiFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor {
            val origin = it.request()
            val originHttpUrl = origin.url()
            val urlWithKey = originHttpUrl.newBuilder()
                .addQueryParameter("appid", BuildConfig.API_KEY)
                .build()

            val requestBuilder = origin.newBuilder().url(urlWithKey)
            it.proceed(requestBuilder.build())
        }
        return httpClient.build()
    }

    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideRemoteDataSource(retrofit: Retrofit) =
        RemoteDataSourceImpl(retrofit)
}