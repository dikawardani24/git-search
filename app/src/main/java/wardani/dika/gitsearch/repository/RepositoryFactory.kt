package wardani.dika.gitsearch.repository

import wardani.dika.gitsearch.api.ApiEndPoint
import wardani.dika.gitsearch.repository.github.GithubRepository
import wardani.dika.gitsearch.repository.github.GithubRepositoryImp

object RepositoryFactory {

    fun createGithubRepository(apiEndPoint: ApiEndPoint): GithubRepository {
        return GithubRepositoryImp(apiEndPoint)
    }

}