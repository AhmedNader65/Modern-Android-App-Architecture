package com.crazyidea.apparch.data.dataSource

import com.crazyidea.apparch.api.UniversitiesApi
import com.crazyidea.apparch.model.Resource
import com.crazyidea.apparch.model.MessageResponse
import com.crazyidea.apparch.model.University
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class UniversitiesRemoteDataSource  @Inject constructor(
     private val universitiesApi: UniversitiesApi,
) {

    suspend fun fetchUniversities(country: String): Resource<List<University>>? {
        return getResponse(
            request = { universitiesApi.fetchUniversities(country)},
            defaultErrorMessage = "Error fetching Universities list")
    }
    private suspend fun <T> getResponse(request: suspend () ->  Response<T>, defaultErrorMessage: String): Resource<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Resource.success(result.body())
            } else {
                val gson = Gson()
                val type = object : TypeToken<MessageResponse?>() {}.type
                val errorResponse: MessageResponse =
                    gson.fromJson(result.errorBody()!!.charStream(), type)
                Resource.error(errorResponse.message, null)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            Resource.error("Unknown Error", null)
        }
    }
}