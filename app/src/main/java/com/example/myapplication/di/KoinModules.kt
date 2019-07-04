package com.example.myapplication.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.database.MyAppDatabase
import com.example.myapplication.database.dao.BlogDao
import com.example.myapplication.service.BlogService.BlogLocalService
import com.example.myapplication.service.BlogService.BlogRemoteService
import com.example.myapplication.service.BlogService.BlogStore
import com.example.myapplication.service.BlogService.api.BlogRemoteRaw
import com.example.nowsudm_jirani.service.NetworkServiceN.NetworkService
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object KoinModules {

    const val SERVER_URL = "http://10.0.2.2:8989/"

    val appModule = module(override = true) {
        single<MyAppDatabase> {
            Room.databaseBuilder(androidApplication(), MyAppDatabase::class.java, "MJiraniRoomDB")
                .fallbackToDestructiveMigration()
                .build()
        }
        //DATABASE ACCESS OBJECTS
        factory<BlogDao> { get<MyAppDatabase>().blogDao() }
        factory<BlogLocalService> { BlogLocalService(get()) }
        factory<BlogRemoteService> { BlogRemoteService(get()) }
        factory<BlogStore> { BlogStore(get(), get()) }

        factory { createOkHttpClient(androidApplication()) }

        factory<BlogRemoteRaw> { createWebService<BlogRemoteRaw>(get(), SERVER_URL) }
    }

    /*
    We create different OkHttpClients depending on whether we have a user token
    -the first time a person has the app, they don't have an access token yet, so we create
    a http client that can still work.
    -after they login, the sharedPreference userToken field has the user token which
    we can add with every request to ensure we have access.
     */
    fun createOkHttpClient(context: Context): OkHttpClient {
        //we check the sharedPreferences to see if the user has a token saved.
        //if it's not empty we use the contents to add a header with every request.
        var sharedPref = context.getSharedPreferences("userTokenFile", Context.MODE_PRIVATE)
        var accessToken = sharedPref.getString("userToken", "")
        Log.d("ok http token", accessToken)
        if (accessToken!!.isEmpty()) {
            Log.d("ok http branch", "token is empty")
            return OkHttpClient
                .Builder()
                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                        val request: Request = chain.request()
                        val newRequest: Request.Builder =
                            request.newBuilder()
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                        return chain.proceed(newRequest.build())
                    }
                })
                .build()

        } else {
            /*
            We can enable caching so that data that was fetched 30 seconds ago doesn't need to
            be fetched again. Once data is more than 30 seconds old and someone revisits a  page
            that needs data from the net we
            cache requests that are 30 seconds old - don't need to hit the server even though we
            have an internet connection
             */
            Log.d("ok http branch", "token is NOT empty")
            val cacheSize = (5 * 1024 * 1024).toLong()
            val cacheForNet = Cache(context.cacheDir, cacheSize)

            return OkHttpClient.Builder()
                .cache(cacheForNet)
                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                        val request: Request = chain.request()
                        val newRequest: Request.Builder =
                            request.newBuilder()
                                .header("Authorization", "Bearer " + accessToken)
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")


                        if (NetworkService.checkIfHasNetwork(context)) {
                            newRequest.addHeader("Cache-Control", "public, max-age=" + 30)
                        } else {
                            newRequest.addHeader(
                                "Cache-Control",
                                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                            )
                        }
                        return chain.proceed(newRequest.build())
                    }
                }).build()
        }
    }


    inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        return retrofit.create(T::class.java)
    }
}