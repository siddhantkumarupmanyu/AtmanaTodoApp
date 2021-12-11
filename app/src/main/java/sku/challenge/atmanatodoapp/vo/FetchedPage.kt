package sku.challenge.atmanatodoapp.vo

data class FetchedPage(
    val page: Int,
    val data: List<Item>
) {

    companion object {
        // page 0 and page 1 are same for api
        // we are assuming page 0 as initial page with no data
        val NO_PAGE = FetchedPage(0, emptyList())
    }

}