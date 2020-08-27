package wardani.dika.gitsearch.model

import wardani.dika.gitsearch.api.response.ProjectResponse
import wardani.dika.gitsearch.api.response.UserResponse

data class SearchResult(var id: Long,
                        var name: String,
                        var photo: String,
                        var resultType: ResultType) {
    companion object {
        fun from(userResponse: UserResponse): SearchResult {
            return SearchResult(
                id = userResponse.id,
                name = userResponse.name,
                photo = userResponse.photo,
                resultType = ResultType.USER
            )
        }

        fun from(projectResponse: ProjectResponse): SearchResult {
            return SearchResult(
                id = projectResponse.id,
                name = projectResponse.name,
                photo = projectResponse.owner.photo,
                resultType = ResultType.REPO
            )
        }
    }
}