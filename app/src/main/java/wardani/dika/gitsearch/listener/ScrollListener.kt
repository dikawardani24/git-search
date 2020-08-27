package wardani.dika.gitsearch.listener

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val onLoadMoreListener: OnLoadMoreListener
): RecyclerView.OnScrollListener() {
    var isLoading = false
    var isLastPage = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (!isLoading && !isLastPage) {
            if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                Log.d(TAG, "Loading more data")
                onLoadMoreListener.onLoadMoreItems()
            }
        }
    }

    interface OnLoadMoreListener {
        fun onLoadMoreItems()
    }

    companion object {
        private const val TAG = "ScrollListener"
    }
}