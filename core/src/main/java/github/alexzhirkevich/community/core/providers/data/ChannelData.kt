package github.alexzhirkevich.community.core.providers.data

import github.alexzhirkevich.community.core.entities.imp.Admin

data class ChannelData(
    override val id: String,
    val subscribers : Long,
    val admins : Collection<Admin>?
) : EntityData