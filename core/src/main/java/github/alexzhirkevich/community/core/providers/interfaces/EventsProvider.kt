package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.IEvent
import github.alexzhirkevich.community.core.providers.base.EntityProvider
import github.alexzhirkevich.community.core.providers.base.IndependentProvider
import github.alexzhirkevich.community.core.providers.base.RangeEntityProvider
import github.alexzhirkevich.community.core.providers.base.Removable
import github.alexzhirkevich.community.core.providers.imp.FirebaseProviderImp
import dagger.Component
import javax.inject.Singleton

interface EventsProvider
    : EntityProvider<IEvent>,
    RangeEntityProvider<IEvent>,
    Removable, IndependentProvider

@Singleton
@Component
interface FirebaseProviderComponent{
    fun getUsersProvider() : FirebaseProviderImp
}