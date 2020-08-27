package wardani.dika.gitsearch.repository.github

import io.reactivex.Observable
import wardani.dika.gitsearch.model.SearchResult

interface GithubRepository {
    fun searchUser(query: String, page: Int): Observable<List<SearchResult>>
    fun searchProject(query: String, page: Int): Observable<List<SearchResult>>
}