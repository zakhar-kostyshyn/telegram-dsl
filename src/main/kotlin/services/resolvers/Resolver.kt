package services.resolvers

import models.Update

interface Resolver {
    fun resolve(update: Update)
}
