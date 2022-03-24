package com.crazyidea.apparch.api

import com.crazyidea.apparch.model.University
import retrofit2.Call
import retrofit2.Response

interface ApiHelper {
   suspend fun fetchUniversities(country: String) : Response<List<University>>

}