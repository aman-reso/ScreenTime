package com.app.screentime.feature.wallet.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

sealed interface BillingPurchaseResult {
    data class Success(
        val purchase: Purchase,
        val productId: String,
        val purchaseToken: String,
        val orderId: String?
    ) : BillingPurchaseResult

    data class Failed(
        val responseCode: Int,
        val message: String
    ) : BillingPurchaseResult

    data object Cancelled : BillingPurchaseResult
}

sealed interface BillingConnectionState {
    data object Disconnected : BillingConnectionState
    data object Connecting : BillingConnectionState
    data object Connected : BillingConnectionState
    data class Error(val message: String) : BillingConnectionState
}

@Singleton
class GooglePlayBillingManager @Inject constructor(
    @ApplicationContext private val context: Context
) : PurchasesUpdatedListener {

    private val tag = "GooglePlayBilling"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _connectionState = MutableStateFlow<BillingConnectionState>(BillingConnectionState.Disconnected)
    val connectionState: StateFlow<BillingConnectionState> = _connectionState.asStateFlow()

    private val _productDetailsMap = MutableStateFlow<Map<String, ProductDetails>>(emptyMap())
    val productDetailsMap: StateFlow<Map<String, ProductDetails>> = _productDetailsMap.asStateFlow()

    private val _purchaseEvents = MutableSharedFlow<BillingPurchaseResult>(extraBufferCapacity = 10)
    val purchaseEvents: SharedFlow<BillingPurchaseResult> = _purchaseEvents.asSharedFlow()

    private var billingClient: BillingClient = createBillingClient()
    private var isConnecting = false
    private var reconnectAttempts = 0

    init {
        startConnection()
    }

    private fun createBillingClient(): BillingClient {
        val pendingParams = PendingPurchasesParams.newBuilder()
            .enableOneTimeProducts()
            .build()

        return BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases(pendingParams)
            .enableAutoServiceReconnection()
            .build()
    }

    fun startConnection(onConnected: (() -> Unit)? = null) {
        if (billingClient.isReady) {
            _connectionState.value = BillingConnectionState.Connected
            onConnected?.invoke()
            return
        }

        if (isConnecting) return
        isConnecting = true
        _connectionState.value = BillingConnectionState.Connecting

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                isConnecting = false
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.i(tag, "Google Play Billing setup successful")
                    _connectionState.value = BillingConnectionState.Connected
                    reconnectAttempts = 0
                    onConnected?.invoke()
                } else {
                    Log.e(tag, "Billing setup failed with code: ${billingResult.responseCode}, debugMessage: ${billingResult.debugMessage}")
                    _connectionState.value = BillingConnectionState.Error(billingResult.debugMessage.ifBlank { "Billing setup failed" })
                    retryConnection()
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnecting = false
                Log.w(tag, "Billing service disconnected")
                _connectionState.value = BillingConnectionState.Disconnected
                retryConnection()
            }
        })
    }

    private fun retryConnection() {
        if (reconnectAttempts < 3) {
            reconnectAttempts++
            val backoffMillis = (1000L * (1 shl reconnectAttempts)).coerceAtMost(10_000L)
            scope.launch {
                delay(backoffMillis)
                startConnection()
            }
        }
    }

    suspend fun queryProducts(productIds: List<String>): Map<String, ProductDetails> {
        if (productIds.isEmpty()) return emptyMap()

        if (!billingClient.isReady) {
            startConnection()
            // Wait up to 3 seconds for connection
            for (i in 1..30) {
                if (billingClient.isReady) break
                delay(100)
            }
            if (!billingClient.isReady) {
                Log.e(tag, "Cannot query products: BillingClient is not ready")
                return _productDetailsMap.value
            }
        }

        val inappProductList = productIds.distinct().map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        val subsProductList = productIds.distinct().map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        val updatedMap = _productDetailsMap.value.toMutableMap()

        try {
            val inappResult = billingClient.queryProductDetails(
                QueryProductDetailsParams.newBuilder().setProductList(inappProductList).build()
            )
            if (inappResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                inappResult.productDetailsList?.forEach { detail ->
                    updatedMap[detail.productId] = detail
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Exception querying in-app product details: ${e.message}", e)
        }

        try {
            val subsResult = billingClient.queryProductDetails(
                QueryProductDetailsParams.newBuilder().setProductList(subsProductList).build()
            )
            if (subsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                subsResult.productDetailsList?.forEach { detail ->
                    updatedMap[detail.productId] = detail
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Exception querying subscription product details: ${e.message}", e)
        }

        _productDetailsMap.value = updatedMap
        Log.i(tag, "Loaded products from Google Play: ${updatedMap.keys}")
        return updatedMap
    }

    fun launchBillingFlow(
        activity: Activity,
        productId: String,
        onLaunchFailed: (String) -> Unit = {}
    ) {
        if (!billingClient.isReady) {
            startConnection {
                launchBillingFlowInternal(activity, productId, onLaunchFailed)
            }
        } else {
            launchBillingFlowInternal(activity, productId, onLaunchFailed)
        }
    }

    private fun launchBillingFlowInternal(
        activity: Activity,
        productId: String,
        onLaunchFailed: (String) -> Unit
    ) {
        val details = _productDetailsMap.value[productId]
        if (details == null) {
            // ProductDetails not yet cached; query on-the-fly and then launch
            scope.launch {
                val queried = queryProducts(listOf(productId))
                val found = queried[productId]
                if (found != null) {
                    launchWithProductDetails(activity, found, onLaunchFailed)
                } else {
                    val msg = "Product '$productId' is not configured in Google Play Console yet."
                    Log.e(tag, msg)
                    _purchaseEvents.emit(BillingPurchaseResult.Failed(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE, msg))
                    onLaunchFailed(msg)
                }
            }
            return
        }

        launchWithProductDetails(activity, details, onLaunchFailed)
    }

    private fun launchWithProductDetails(
        activity: Activity,
        productDetails: ProductDetails,
        onLaunchFailed: (String) -> Unit
    ) {
        val productDetailsParamsBuilder = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)

        if (productDetails.productType == BillingClient.ProductType.SUBS) {
            val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
            if (!offerToken.isNullOrBlank()) {
                productDetailsParamsBuilder.setOfferToken(offerToken)
            }
        }

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParamsBuilder.build()))
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, flowParams)
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            val errorMsg = billingResult.debugMessage.ifBlank { "Failed to launch Google Play purchase flow" }
            Log.e(tag, "launchBillingFlow failed: ${billingResult.responseCode} - $errorMsg")
            onLaunchFailed(errorMsg)
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases.isNullOrEmpty()) {
                    Log.w(tag, "onPurchasesUpdated OK but purchases list is empty")
                    return
                }
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.i(tag, "User cancelled Google Play purchase")
                _purchaseEvents.tryEmit(BillingPurchaseResult.Cancelled)
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Log.w(tag, "Item already owned. Retrying verification...")
                purchases?.forEach { handlePurchase(it) }
            }
            else -> {
                val errorMsg = billingResult.debugMessage.ifBlank { "Google Play purchase failed (code ${billingResult.responseCode})" }
                Log.e(tag, "onPurchasesUpdated error: ${billingResult.responseCode} - $errorMsg")
                _purchaseEvents.tryEmit(BillingPurchaseResult.Failed(billingResult.responseCode, errorMsg))
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            val productId = purchase.products.firstOrNull().orEmpty()
            Log.i(tag, "Purchase successful: product=$productId, orderId=${purchase.orderId}, token=${purchase.purchaseToken.take(12)}...")

            // Server-driven billing: Do not consume on Android.
            // The backend server (/api/billing/google/verify) consumes and acknowledges the purchase safely.
            scope.launch {
                _purchaseEvents.emit(
                    BillingPurchaseResult.Success(
                        purchase = purchase,
                        productId = productId,
                        purchaseToken = purchase.purchaseToken,
                        orderId = purchase.orderId
                    )
                )
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            Log.i(tag, "Purchase is pending user payment completion (offline/UPI pending)")
            _purchaseEvents.tryEmit(BillingPurchaseResult.Failed(
                responseCode = BillingClient.BillingResponseCode.OK,
                message = "Payment is pending. Your coins will be credited once payment completes."
            ))
        }
    }

    suspend fun consumePurchase(purchaseToken: String): Boolean {
        if (!billingClient.isReady) return false
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        return try {
            val result = billingClient.consumePurchase(consumeParams)
            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.i(tag, "Successfully consumed purchase token: ${purchaseToken.take(12)}...")
                true
            } else {
                Log.w(tag, "Failed to consume purchase token: ${result.billingResult.debugMessage}")
                false
            }
        } catch (e: Exception) {
            Log.e(tag, "Exception consuming purchase: ${e.message}", e)
            false
        }
    }
}
