package github.alexzhirkevich.community.core.entities.interfaces

interface Channel : Chat, Listable {
    var tag: String
    var description: String
    var creatorId: String
}