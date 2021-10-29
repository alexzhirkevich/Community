package github.alexzhirkevich.community.core.entities.interfaces

interface Chat : Entity {
    var lastMessage : Sendable?
    var creationTime : Long
    var type : Int

    companion object {
        const val TYPE_GROUP = 0
        const val TYPE_DIALOG = 1
        const val TYPE_CHANNEL = 2
    }
}