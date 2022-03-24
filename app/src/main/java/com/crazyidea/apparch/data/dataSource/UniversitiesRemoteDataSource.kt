package com.crazyidea.apparch.data.dataSource

import com.crazyidea.apparch.api.UniversitiesApi
import com.crazyidea.apparch.data.repository.Resource
import com.crazyidea.apparch.model.MessageResponse
import com.crazyidea.apparch.model.University
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class UniversitiesRemoteDataSource  @Inject constructor(
     private val universitiesApi: UniversitiesApi,
     private val retrofit: Retrofit
) {


//    suspend fun fetchUniversities(country: String):
//            Call<List<University>> =
//    // Move the execution to an IO-optimized thread since the ApiService
//        // doesn't support coroutines and makes synchronous requests.
//        withContext(ioDispatcher) {
//            universitiesApi.fetchUniversities(country)
//        }

    suspend fun fetchUniversities(country: String): Resource<List<University>>? {
        var result : Resource<List<University>>? = null
        val movieService = retrofit.create(UniversitiesApi::class.java);
        return getResponse(
            request = { movieService.fetchUniversities(country)},
            defaultErrorMessage = "Error fetching Universities list")
//        universitiesApi.fetchUniversities(country).enqueue(object :
//            Callback<List<University>> {
//            override fun onResponse(
//                call: Call<List<University>>,
//                response: Response<List<University>>
//            ) {
//                if (response.isSuccessful)
//                    result =  Resource.success(response.body())
//                else {
//                    val gson = Gson()
//                    val type = object : TypeToken<MessageResponse?>() {}.type
//                    val errorResponse: MessageResponse =
//                        gson.fromJson(response.errorBody()!!.charStream(), type)
//                    result =   Resource.error(errorResponse.message, null)
//                }
//            }
//
//            override fun onFailure(call: Call<List<University>>, t: Throwable) {
//                result =  Resource.error(t.message!!, null)
//            }
//        })
//        return result
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