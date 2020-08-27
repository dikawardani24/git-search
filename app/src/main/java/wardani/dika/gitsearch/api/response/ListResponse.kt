package wardani.dika.gitsearch.api.response

import com.google.gson.annotations.SerializedName

open class ListResponse<T>(
    @SerializedName("total_count")
    val itemCount: Long,
    @SerializedName("incomplete_results")
    val incompleteResult: Boolean,
    @SerializedName("items")
    val items: List<T>
)