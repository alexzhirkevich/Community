package github.alexzhirkevich.community.screens.auth.di

import dagger.Component
import github.alexzhirkevich.community.core.di.CoreModule
import github.alexzhirkevich.community.core.repo.interfaces.PhoneAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Singleton
@Component(modules = [CoreModule::class])
interface AuthComponent {
    val phoneAuthProvider: PhoneAuthRepository
}
