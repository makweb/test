package ru.skillbranch.sbdelivery.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.skillbranch.sbdelivery.core.BaseViewModel
import ru.skillbranch.sbdelivery.core.adapter.CategoryItemState
import ru.skillbranch.sbdelivery.core.adapter.ProductItemState
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifier
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent
import ru.skillbranch.sbdelivery.domain.filter.CategoriesFilter
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError
import ru.skillbranch.sbdelivery.repository.mapper.CategoriesMapper
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper

class MainViewModel(
    private val repository: DishesRepositoryContract,
    private val dishesMapper: DishesMapper,
    private val categoriesMapper: CategoriesMapper,
    private val notifier: BasketNotifier,
    private val filterUseCase: CategoriesFilter
) : BaseViewModel() {

    private val defaultState = MainState.Loader
    private val action = MutableLiveData<MainState>()
    val state: LiveData<MainState>
        get() = action

    private var categories: List<CategoryItemState> = emptyList()

    fun loadDishes(categoryId: String) {
        filterOrCachesDish(categoryId)
            .doOnSubscribe { action.value = defaultState }
            .flatMap { dishes -> repository.getCategories().map { it to dishes } }
            .map { categoriesMapper.mapDtoToState(it.first) to dishesMapper.mapDtoToState(it.second) }
            .subscribe({
                categories = it.first
                val newState = MainState.Result(it.second, categories)
                action.value = newState
            }, {
                if (it is EmptyDishesError) {
                    action.value =
                        MainState.Error("Продукт по данной категории недоступен", it, false, categories = categories)
                } else {
                    action.value = MainState.Error("Что то пошло не по плану", it)
                }
                it.printStackTrace()
            }).track()
    }

    private fun filterOrCachesDish(categoryId: String) =
        if (categoryId.isBlank()) repository.getDishes() else filterUseCase.categoryFilterDishes(categoryId)

    fun handleAddBasket(item: ProductItemState) {
        notifier.putDishes(BasketEvent.AddDish(item.id, item.title, item.price))
    }
}