package com.uaemex.rr.api.client

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FabricRRWebClient {

    static final String API_BASE_URL = "https://api.github.com"

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()

    static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())

    static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build()
        retrofit.create(serviceClass)
    }

}

