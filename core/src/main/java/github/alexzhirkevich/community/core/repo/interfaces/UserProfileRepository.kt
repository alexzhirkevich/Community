package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.repo.base.IndependentRepository

interface UserProfileRepository : IndependentRepository{

    suspend fun setName(id : String, name : String)

    suspend fun setUsername(id : String, username : String)

    suspend fun setDescription(id : String, text : String)

    suspend fun setImageUri(id : String, uri : String)

//    fun getContacts() : Observable<List<IUser>>
//
//    fun setContacts(contacts : Map<String, String>) : Completable

}