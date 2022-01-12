package github.alexzhirkevich.community.core.entities.interfaces

interface User  : Contact, Listable, Taggable {
    var description : String
    var creationTime : Long
    var lastOnline: Long
    var isOnline: Boolean
    var isBot : Boolean
}