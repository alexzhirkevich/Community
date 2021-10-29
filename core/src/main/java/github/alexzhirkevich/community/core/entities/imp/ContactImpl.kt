package github.alexzhirkevich.community.core.entities.imp

import github.alexzhirkevich.community.core.entities.interfaces.Contact

open class ContactImpl(
    final override var id: String = "",
    final override var name: String="",
    final override var phone: String=""
    ) :  Contact {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ContactImpl) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (phone != other.phone) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + phone.hashCode()
        return result
    }

    override fun toString(): String {
        return "ContactImpl(id='$id', name='$name', phone='$phone')"
    }
}