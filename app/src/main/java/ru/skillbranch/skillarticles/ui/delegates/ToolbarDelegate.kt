package ru.skillbranch.skillarticles.ui.delegates

import android.app.Activity
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.ui.RootActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

//fun Fragment.toolbar() = ToolbarDelegate(this)

class ToolbarDelegate(@IdRes  val toolbarId : Int = R.id.toolbar) :
    ReadOnlyProperty<Fragment, Toolbar>,
    LifecycleObserver {
    private var _toolbar: Toolbar? = null
/*
    init {
        fragment.setHasOptionsMenu(true)

        *//*fragment.lifecycle.addObserver(object : LifecycleObserver{
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                fragment
            }
        })*//*
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
            Log.e("ToolbarDelegate", "viewLifecycleOwner $viewLifecycleOwner")
            viewLifecycleOwner.lifecycle.addObserver(this)
        }

        fragment.lifecycle.addObserver(object: LifecycleObserver{
                @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                fun onCreateActivity() {
                    fragment.requireActivity().lifecycle.addObserver(object : LifecycleObserver{
                        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                        fun onCreateActivity() {
                            Log.e("ToolbarDelegate", "activity created")
//                            createMenu(menu)
                        }
                    })

                }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

        //run on activity create callback called
        if (_toolbar == null) {
            val activity = fragment.requireActivity() as RootActivity
            _toolbar = activity.viewBinding.toolbar
        }
        Log.e("ToolbarDelegate", "fragment view create")
        createMenu(menu)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
//        _value = null
        Log.e("ToolbarDelegate", "fragment view destroy")
    }

    private fun createMenu(@MenuRes menu : Int) {
        *//*val menu = _toolbar!!.menu
        Log.e("ToolbarDelegate", "create menu $items $menu")
        if(items.isEmpty())  menu.clear()
        else{
            for ((index, menuHolder) in items.withIndex()) {
                val item = menu.add(0, menuHolder.menuId, index, menuHolder.title)
                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
                    .setIcon(menuHolder.icon)
                    .setOnMenuItemClickListener {
                        menuHolder.clickListener?.invoke(it)?.let { true } ?: false
                    }

                if (menuHolder.actionViewLayout != null) item.setActionView(menuHolder.actionViewLayout)
            }
        }*//*
        //run on activity create callback called
//        if (_toolbar == null) {
            val activity = fragment.requireActivity() as RootActivity
            _toolbar = activity.findViewById(R.id.toolbar)
//        }

        Log.e("ToolbarDelegate", "create menu ${Thread.currentThread().name} $menu $_toolbar ")

        fragment.setHasOptionsMenu(true)
        _toolbar!!.inflateMenu(menu)
//        _toolbar!!.invalidate()

//        _toolbar.getMenuI
//        _toolbar!!.inflateMenu(menu)
    }*/


    override fun getValue(thisRef: Fragment, property: KProperty<*>): Toolbar {
        val activity = thisRef.requireActivity() as RootActivity
        if (_toolbar == null) {
            _toolbar = activity.findViewById(toolbarId)


//            if (root.toolbarBuilder.items.isNotEmpty()) {
//                for ((index, menuHolder) in root.toolbarBuilder.items.withIndex()) {
//                    val item = menu.add(0, menuHolder.menuId, index, menuHolder.title)
//                    item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
//                        .setIcon(menuHolder.icon)
//                        .setOnMenuItemClickListener {
//                            menuHolder.clickListener?.invoke(it)?.let { true } ?: false
//                        }
//
//                    if (menuHolder.actionViewLayout != null) item.setActionView(menuHolder.actionViewLayout)
//                }
//            } else menu.clear()
        }

        return _toolbar!!

    }
}

data class MenuItemHolder(
    val title: String,
    val menuId: Int,
    val icon: Int,
    val actionViewLayout: Int? = null,
    val clickListener: ((MenuItem) -> Unit)? = null
)
