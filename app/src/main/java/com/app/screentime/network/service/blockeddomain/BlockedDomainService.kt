package com.app.screentime.network.service.blockeddomain

import com.app.screentime.network.model.*

/**
 * Service interface for Blocked Domain related API operations
 */
interface BlockedDomainService {
    suspend fun getBlockedDomains(): Result<ApiResponse<GetBlockedDomainsResponse>>
    suspend fun getDomainGroups(): Result<ApiResponse<List<BlockedDomainGroup>>>
    suspend fun submitBlockedDomain(request: SubmitBlockedDomainRequest): Result<ApiResponse<SubmitBlockedDomainResponse>>
    suspend fun updateBlockedDomain(id: Int, request: SubmitBlockedDomainRequest): Result<ApiResponse<SubmitBlockedDomainResponse>>
    suspend fun deleteBlockedDomain(id: Int): Result<ApiResponse<Unit>>
}

