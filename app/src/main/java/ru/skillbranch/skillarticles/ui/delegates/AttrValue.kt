package ru.skillbranch.skillarticles.ui.delegates

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AttrValueDelegate(@AttrRes private val res: Int, val context: Context) :
    ReadOnlyProperty<Any, Int> {
    private var _value: Int? = null
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        if (_value == null) {
            val tv = TypedValue()
            if (context.theme.resolveAttribute(res, tv, true)) _value = tv.data
            else throw Resources.NotFoundException("Resource with id $res not found")
        }
        return _value!!
    }
}

class AttrValue(@AttrRes private val res: Int) {
    operator fun provideDelegate(
        thisRef: Any,
        prop: KProperty<*>
    ): ReadOnlyProperty<Any, Int> = when (thisRef) {
        is Activity -> AttrValueDelegate(res, thisRef)
        is Fragment -> AttrValueDelegate(res, thisRef.requireContext())
        is View -> AttrValueDelegate(res, thisRef.context)
        else -> error("attrValue must be inside Activity / Fragment or View")
    }

}