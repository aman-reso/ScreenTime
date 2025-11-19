package com.app.screentime.network.repository.blockeddomain

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
) {
    suspend fun getBlockedDomains(): Result<ApiResponse<GetBlockedDomainsResponse>> {
        return blockedDomainService.getBlockedDomains()
    }

    suspend fun getDomainGroups(): Result<ApiResponse<List<BlockedDomainGroup>>> {
        return blockedDomainService.getDomainGroups()
    }

    suspend fun submitBlockedDomain(request: SubmitBlockedDomainRequest): Result<ApiResponse<SubmitBlockedDomainResponse>> {
        return blockedDomainService.submitBlockedDomain(request)
    }

    suspend fun updateBlockedDomain(id: Int, request: SubmitBlockedDomainRequest): Result<ApiResponse<SubmitBlockedDomainResponse>> {
        return blockedDomainService.updateBlockedDomain(id, request)
    }

    suspend fun deleteBlockedDomain(id: Int): Result<ApiResponse<Unit>> {
        return blockedDomainService.deleteBlockedDomain(id)
    }
}

