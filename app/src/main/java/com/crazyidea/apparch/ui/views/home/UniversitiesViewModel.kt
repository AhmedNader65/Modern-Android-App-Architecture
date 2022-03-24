package com.crazyidea.apparch.ui.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazyidea.apparch.data.repository.Resource
import com.crazyidea.apparch.data.repository.Status
import com.crazyidea.apparch.data.repository.UniversityRepository
import com.crazyidea.apparch.model.University
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniversitiesViewModel @Inject constructor(
    private val repository: UniversityRepository,
) : ViewModel() {
    private val _uniList = MutableStateFlow(Resource<List<University>>(Status.LOADING, null, null))

    val uniList: StateFlow<Resource<List<University>>> = _uniList.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchUniversities(country: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            repository.fetchUniversities(true, country)
                .collect {
                    if (it != null) {
                        _uniList.value =  it.copy()
                        getValue()
                    }
                }
        }
    }

    private fun getValue() {
        var x = 5
        x = 432
    }
}