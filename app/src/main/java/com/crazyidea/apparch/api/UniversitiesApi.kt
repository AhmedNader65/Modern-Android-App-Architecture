package com.crazyidea.apparch.api

import com.crazyidea.apparch.model.University
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UniversitiesApi {

    @GET("search")
    suspend fun fetchUniversities(
        @Query("country")  country:String
    ): Response<List<University>>

}