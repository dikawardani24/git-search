package wardani.dika.gitsearch.activity.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import wardani.dika.gitsearch.activity.LoadMoreState
import wardani.dika.gitsearch.activity.LoadingState
import wardani.dika.gitsearch.api.response.ErrorResponse
import wardani.dika.gitsearch.exception.SystemException
import wardani.dika.gitsearch.model.SearchResult
import wardani.dika.gitsearch.repository.github.GithubRepository
import wardani.dika.gitsearch.util.JSonHelper
import java.net.UnknownHostException

class MainViewModel(
    application: Application,
    private val githubRepository: GithubRepository
) : AndroidViewModel(application) {
    val loadingStateLiveData: LiveData<LoadingState> = MutableLiveData()
    val loadMoreStateLiveData: LiveData<LoadMoreState> = MutableLiveData()
    val searchLiveData: LiveData<List<SearchResult>>  = MutableLiveData()
    val errorMessage: LiveData<String> = MutableLiveData()

    private var currentPage = 0
    private var currentKeyword = ""
    private var currentProcess: Disposable? = null

    private fun cancelCurrentProcess() {
        currentProcess?.let {
            Log.d(TAG, "Canceling previous process")
            if (!it.isDisposed) {
                (loadMoreStateLiveData as MutableLiveData).postValue(LoadMoreState.DONE)
                it.dispose()
                currentProcess = null
            }
        }
    }

    private fun startSearching(onResultFetched: () -> Unit,
                               onEmptyResult: () -> Unit,
                               onError:() -> Unit) {
        cancelCurrentProcess()

        val errorMessageLiveData = errorMessage as MutableLiveData

        if (currentKeyword.isEmpty()) {
            onError()
            return
        }

        currentProcess = Observables.zip(
            githubRepository.searchProject(currentKeyword, currentPage),
            githubRepository.searchUser(currentKeyword, currentPage)
        ).subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    val dataSource = listOf(it.first, it.second)
                    val searchResults = arrayListOf<SearchResult>()

                    dataSource.forEach { searchResults.addAll(it) }

                    if (searchResults.isEmpty()) {
                        onEmptyResult()
                    } else {
                        val searchResultLiveData = searchLiveData as MutableLiveData
                        searchResultLiveData.postValue(searchResults)
                        onResultFetched()
                    }
                },
                onError = {
                    it.printStackTrace()
                    val message = when(it) {
                        is UnknownHostException -> "Unable to reach server, please check your internet connection"
                        is HttpException -> {
                            val errorResponseJsonString = it.response()?.errorBody()?.string()
                            if (errorResponseJsonString != null) {
                                val errorResponse = JSonHelper.fromJson(ErrorResponse::class.java, errorResponseJsonString)
                                errorResponse.message
                            } else {
                                it.message()
                            }
                        }
                        is SystemException -> it.message
                        else -> "Unknown error occurred, please try again later"
                    }

                    errorMessageLiveData.postValue(message)
                    onError()
                },
                onComplete = {
                    currentProcess = null
                }
            )
    }


    fun loadMoreData() {
        currentPage += 1
        val loadMoreStateLiveData = loadMoreStateLiveData as MutableLiveData

        loadMoreStateLiveData.postValue(LoadMoreState.LOADING)

        startSearching(
            onEmptyResult = {
                loadMoreStateLiveData.postValue(LoadMoreState.NO_MORE_DATA)
            },
            onResultFetched = {
                loadMoreStateLiveData.postValue(LoadMoreState.DONE)
            },
            onError = {
                loadMoreStateLiveData.postValue(LoadMoreState.DONE)
            }
        )
    }

    fun reload() {
        search(currentKeyword)
    }

    fun search(keyword: String?) {
        currentKeyword = keyword ?: ""
        currentPage = 1
        val loadingStateLiveData = loadingStateLiveData as MutableLiveData

        loadingStateLiveData.postValue(LoadingState.LOADING)
        startSearching(
            onEmptyResult = {
                loadingStateLiveData.postValue(LoadingState.NO_DATA)
            },
            onResultFetched = {
                loadingStateLiveData.postValue(LoadingState.FINISH)
            },
            onError = {
                loadingStateLiveData.postValue(LoadingState.NO_DATA)
            }
        )
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}