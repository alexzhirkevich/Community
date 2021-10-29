package github.alexzhirkevich.community.core.entities.interfaces

import java.io.Serializable

/**
 * Interface for base firebase database Entity
 * */
interface Entity : Comparable<Entity>, Serializable {
    var id: String
}