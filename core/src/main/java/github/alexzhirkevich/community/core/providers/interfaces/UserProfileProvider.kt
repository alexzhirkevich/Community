package github.alexzhirkevich.community.core.providers.interfaces

import github.alexzhirkevich.community.core.providers.base.IndependentProvider

interface UserProfileProvider : IndependentProvider{

    suspend fun setName(name : String)

    suspend fun setUsername(username : String)

    suspend fun setDescription(text : String)

    suspend fun setImageUri(uri : String)

//    fun getContacts() : Observable<List<IUser>>
//
//    fun setContacts(contacts : Map<String, String>) : Completable

}