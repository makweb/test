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
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper
import ru.skillbranch.sbdelivery.ui.search.SearchState
import ru.skillbranch.sbdelivery.ui.search.SearchViewModel


/**
 * Состояния загрузки при поиске товаров
 **/
@RunWith(JUnit4::class)
class UnitTest2 {

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
    fun module2() {
        //when loading and init should state Loading
        var hotObserve: ReplaySubject<List<DishEntity>> = ReplaySubject.create()
        whenever(useCase.getDishes()).thenReturn(hotObserve.hide().single(listOf()))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.initState()
        Assertions.assertThat(viewModel.state.value).isEqualTo(SearchState.Loading)

        verify(useCase,times(1).description("when loading and init should state Loading")).getDishes()

        //when delay loading and init should state Loading
        hotObserve = ReplaySubject.create()
        whenever(useCase.getDishes()).thenReturn(hotObserve.hide().single(MockDataHolder.listDishes))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.initState()
        Assertions.assertThat(viewModel.state.value).isEqualTo(SearchState.Loading)

        verify(useCase, times(2).description("when delay loading and init should state Loading")).getDishes()

        //when on error and init should not state loading
        whenever(useCase.getDishes()).thenReturn(Single.error(RuntimeException()))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.initState()
        Assertions.assertThat(viewModel.state.value).isNotEqualTo(SearchState.Loading)

        verify(useCase, times(3).description("when on error and init should not state loading")).getDishes()

        //when on full result data and init should not state loading
        whenever(useCase.getDishes()).thenReturn(Single.just(MockDataHolder.listDishes))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.initState()
        Assertions.assertThat(viewModel.state.value).isNotEqualTo(SearchState.Loading)

        verify(useCase,times(4).description("when on full result data and init should not state loading")).getDishes()


        //when use case success data should value state in Result
        whenever(useCase.getDishes()).thenReturn(Single.just(MockDataHolder.listDishes))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.initState()
        Assertions.assertThat(viewModel.state.value).`as`("check viewModel state - when use case success data should value state in Result").isEqualTo(SearchState.Result(MockDataHolder.searchStateList))

        verify(useCase,times(5).description("when use case success data should value state in Result")).getDishes()
        verify(mapper,times(2).description("when use case success data should value state in Result")).mapDtoToState(any())

        //when search in dishes should return success show Result state
        whenever(useCase.findDishesByName(any())).thenReturn(Observable.just(MockDataHolder.listDishes))
        whenever(mapper.mapDtoToState(any())).thenReturn(MockDataHolder.searchStateList)
        viewModel.setSearchEvent(Observable.just("Test"))
        Assertions.assertThat(viewModel.state.value).`as`("check viewModel state - when search in dishes should return success show Result state").isEqualTo(SearchState.Result(MockDataHolder.searchStateList))

        verify(useCase,times(1).description("when search in dishes should return success show Result state")).findDishesByName(any())
        verify(mapper,times(3).description("when search in dishes should return success show Result state")).mapDtoToState(any())



    }
}