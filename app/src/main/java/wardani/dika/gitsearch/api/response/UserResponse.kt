package wardani.dika.gitsearch.api.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("login")
    val name: String,
    @SerializedName("avatar_url")
    val photo: String
)