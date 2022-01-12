package github.alexzhirkevich.community.core.entities.interfaces

interface Channel : Chat, Listable, Taggable {
    var description: String
    var creatorId: String
}