package com.example.sosyalyazilim.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.example.sosyalyazilim.entity.model.data.MockResultItem
import com.example.sosyalyazilim.entity.model.sealed.Results
import com.example.sosyalyazilim.entity.rest.emrediricanService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MockViewModel: ViewModel(){


    private val mockLiveData = MutableLiveData<Results<List<MockResultItem>>>()
    private val isViewProgressBar= MutableLiveData<Int>()

    fun getMockLiveData(): MutableLiveData<Results<List<MockResultItem>>> {
        return mockLiveData
    }
    fun getProgresBarStateLiveData(): LiveData<Int> {
        return isViewProgressBar
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return uiScope.launch{
            try {
                isViewProgressBar.value = View.VISIBLE
                block()
            } catch (error: Error) {
                isViewProgressBar.value = View.GONE
            } finally {
                isViewProgressBar.value = View.GONE
            }
        }
    }
    //Local file read
    fun getMockListData() {

        launchDataLoad{

            val result = emrediricanService.mockListData().await()
            if (result.isSuccessful) {
                result.body()?.let { mockItem  ->
                    mockLiveData.value= Results.Success(mockItem.data)

                }
            }
            else{
                result
            }

        }
    }






}
