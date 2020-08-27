package wardani.dika.gitsearch.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndPoint {
    @GET("search/users")
    fun searchUser(@Query("q") query: String,
                   @Query("page") page: Int,
                   @Query("sort") sort: String,
                   @Query("order") order: String): Observable<String>

    @GET("search/repositories")
    fun searchProject(@Query("q") query: String,
                      @Query("page") page: Int,
                      @Query("sort") sort: String,
                      @Query("order") order: String): Observable<String>
}