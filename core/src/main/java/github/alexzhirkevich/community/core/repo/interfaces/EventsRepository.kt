package github.alexzhirkevich.community.core.repo.interfaces

import github.alexzhirkevich.community.core.entities.interfaces.IEvent
import github.alexzhirkevich.community.core.repo.base.EntityRepository
import github.alexzhirkevich.community.core.repo.base.IndependentRepository
import github.alexzhirkevich.community.core.repo.base.RangeEntityRepository
import github.alexzhirkevich.community.core.repo.base.Removable

interface EventsRepository
    : EntityRepository<IEvent>,
    RangeEntityRepository<IEvent>,
    Removable, IndependentRepository
