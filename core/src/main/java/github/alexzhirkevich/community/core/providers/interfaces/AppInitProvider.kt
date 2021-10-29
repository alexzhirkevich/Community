package github.alexzhirkevich.community.core.providers.interfaces

import android.content.Context
import github.alexzhirkevich.community.core.providers.base.IndependentProvider
import github.alexzhirkevich.community.core.providers.base.Provider

interface AppInitProvider : Provider  {

    fun init(context : Context)
}