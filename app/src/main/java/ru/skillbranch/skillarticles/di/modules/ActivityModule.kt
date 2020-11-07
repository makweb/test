package ru.skillbranch.skillarticles.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.repositories.IRepository
import ru.skillbranch.skillarticles.data.repositories.RootRepository
import ru.skillbranch.skillarticles.di.ViewModelKey
import ru.skillbranch.skillarticles.di.scopes.ActivityScope
import ru.skillbranch.skillarticles.example.*

@Module
abstract class ActivityModule {

    @Binds
    @ActivityScope
    abstract fun bindRootRepository(repository : RootRepository): IRepository


    /*@Binds
    @IntoMap
    @ViewModelKey(TestViewModel::class)
    @ActivityScope
    abstract fun bindViewModel(vm:TestViewModel.Factory) : ViewModelAssistedFactory<ViewModel>*/

   /* @Binds
    @IntoMap
    @ViewModelKey(ViewModelA::class)
    @ActivityScope
    abstract fun bindViewModelA(vm:ViewModelA.Factory) : ViewModelAssistedFactory<out ViewModel>*/

    /*@Binds
    @IntoMap
    @ViewModelKey(ViewModelB::class)
    @ActivityScope
    abstract fun bindViewModeBl(vm: ViewModelB.Factory): ViewModelAssistedFactory<out ViewModel>
*/
}