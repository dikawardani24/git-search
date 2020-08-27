package wardani.dika.gitsearch.api.response

class UserListResponse(
    itemCount: Long,
    incompleteResult: Boolean,
    items: List<UserResponse>
) : ListResponse<UserResponse>(itemCount, incompleteResult, items)