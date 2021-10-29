package github.alexzhirkevich.community.core.entities.interfaces


interface IEvent : Entity, Listable {

    var creatorId : String
    var description : String
    var time : Long
    var address : String
    var location : Pair<Float,Float>
    var isValid : Boolean
    var isEnded : Boolean
}