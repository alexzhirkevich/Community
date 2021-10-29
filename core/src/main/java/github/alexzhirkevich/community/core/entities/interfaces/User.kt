package github.alexzhirkevich.community.core.entities.interfaces

interface User  : Contact, Listable {
    var username : String
    var description : String
    var creationTime : Long
    var lastOnline: Long
    var isOnline: Boolean
    var isBot : Boolean
}