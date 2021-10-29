package github.alexzhirkevich.community.core.entities.interfaces


interface Group  : Entity, Listable, Chat {
    var creatorId: String
}