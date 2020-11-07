package ru.skillbranch.skillarticles.example

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_test.*
import ru.skillbranch.skillarticles.App
import ru.skillbranch.skillarticles.R
import javax.inject.Inject

class TestActivity : AppCompatActivity() {

   /* @Inject
    lateinit var factory: TestViewModelFactory
    val viewModel: TestViewModel by viewModels { factory.create(this, intent.extras) }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        App.activityComponent = App.appComponent.activityComponent.create(this)

        injectDependency()

//        Log.e("TestActivity", "viewmodelInstance: ${System.identityHashCode(viewModel)}");

        btn_a.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentA())
                .commit()
        }

        btn_b.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentB())
                .commit()
        }

        btn_clear.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .remove(supportFragmentManager.findFragmentById(R.id.container)!!)
                .commit()
        }

    }

    fun injectDependency() {
        App.activityComponent.inject(this)
    }
}