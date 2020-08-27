package wardani.dika.gitsearch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import wardani.dika.gitsearch.R
import wardani.dika.gitsearch.model.ResultType
import wardani.dika.gitsearch.model.SearchResult
import java.lang.Exception

class ItemSearchResultAdapter(
    private val searchResults: ArrayList<SearchResult>
): RecyclerView.Adapter<ItemSearchResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var photoIv: ImageView = view.findViewById(R.id.resultPhoto)
        private var nameTv: TextView = view.findViewById(R.id.resultTv)
        private var resultTypeImg: ImageView = view.findViewById(R.id.resultTypeImg)
        private var resultTypeTv: TextView = view.findViewById(R.id.resultTypeTv)

        @SuppressLint("SetTextI18n")
        fun bind(searchResult: SearchResult) {
            nameTv.text = searchResult.name

            val context = itemView.context
            when(searchResult.resultType) {
                ResultType.REPO -> {
                    resultTypeImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_folder_open_24))
                    resultTypeTv.text = "Repository"
                }
                ResultType.USER -> {
                    resultTypeImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_person_outline_24))
                    resultTypeTv.text = "User"
                }
            }

            if (searchResult.photo.isNotEmpty()) {
                Picasso.get().load(searchResult.photo)
                    .error(ContextCompat.getDrawable(context, R.drawable.progress_animation)!!)
                    .into(photoIv, object : Callback {
                        override fun onSuccess() {
                            photoIv.scaleType = ImageView.ScaleType.CENTER_CROP
                            photoIv.setPadding(0)
                        }
                        override fun onError(e: Exception?) {
                            photoIv.scaleType = ImageView.ScaleType.CENTER_INSIDE
                            photoIv.setPadding(30)
                        }
                    })
            }
        }

    }

    fun addItems(vararg items: SearchResult) {
        searchResults.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        searchResults.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchResult = searchResults[position]
        holder.bind(searchResult)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }
}