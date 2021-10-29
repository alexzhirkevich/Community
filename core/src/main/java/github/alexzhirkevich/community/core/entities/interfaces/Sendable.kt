package github.alexzhirkevich.community.core.entities.interfaces


interface Sendable  : Entity {
    var type : Int
    var chatId: String
    var time: Long

    companion object {
        const val TYPE_SYSTEM = 1
        const val TYPE_USER = 0
    }
}