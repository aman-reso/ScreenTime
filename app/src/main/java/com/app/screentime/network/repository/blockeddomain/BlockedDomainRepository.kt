package com.app.screentime.network.repository.blockeddomain

import com.app.screentime.core.network.model.ApiResponse
import com.app.screentime.network.model.*
import com.app.screentime.network.service.blockeddomain.BlockedDomainService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Blocked Domain operations
 */
@Singleton
class BlockedDomainRepository @Inject constructor(
    private val blockedDomainService: BlockedDomainService
)

