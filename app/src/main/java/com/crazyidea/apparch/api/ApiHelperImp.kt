package com.crazyidea.apparch.api

import com.crazyidea.apparch.model.University
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImp @Inject constructor(private val universitiesApi: UniversitiesApi) : ApiHelper {
    override suspend fun fetchUniversities(country: String): Response<List<University>> =
        universitiesApi.fetchUniversities(country)
}