package github.alexzhirkevich.community.core.entities.interfaces

interface IChannelAdmin  : Entity {

    var canEdit : Boolean
    var canPost : Boolean
    var canDelete : Boolean
    var canBan : Boolean

}