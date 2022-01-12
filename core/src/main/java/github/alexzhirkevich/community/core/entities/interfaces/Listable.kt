package github.alexzhirkevich.community.core.entities.interfaces

sealed interface Listable : Entity {

    var name : String
    var imageUri : String
}