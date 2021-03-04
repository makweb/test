package ru.skillbranch.sbdelivery

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import ru.skillbranch.sbdelivery.domain.filter.CategoriesFilterUseCase
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError


/**
 *  группируем  продукты по категориям
 **/
class UnitTest1 {

    private val repository = mock<DishesRepositoryContract>()
    private lateinit var useCase: CategoriesFilterUseCase

    @Before
    fun setUp() {
        useCase = CategoriesFilterUseCase(repository)
    }

    @Test
    fun module1() {

        //when send categoryId should return filter list in categoryIds list
        // given
        whenever(repository.getCachedDishes()).thenReturn(
            Single.just(
                MockDataHolder.listDishes
            )
        )
        var targetCategoryId = "5ed8da011f071c00465b1fe4"
        val correctDishes = MockDataHolder.listDishes.filter { it.categoryId == targetCategoryId }
        // when
        useCase.categoryFilterDishes(targetCategoryId)
            .test()
            .assertResult(correctDishes)
        // then
        verify(repository, times(1).description("when send categoryId should return filter list in categoryIds list")).getCachedDishes()

        // given
        whenever(repository.getCachedDishes()).thenReturn(
                Single.just(
                        MockDataHolder.listDishes
                )
        )
        targetCategoryId = ""
        // when
        useCase.categoryFilterDishes(targetCategoryId)
                .test()
                .assertResult(MockDataHolder.listDishes)
        // then
        verify(repository, times(2).description("when empty categoryId should return full list DishesEntity")).getCachedDishes()

        // given
        whenever(repository.getCachedDishes()).thenReturn(
                Single.just(
                        MockDataHolder.listDishes
                )
        )
        targetCategoryId = "5ed8da011f071c09565b1fe4"
        val correctError = EmptyDishesError::class.java
        // when
        useCase.categoryFilterDishes(targetCategoryId)
                .test()
                .assertError(correctError)
        // then
        verify(repository,times(3).description("when send categoryId should filter empty list throw EmptyDishesError")).getCachedDishes()
    }
}