package wardani.dika.gitsearch.listener

import android.util.Log
import androidx.appcompat.widget.SearchView

fun SearchView.setOnQueryChangeListener(onQueryChangeListener: (newQuery: String?) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            onQueryChangeListener(newText)
            Log.d("OnQueryChangeListener", "$newText")
            return true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            onQueryChangeListener(query)
            return false
        }
    })
}