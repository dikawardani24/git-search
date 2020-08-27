package wardani.dika.gitsearch.api.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message")
    val message: String
)