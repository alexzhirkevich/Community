package github.alexzhirkevich.community.smt

import dagger.Component
import github.alexzhirkevich.community.core.di.CoreBindModule
import github.alexzhirkevich.community.core.di.CoreModule
import github.alexzhirkevich.community.core.di.Stage
import github.alexzhirkevich.community.core.repo.interfaces.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Singleton
@Component(modules = [
    CoreBindModule::class
])
internal interface SystemMessageComponent{

    val usersRepository : UsersRepository
}