package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.Contact
import github.alexzhirkevich.community.core.Response
import github.alexzhirkevich.community.core.repo.base.RangeEntityRepository
import github.alexzhirkevich.community.core.repo.base.EntityRepository
import kotlinx.coroutines.flow.Flow

interface ContactsRepository :
    EntityRepository<Contact>,
    RangeEntityRepository<Contact> {

    suspend fun findByPhone(phone : String) : Flow<Response<Contact>>

    suspend fun synchronize(contacts : Collection<Contact>)
}