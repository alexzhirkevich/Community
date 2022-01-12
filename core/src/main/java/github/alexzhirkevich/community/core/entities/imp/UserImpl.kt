package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.entities.interfaces.User

class UserImpl constructor(
    id: String = "",
    name: String = "",
    phone: String = "",
    override var imageUri: String = "",
    override var tag: String = "",
    override var description: String="",
    override var creationTime: Long =  0,
    override var lastOnline: Long = 0,
    override var isOnline: Boolean = false,
    override var isBot: Boolean = false
) : ContactImpl(id = id,name = name,phone = phone), User {

    override fun compareTo(other: Entity): Int {
        return if (other is UserImpl) name.compareTo(other.name) else 0
    }
}