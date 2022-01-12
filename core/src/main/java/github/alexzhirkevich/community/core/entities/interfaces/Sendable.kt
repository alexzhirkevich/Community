package github.alexzhirkevich.community.core.entities.interfaces

interface TimeOwner {
    var time : Long
}

interface Sendable  : TimeOwner, Entity {
    var type : Int
    var chatId: String

    companion object {
        const val TYPE_SYSTEM = 1
        const val TYPE_USER = 0
    }
}