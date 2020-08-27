package wardani.dika.gitsearch.api.response

import com.google.gson.annotations.SerializedName

data class ProjectResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("owner")
    val owner: UserResponse
)