package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.repo.base.Repository
import github.alexzhirkevich.community.core.repo.base.Synchronizable

typealias SettingsChangeListener<T> = (new : T) -> Unit

interface SettingsRepository : Repository, Synchronizable {

    val confidentiality : Confidentiality

    fun addOnConfidentialitySettingsChangedListener(
        listener : SettingsChangeListener<Confidentiality>
    )

    fun removeOnConfidentialitySettingsChangedListener(
        listener : SettingsChangeListener<Confidentiality>
    )

    interface Confidentiality {

        var blackList                           : List<String>
        var phoneAccess                         : Byte
        var activityAccess                      : Byte
        var callsAccess                         : Byte
        var groupInviteAccess                   : Byte
        var isContactsSynchronizationEnabled    : Boolean

        companion object{
            const val ACCESS_CONTACTS : Byte = 0
            const val ACCESS_ALL : Byte = 1
            const val ACCESS_NOONE : Byte = -1
        }
    }
}

