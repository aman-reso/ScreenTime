package com.app.screentime.core.network.api

import com.app.screentime.core.network.ApiEndpoints
import com.app.screentime.core.network.NetworkClient
import com.app.screentime.core.network.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectApi @Inject constructor(
    private val networkClient: NetworkClient
) {
    val baseUrl: String
        get() = ApiEndpoints.getBaseUrl()

    private val httpClient: HttpClient
        get() = networkClient.httpClient

    // ── 1. Auth ───────────────────────────────────────────────────────────────

    suspend fun requestOtp(phone: String? = null, email: String? = null): ConnectOTPResponse {
        val res: ApiResponse<ConnectOTPResponse> = httpClient.post("$baseUrl${ApiEndpoints.ConnectAuth.OTP_REQUEST}") {
            setBody(ConnectOTPRequest(phone = phone, email = email))
        }.body()
        return res.data ?: ConnectOTPResponse(mock_code = "123456", message = res.message)
    }

    suspend fun verifyOtp(phone: String? = null, email: String? = null, code: String, name: String? = null): ConnectAuthResponse {
        val res: ApiResponse<ConnectAuthResponse> = httpClient.post("$baseUrl${ApiEndpoints.ConnectAuth.OTP_VERIFY}") {
            setBody(ConnectOTPVerifyRequest(phone = phone, email = email, code = code, name = name))
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "OTP verification failed" })
    }

    suspend fun getMe(token: String): TenantDto {
        val res: ApiResponse<TenantDto> = httpClient.get("$baseUrl${ApiEndpoints.ConnectAuth.ME}") {
            bearerAuth(token)
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Failed to get user profile" })
    }

    // ── 2. Templates ─────────────────────────────────────────────────────────

    suspend fun getTemplates(category: String? = null): List<TemplateDto> {
        val url = if (category.isNullOrBlank()) {
            "$baseUrl${ApiEndpoints.ConnectTemplates.LIST}"
        } else {
            "$baseUrl${ApiEndpoints.ConnectTemplates.LIST}?category=$category"
        }
        val res: ApiResponse<List<TemplateDto>> = httpClient.get(url).body()
        return res.data ?: emptyList()
    }

    suspend fun getTemplateDetail(templateId: String): TemplateDetailResponse {
        val res: ApiResponse<TemplateDetailResponse> = httpClient.get("$baseUrl${ApiEndpoints.ConnectTemplates.DETAIL}$templateId").body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Template not found" })
    }

    // ── 3. Sites ─────────────────────────────────────────────────────────────

    suspend fun checkSlug(slug: String, excludeId: String? = null): CheckSlugResponse {
        val url = if (excludeId.isNullOrBlank()) {
            "$baseUrl${ApiEndpoints.ConnectSites.CHECK_SLUG}?slug=$slug"
        } else {
            "$baseUrl${ApiEndpoints.ConnectSites.CHECK_SLUG}?slug=$slug&exclude_id=$excludeId"
        }
        val res: ApiResponse<CheckSlugResponse> = httpClient.get(url).body()
        return res.data ?: CheckSlugResponse(slug = slug, available = false, message = res.message)
    }

    suspend fun getSites(token: String): List<UserSiteSummaryDto> {
        val res: ApiResponse<List<UserSiteSummaryDto>> = httpClient.get("$baseUrl${ApiEndpoints.ConnectSites.BASE}") {
            bearerAuth(token)
        }.body()
        return res.data ?: emptyList()
    }

    suspend fun createSite(token: String, req: CreateSiteRequestDto): CreateSiteResponseDto {
        val res: ApiResponse<CreateSiteResponseDto> = httpClient.post("$baseUrl${ApiEndpoints.ConnectSites.BASE}") {
            bearerAuth(token)
            setBody(req)
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Failed to create site" })
    }

    suspend fun getSite(token: String, siteId: String): SiteDetailResponseDto {
        val res: ApiResponse<SiteDetailResponseDto> = httpClient.get("$baseUrl${ApiEndpoints.ConnectSites.detail(siteId)}") {
            bearerAuth(token)
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Site not found" })
    }

    suspend fun updateSite(token: String, siteId: String, req: UpdateSiteRequestDto): Boolean {
        val res = httpClient.put("$baseUrl${ApiEndpoints.ConnectSites.detail(siteId)}") {
            bearerAuth(token)
            setBody(req)
        }
        return res.status.isSuccess()
    }

    suspend fun deleteSite(token: String, siteId: String): Boolean {
        val res = httpClient.delete("$baseUrl${ApiEndpoints.ConnectSites.detail(siteId)}") {
            bearerAuth(token)
        }
        return res.status.isSuccess()
    }

    suspend fun publishSite(token: String, siteId: String): Boolean {
        val res = httpClient.post("$baseUrl${ApiEndpoints.ConnectSites.publish(siteId)}") {
            bearerAuth(token)
        }
        return res.status.isSuccess()
    }

    suspend fun unpublishSite(token: String, siteId: String): Boolean {
        val res = httpClient.post("$baseUrl${ApiEndpoints.ConnectSites.unpublish(siteId)}") {
            bearerAuth(token)
        }
        return res.status.isSuccess()
    }

    suspend fun getSiteAnalytics(token: String, siteId: String): AnalyticsSummaryDto {
        val res: ApiResponse<AnalyticsSummaryDto> = httpClient.get("$baseUrl${ApiEndpoints.ConnectSites.analytics(siteId)}") {
            bearerAuth(token)
        }.body()
        return res.data ?: AnalyticsSummaryDto(site_id = siteId)
    }

    suspend fun toggleSiteAds(token: String, siteId: String, enabled: Boolean): Boolean {
        val res = httpClient.post("$baseUrl${ApiEndpoints.ConnectSites.toggleAds(siteId)}") {
            bearerAuth(token)
            setBody(mapOf("enabled" to enabled))
        }
        return res.status.isSuccess()
    }

    suspend fun getSiteEarnings(token: String, siteId: String): List<AdEarningsLedgerDto> {
        val res: ApiResponse<List<AdEarningsLedgerDto>> = httpClient.get("$baseUrl${ApiEndpoints.ConnectSites.earnings(siteId)}") {
            bearerAuth(token)
        }.body()
        return res.data ?: emptyList()
    }

    // ── 4. Voice Studio ──────────────────────────────────────────────────────

    suspend fun startVoiceSession(token: String, transcript: String, category: String? = null, templateId: String? = null): VoiceSessionResponseDto {
        val res: ApiResponse<VoiceSessionResponseDto> = httpClient.post("$baseUrl${ApiEndpoints.ConnectVoice.SESSION}") {
            bearerAuth(token)
            setBody(VoiceSessionStartRequestDto(transcript = transcript, category = category, template_id = templateId))
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Voice extraction failed" })
    }

    suspend fun patchVoiceSession(token: String, sessionId: String, transcript: String): VoiceSessionResponseDto {
        val res: ApiResponse<VoiceSessionResponseDto> = httpClient.post("$baseUrl${ApiEndpoints.ConnectVoice.PATCH}") {
            bearerAuth(token)
            setBody(VoiceSessionPatchRequestDto(session_id = sessionId, transcript = transcript))
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Voice patch failed" })
    }

    // ── 5. AI Site Studio ────────────────────────────────────────────────────

    suspend fun parseAIInstruction(instruction: String, preferredCategory: String? = null, preferredTemplateId: String? = null): AIParseResponseDto {
        val res: ApiResponse<AIParseResponseDto> = httpClient.post("$baseUrl${ApiEndpoints.ConnectAI.PARSE}") {
            setBody(AIParseRequestDto(instruction = instruction, preferred_category = preferredCategory, preferred_template_id = preferredTemplateId))
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "AI instruction parsing failed" })
    }

    suspend fun generateAndCreateSite(token: String? = null, req: AIGenerateAndCreateRequestDto): AIGenerateAndCreateResponseDto {
        val res: ApiResponse<AIGenerateAndCreateResponseDto> = httpClient.post("$baseUrl${ApiEndpoints.ConnectAI.GENERATE_AND_CREATE}") {
            if (!token.isNullOrBlank()) {
                bearerAuth(token)
            }
            setBody(req)
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "AI site generation failed" })
    }

    // ── 6. Catalog ───────────────────────────────────────────────────────────

    suspend fun getCatalogItems(siteId: String): List<CatalogItemDto> {
        val res: ApiResponse<List<CatalogItemDto>> = httpClient.get("$baseUrl${ApiEndpoints.ConnectSites.catalog(siteId)}").body()
        return res.data ?: emptyList()
    }

    suspend fun addCatalogItem(token: String, siteId: String, req: CreateCatalogItemRequestDto): CatalogItemDto {
        val res: ApiResponse<CatalogItemDto> = httpClient.post("$baseUrl${ApiEndpoints.ConnectSites.catalog(siteId)}") {
            bearerAuth(token)
            setBody(req)
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Failed to add catalog item" })
    }

    suspend fun deleteCatalogItem(token: String, siteId: String, itemId: String): Boolean {
        val res = httpClient.delete("$baseUrl${ApiEndpoints.ConnectSites.catalogItem(siteId, itemId)}") {
            bearerAuth(token)
        }
        return res.status.isSuccess()
    }

    // ── 7. Monetization & Payouts ────────────────────────────────────────────

    suspend fun getPayouts(token: String): List<PayoutDto> {
        val res: ApiResponse<List<PayoutDto>> = httpClient.get("$baseUrl${ApiEndpoints.ConnectMonetization.PAYOUTS}") {
            bearerAuth(token)
        }.body()
        return res.data ?: emptyList()
    }

    suspend fun requestPayout(token: String, req: RequestPayoutRequestDto): PayoutDto {
        val res: ApiResponse<PayoutDto> = httpClient.post("$baseUrl${ApiEndpoints.ConnectMonetization.PAYOUTS}") {
            bearerAuth(token)
            setBody(req)
        }.body()
        return res.data ?: throw Exception(res.error ?: res.message.ifBlank { "Failed to request payout" })
    }

    suspend fun simulateAdRevenue(siteId: String, amount: Double = 1500.0): SimulateAdRevenueResponseDto {
        val res: ApiResponse<SimulateAdRevenueResponseDto> = httpClient.post("$baseUrl${ApiEndpoints.ConnectMonetization.SIMULATE_AD_REVENUE}?site_id=$siteId&amount=$amount").body()
        return res.data ?: SimulateAdRevenueResponseDto(site_id = siteId, gross_amount = amount, creator_credited = amount * 0.70)
    }
}
