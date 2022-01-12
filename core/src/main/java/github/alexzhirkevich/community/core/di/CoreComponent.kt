package github.alexzhirkevich.community.core.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import github.alexzhirkevich.community.core.repo.interfaces.StorageRepository
import github.alexzhirkevich.community.core.repo.stage.TestStorageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Component(modules = [CoreModule::class,CoreBindModule::class])
@Singleton
interface CoreComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context : Context) : Builder
        @BindsInstance
        fun setScope(coroutineScope: CoroutineScope) : Builder

        fun build() : CoreComponent
    }

    @Stage
    val storageRepository : TestStorageRepository
}