package ru.skillbranch.sbdelivery.ui.main

import ru.skillbranch.sbdelivery.core.adapter.CategoryItemState
import ru.skillbranch.sbdelivery.core.adapter.ProductItemState

sealed class MainState {
    object Loader : MainState()
    data class Error(
        val message: String, val error: Throwable,
        val isRefresh: Boolean = true,
        val categories: List<CategoryItemState> = emptyList()
    ) : MainState()

    data class Result(
        val productItems: List<ProductItemState>,
        val categories: List<CategoryItemState>
    ) : MainState()
}