package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.Contact
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.providers.base.RangeEntityProvider
import github.alexzhirkevich.community.core.providers.base.EntityProvider
import kotlinx.coroutines.flow.Flow

interface ContactsProvider :
    EntityProvider<Contact>,
    RangeEntityProvider<Contact> {

    suspend fun findByPhone(phone : String) : Flow<Response<Contact>>

    suspend fun synchronize(contacts : Collection<Contact>)
}