package wardani.dika.gitsearch.activity.main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import wardani.dika.gitsearch.R
import wardani.dika.gitsearch.activity.LoadMoreState
import wardani.dika.gitsearch.activity.LoadingState
import wardani.dika.gitsearch.adapter.ItemSearchResultAdapter
import wardani.dika.gitsearch.api.ApiClient
import wardani.dika.gitsearch.api.ApiEndPoint
import wardani.dika.gitsearch.databinding.ActivityMainBinding
import wardani.dika.gitsearch.listener.ScrollListener
import wardani.dika.gitsearch.listener.setOnQueryChangeListener
import wardani.dika.gitsearch.repository.RepositoryFactory
import wardani.dika.gitsearch.util.showWarning


class MainActivity : AppCompatActivity(), ScrollListener.OnLoadMoreListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ItemSearchResultAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var scrollListener: ScrollListener
    private lateinit var loadingViewsMap: Map<Int, View>

    private fun handleLoadingState(loadingState: LoadingState) {
        binding.run {
            val toVisible: Int
            when(loadingState) {
                LoadingState.LOADING -> {
                    toVisible = searchProgress.id
                }
                LoadingState.FINISH -> {
                    toVisible = searchResultRv.id
                    binding.dataContainer.visibility = View.VISIBLE
                }
                LoadingState.NO_DATA -> {
                    toVisible = noDataCmp.root.id
                    val queryEmpty = searchGitSv.query.isEmpty()
                    val text = if (queryEmpty) "Start Searching" else "Reload"
                    noDataCmp.reloadBtn.text = text
                    binding.dataContainer.visibility = View.GONE
                }
            }

            loadingViewsMap.keys.forEach {
                val view = loadingViewsMap[it]
                if (view != null) {
                    if (it == toVisible) {
                        view.visibility = View.VISIBLE
                    } else {
                        view.visibility = View.GONE
                    }
                } else {
                    Log.d(TAG, "Related component for loading view not found, id $it")
                }
            }
        }

    }

    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
        try {
            ProviderInstaller.installIfNeeded(callingActivity)
        } catch (e: GooglePlayServicesRepairableException) {
            showWarning("${e.message}")
        } catch (e: GooglePlayServicesNotAvailableException) {
            showWarning("Google Play Services not available.")
        }
    }

    private fun handleLoadMoreDataState(loadMoreState: LoadMoreState){
        when(loadMoreState) {
            LoadMoreState.LOADING -> {
                scrollListener.isLoading = true
                binding.loadMoreLoading.visibility = View.VISIBLE
            }
            LoadMoreState.DONE -> {
                scrollListener.isLoading = false
                binding.loadMoreLoading.visibility = View.GONE
            }
            LoadMoreState.NO_MORE_DATA -> {
                scrollListener.isLastPage = true
                binding.loadMoreLoading.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        updateAndroidSecurityProvider(this)

        val endPoint = ApiClient.createEndPoint(this, ApiEndPoint::class.java)
        viewModel = MainViewModel(
            application = application,
            githubRepository = RepositoryFactory.createGithubRepository(endPoint)
        )

        binding.run {
            adapter = ItemSearchResultAdapter(arrayListOf())
            val layoutManager = LinearLayoutManager(this@MainActivity)
            searchResultRv.adapter = adapter
            searchResultRv.layoutManager = layoutManager
            scrollListener = ScrollListener(layoutManager, this@MainActivity)
            searchResultRv.addOnScrollListener(scrollListener)

            loadingViewsMap = mapOf(
                searchProgress.id to binding.searchProgress,
                searchResultRv.id to binding.searchResultRv,
                binding.noDataCmp.root.id to binding.noDataCmp.root
            )

            searchGitSv.setOnQueryChangeListener {
                adapter.clear()
                viewModel.search(it)
            }

            noDataCmp.reloadBtn.setOnClickListener {
                if (searchGitSv.query.isEmpty()) {
                    searchGitSv.requestFocus()
                } else {
                    viewModel.reload()
                }
            }

            handleLoadingState(LoadingState.NO_DATA)
        }

        viewModel.run {
            searchLiveData.observe(this@MainActivity) { adapter.addItems(*it.toTypedArray()) }
            loadingStateLiveData.observe(this@MainActivity) { handleLoadingState(it) }
            loadMoreStateLiveData.observe(this@MainActivity) { handleLoadMoreDataState(it) }
            errorMessage.observe(this@MainActivity) { showWarning(it) }
        }

    }

    override fun onLoadMoreItems() {
        viewModel.loadMoreData()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}