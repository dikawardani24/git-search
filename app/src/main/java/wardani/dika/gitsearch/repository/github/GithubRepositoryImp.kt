package wardani.dika.gitsearch.repository.github

import io.reactivex.Observable
import wardani.dika.gitsearch.api.ApiEndPoint
import wardani.dika.gitsearch.api.response.ProjectListResponse
import wardani.dika.gitsearch.api.response.UserListResponse
import wardani.dika.gitsearch.model.SearchResult
import wardani.dika.gitsearch.util.JSonHelper
import wardani.dika.gitsearch.util.mapToList

class GithubRepositoryImp(
    private val apiEndPoint: ApiEndPoint
) : GithubRepository {

    override fun searchUser(query: String, page: Int): Observable<List<SearchResult>> {
        return apiEndPoint.searchUser(query, page, "stars", "desc").map {
            val response = JSonHelper.fromJson(UserListResponse::class.java, it)
            mapToList(response.items) { input -> SearchResult.from(input) }
        }
    }

    override fun searchProject(query: String, page: Int): Observable<List<SearchResult>> {
        return apiEndPoint.searchProject(query, page, "stars", "desc").map {
            val response = JSonHelper.fromJson(ProjectListResponse::class.java, it)
            mapToList(response.items) { input -> SearchResult.from(input) }
        }
    }
}