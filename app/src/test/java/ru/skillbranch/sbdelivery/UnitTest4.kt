package ru.skillbranch.sbdelivery

import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifier
import ru.skillbranch.sbdelivery.core.notifier.BasketNotifierImpl
import ru.skillbranch.sbdelivery.core.notifier.event.BasketEvent

class UnitTest4 {

    private lateinit var notifier: BasketNotifier

    @Before
    fun setUp() {
        notifier = BasketNotifierImpl()
    }

    @Test
    fun module4() {
        //when single event should return one event in subscribe
        val event = BasketEvent.AddDish("test", "Test", "test")
        notifier.putDishes(event)

        notifier.eventSubscribe().test()
            .assertNotComplete()
        Assertions.assertThat(notifier.eventSubscribe().test().values().size).`as`("check BasketNotifier values size - when single event should return one event in subscribe").isEqualTo(1)


        //when put many clone data event should be return buffer event
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)
        notifier.putDishes(event)

        notifier.eventSubscribe().test()
                .assertNotComplete()

        Assertions.assertThat(notifier.eventSubscribe().test().values().size).`as`("check BasketNotifier values size - when put many clone data event should be return buffer event").isEqualTo(12)

        //when many other data event should be return actual and order data
        notifier = BasketNotifierImpl()
        val listTestEvent = listOf(
                BasketEvent.AddDish("test", "Test", "test"),
                BasketEvent.AddDish("nst", "tyas", "sfx"),
                BasketEvent.AddDish("ase", "ew", "sdc"),
                BasketEvent.AddDish("tweq", "ewt", "dee")
        )
        listTestEvent.forEach {
            notifier.putDishes(it)
        }

        notifier.eventSubscribe().test()
                .assertNotComplete()
        Assertions.assertThat(notifier.eventSubscribe().test().values()).`as`("check BasketNotifier values data - when many other data event should be return actual and order data").isEqualTo(listTestEvent)
        Assertions.assertThat(notifier.eventSubscribe().test().values().size).`as`("check BasketNotifier values size - when many other data event should be return actual and order data").isEqualTo(listTestEvent.size)
    }
}