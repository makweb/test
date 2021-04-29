package ru.skillbranch.skillarticles.ui.delegates

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingDelegate<T : ViewBinding>(
    private val activity: AppCompatActivity,
    private val initializer: (LayoutInflater) -> T
) : ReadOnlyProperty<AppCompatActivity, T>, LifecycleObserver {
    private var _value: T? = null

    init {
        activity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        //run on activity create callback called
        if (_value == null) {
            _value = initializer(activity.layoutInflater)
        }

        activity.setContentView(_value!!.root) //set main view
        activity.lifecycle.removeObserver(this) //unregister observe this
    }

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        if (_value == null) {
            _value = initializer(thisRef.layoutInflater)
        }

        return _value!!
    }
}

inline fun <reified T : ViewBinding> AppCompatActivity.viewBinding(noinline initializer: (LayoutInflater) -> T) =
    ViewBindingDelegate(this, initializer)


inline fun <reified T : ViewBinding> Fragment.viewBinding(noinline initializer: (View) -> T) =
    FragmentViewBindingDelegate(this, initializer)


class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val initializer: (View) -> T
) : ReadOnlyProperty<Fragment, T>, LifecycleObserver {
    private var _value: T? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

        //run on activity create callback called
        if (_value == null) {
            _value = initializer(fragment.requireView())
        }
        Log.e("ViewBindingDelegate", "fragment view create")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
       _value = null
        Log.e("ViewBindingDelegate", "fragment view destroy")
    }

    init {
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = _value
        if (binding != null) {
            return binding
        }

        val lifecycle = thisRef.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return initializer(thisRef.requireView()).also { this._value = it }
    }
}