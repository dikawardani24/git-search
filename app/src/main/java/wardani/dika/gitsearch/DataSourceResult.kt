package wardani.dika.gitsearch


abstract class DataSourceResult<T> {
    data class Succeed<T>(val data: T): DataSourceResult<T>()
    data class Failed<T>(val error: Throwable): DataSourceResult<T>()
}