package wardani.dika.gitsearch.api.response

class ProjectListResponse(
    itemCount: Long,
    incompleteResult: Boolean,
    items: List<ProjectResponse>
) : ListResponse<ProjectResponse>(itemCount, incompleteResult, items)