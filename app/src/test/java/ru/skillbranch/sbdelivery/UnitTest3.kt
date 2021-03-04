package ru.skillbranch.sbdelivery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.ReplaySubject
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.skillbranch.sbdelivery.domain.SearchUseCase
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper
import ru.skillbranch.sbdelivery.ui.search.SearchState
import ru.skillbranch.sbdelivery.ui.search.SearchViewModel


/*
* Обработка ошибок при поиске товаров
* */
@RunWith(JUnit4::class)
class UnitTest3 {

    @Rule
    @JvmField
    var executorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private val mapper: DishesMapper = mock()
    private val useCase: SearchUseCase = mock()
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        viewModel = SearchViewModel(useCase, mapper)
    }

    @Test
    fun module3() {
        //when use case error data should value return in SearchState Error
        whenever(useCase.getDishes()).thenReturn(Single.error(EmptyDishesError("")))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.initState()
        Assertions.assertThat(viewModel.state.value).`as`("check viewModel state - when use case error data should value return in SearchState Error").isEqualTo(SearchState.Error(""))

        verify(useCase, times(1).description("when use case error data should value return in SearchState Error")).getDishes()


        //when use loadingState data should value return in SearchState Error
        val hotObserve: ReplaySubject<List<DishEntity>> = ReplaySubject.create()
        whenever(useCase.getDishes()).thenReturn(hotObserve.hide().single(MockDataHolder.listDishes))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.initState()
        Assertions.assertThat(viewModel.state.value).`as`("check viewModel state - when use loadingState data should value return in SearchState Error").isNotEqualTo(SearchState.Error(""))

        verify(useCase,times(2).description("when use case error data should value return in SearchState Error")).getDishes()

        //when search in dishes should return loading not show Error state
        viewModel.setSearchEvent(Observable.just("Test"))
        Assertions.assertThat(viewModel.state.value).`as`("check viewModel state - when search in dishes should return loading not show Error state").isNotEqualTo(SearchState.Error(""))
        verify(useCase,times(1).description("when use case error data should value return in SearchState Error")).findDishesByName(any())


        //when search in dishes should return error use case show Error state
        whenever(useCase.findDishesByName(any())).thenReturn(Observable.error(EmptyDishesError("")))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.setSearchEvent(Observable.just("Test"))
        Assertions.assertThat(viewModel.state.value).`as`("check viewModel state - when search in dishes should return error use case show Error state").isEqualTo(SearchState.Error(""))
        verify(useCase, times(2).description("when use case error data should value return in SearchState Error")).findDishesByName(any())

        //when use case error data should not value state Result
        whenever(useCase.getDishes()).thenReturn(Single.error(RuntimeException("")))
        viewModel.initState()
        Assertions.assertThat(viewModel.state.value).`as`("check viewModel state - when use case error data should not value state Result").isNotEqualTo(SearchState.Result(MockDataHolder.searchStateList))

        verify(useCase, times(3).description("when use case error data should not value state Result")).getDishes()

    }
}