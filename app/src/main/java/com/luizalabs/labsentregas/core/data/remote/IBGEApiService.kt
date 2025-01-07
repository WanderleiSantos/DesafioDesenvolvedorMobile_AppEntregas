package com.luizalabs.labsentregas.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface IBGEApiService {
    @GET("api/v1/localidades/estados/{uf}/distritos")
    suspend fun getCities(@Path("uf") uf: String): List<CityResponse>
}