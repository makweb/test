package ru.skillbranch.sbdelivery

import com.nhaarman.mockito_kotlin.*
import io.reactivex.rxjava3.core.Observable
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import ru.skillbranch.sbdelivery.domain.SearchUseCase
import ru.skillbranch.sbdelivery.domain.SearchUseCaseImpl
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import java.util.*

/*
* Сделать поиск с помощью возможностей БД
* */
class UnitTest5 {
    private val repository = mock<DishesRepositoryContract>()
    private lateinit var useCase: SearchUseCase

    @Before
    fun setUp() {
        useCase = SearchUseCaseImpl(repository)
    }

    @Test
    fun module5() {
        //when user input search text when return actual dishes
        whenever(repository.findDishesByName(any())).thenReturn(Observable.just(MockDataHolder.listDishes.filter {
            it.title.toLowerCase(Locale.ROOT).contains(
                "Чак".trim().toLowerCase(
                    Locale.ROOT
                )
            )
        }))
        var actualList = MockDataHolder.listDishes.filter {
            it.title.toLowerCase(Locale.ROOT).contains(
                "Чак".trim().toLowerCase(
                    Locale.ROOT
                )
            )
        }
        useCase.findDishesByName("Чак").test()
            .assertNoErrors()
            .assertResult(actualList)



        //when user input search text when return actual size or order dishes
        whenever(repository.findDishesByName(any())).thenReturn(Observable.just(MockDataHolder.listDishes.sortedBy { it.title }
                .filter {
                    it.title.toLowerCase(Locale.ROOT).contains(
                            "Мясная".trim().toLowerCase(
                                    Locale.ROOT
                            )
                    )
                }))

         actualList = MockDataHolder.listDishes.sortedBy { it.title }.filter {
            it.title.toLowerCase(Locale.ROOT).contains(
                    "Мясная".trim().toLowerCase(
                            Locale.ROOT
                    )
            )
        }
        useCase.findDishesByName("Мясная").test()
                .assertNoErrors()
                .assertResult(actualList)

        verify(repository, times(2).description("when user input search text when return actual size or order dishes")).findDishesByName(any())

        //when user input search text when return actual size or drop
        whenever(repository.findDishesByName(any())).thenReturn(Observable.just(MockDataHolder.listDishes.sortedBy { it.title }
                .filter {
                    it.title.toLowerCase(Locale.ROOT).contains(
                            "Чак".trim().toLowerCase(
                                    Locale.ROOT
                            )
                    )
                }))
        val testValue = useCase.findDishesByName("Мясная").test().values()
        Assertions.assertThat(testValue.size).isEqualTo(1)
        verify(repository,times(3).description("when user input search text when return actual size or drop")).findDishesByName(any())
    }
}