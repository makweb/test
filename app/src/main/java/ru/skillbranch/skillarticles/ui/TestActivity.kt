package ru.skillbranch.skillarticles.ui

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.ui.base.BaseActivity
import ru.skillbranch.skillarticles.ui.base.Binding
import ru.skillbranch.skillarticles.viewmodels.base.BaseViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState
import ru.skillbranch.skillarticles.viewmodels.base.Notify

class TestActivity : BaseActivity<TestViewModel>() {
    override val layout: Int = R.layout.activity_root
    public override val viewModel : TestViewModel by provideViewModel("test args for TestViewModel by provideViewModel")
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override val binding: TestBinding by lazy { TestBinding() }

    override fun setupViews() {
    }

    override fun renderNotification(notify: Notify) {
    }

    inner class TestBinding:Binding(){
        override fun onFinishInflate() {
        }

        override fun bind(data: IViewModelState) {
        }

        override fun saveUi(outState: Bundle) {
        }

        override fun restoreUi(savedState: Bundle) {
        }

    }
}

class TestViewModel(val args : String) : BaseViewModel<TestState>(TestState()){

}

data class TestState(val test : String = "test") : IViewModelState {
    override fun save(outState: Bundle) {}

    override fun restore(savedState: Bundle): TestState {
        return copy()
    }

}
