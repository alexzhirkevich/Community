package github.alexzhirkevich.community.core.entities.interfaces

interface Contact : Entity {

    var name : String
    var phone : String

    override fun compareTo(other: Entity): Int =
        if (other is Contact) {
            name.compareTo(other.name)
        } else 0
}