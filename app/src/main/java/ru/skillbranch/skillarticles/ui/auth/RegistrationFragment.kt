package ru.skillbranch.skillarticles.ui.auth

import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.ui.RootActivity
import ru.skillbranch.skillarticles.ui.base.BaseFragment
import ru.skillbranch.skillarticles.viewmodels.auth.AuthViewModel

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment() : BaseFragment<AuthViewModel>() {

    //for testing
    var _mockFactory: ((SavedStateRegistryOwner)->ViewModelProvider.Factory)? = null

    override val viewModel: AuthViewModel by viewModels{
        _mockFactory?.invoke(this) ?: defaultViewModelProviderFactory
    }

    override val layout: Int = R.layout.fragment_auth


    override fun setupViews() {

    }


}
