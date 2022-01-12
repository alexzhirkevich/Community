package github.alexzhirkevich.community.core.repo.interfaces

import android.content.Context
import github.alexzhirkevich.community.core.repo.base.Repository

interface AppInitRepository : Repository  {

    fun init(context : Context)
}