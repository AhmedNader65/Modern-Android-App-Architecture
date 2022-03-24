package com.crazyidea.apparch.data.repository

import com.crazyidea.apparch.data.dataSource.UniversitiesRemoteDataSource
import com.crazyidea.apparch.model.MessageResponse
import com.crazyidea.apparch.model.University
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UniversityRepository @Inject constructor(
    private val universitiesRemoteDataSource: UniversitiesRemoteDataSource,
) {

    // Mutex to make writes to cached values thread-safe.
    private val latestUniversitiesMutex = Mutex()

    // Cache of the latest universities got from the network.
    private var latestUniversities: Resource<List<University>>? = null
    suspend fun fetchUniversities(
        refresh: Boolean = false,
        country: String
    ): Flow<Resource<List<University>>?> {
        return flow {
            emit(fetchUniversitiesCached())
            if (refresh) {
                emit(Resource.loading())
                val result = universitiesRemoteDataSource.fetchUniversities(country)
                if (result?.status == Status.SUCCESS) {
                    result.let {
                        latestUniversitiesMutex.withLock {
                            latestUniversities = it
                        }
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
//        universitiesRemoteDataSource.fetchUniversities(country).enqueue(object :
//            Callback<List<University>> {
//            override fun onResponse(
//                call: Call<List<University>>,
//                response: Response<List<University>>
//            ) {
//                if (response.isSuccessful) {
////                        latestUniversitiesMutex.withLock {
////                            latestUniversities = response.body()!!
////                        }
//                    Resource.success(response.body()!!)
//                } else {
//                    val gson = Gson()
//                    val type = object : TypeToken<MessageResponse?>() {}.type
//                    val errorResponse: MessageResponse =
//                        gson.fromJson(response.errorBody()!!.charStream(), type)
//                    Resource.error(errorResponse.message, null)
//                }
//            }
//
//            override fun onFailure(call: Call<List<University>>, t: Throwable) {
//                Resource.error(t.message!!, null)
//            }
//        })
    }

    private suspend fun fetchUniversitiesCached(): Resource<List<University>>? {
        return latestUniversitiesMutex.withLock {
            this.latestUniversities
        }
    }


}