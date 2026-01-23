# Hardcoded Strings Report

## Critical User-Facing Strings (Should be localized)

### 1. **RegistrationViewModel.kt** (Lines 29-35)
**Priority: HIGH**
```kotlin
title = "Register Device",
subtitle = "Connecting to server...",
title = "Grant Permissions",
subtitle = "Allow app usage access",
```
**Suggested String Resources:**
- `register_device`
- `connecting_to_server`
- `grant_permissions`
- `allow_app_usage_access`

---

### 2. **RewardInfoBottomSheet.kt** (Lines 274-438)
**Priority: HIGH**
```kotlin
text = "Price: ${reward.coinPrice} coins",
text = "Stock: ${reward.stockQuantity} available",
text = "Out of stock",
text = "Category: ${reward.category}",
text = "Claim Reward",
label = "Name",
label = "Email",
label = "Phone Number",
label = "UPI ID",
label = "Address",
label = "Postal Code",
label = "Save details for future reference",
```
**Suggested String Resources:**
- `reward_price_format` (with placeholder)
- `stock_available_format` (with placeholder)
- `out_of_stock`
- `category_format` (with placeholder)
- `claim_reward`
- `name`
- `email`
- `phone_number`
- `upi_id`
- `address`
- `postal_code`
- `save_details_for_future`

---

### 3. **SingleAppUsageDetailScreen.kt** (Lines 656-700)
**Priority: HIGH**
```kotlin
text = "Quick actions",
text = "Launch",
text = "Set timer",
text = "Block",
text = "Settings",
text = "Recover Notification",
```
**Suggested String Resources:**
- `quick_actions`
- `launch`
- `set_timer`
- `block`
- `settings`
- `recover_notification`

---

### 4. **ChallengeDetailUseCase.kt** (Lines 66-86)
**Priority: HIGH**
```kotlin
title = "Premium Subscription",
description = "Get 1 month free premium subscription",
title = "Cash Reward",
description = "Win ₹500 cash prize",
title = "Gift Card",
description = "Amazon gift card worth ₹1000",
```
**Suggested String Resources:**
- `reward_premium_subscription`
- `reward_premium_subscription_desc`
- `reward_cash_reward`
- `reward_cash_reward_desc`
- `reward_gift_card`
- `reward_gift_card_desc`

---

### 5. **ProfileScreen.kt** (Line 274)
**Priority: MEDIUM**
```kotlin
text = "& Crafted in Patna",
```
**Suggested String Resource:**
- `crafted_in_patna`

---

### 6. **ActivePlanCard.kt** (Lines 122-127)
**Priority: MEDIUM**
```kotlin
text = "Total Visits",
text = "$currentVisits/$totalVisits",
```
**Suggested String Resources:**
- `total_visits`
- `visits_format` (with placeholders)

---

### 7. **ActivePlansSection.kt** (Lines 40-49)
**Priority: MEDIUM**
```kotlin
text = "My Active Plans",
text = "You have $planCount active plans",
```
**Suggested String Resources:**
- `my_active_plans`
- `active_plans_count_format` (with placeholder)

---

### 8. **RewardPointsHeader.kt** (Lines 49-87)
**Priority: MEDIUM**
```kotlin
text = "My Rewards Points",
text = "Earned points",
text = "Level $level",
```
**Suggested String Resources:**
- `my_rewards_points`
- `earned_points`
- `level_format` (with placeholder)

---

### 9. **ControlCenterScreen.kt** (Lines 366, 452)
**Priority: HIGH**
```kotlin
bodyText = "Add a person who can view your AppTime and location. You can manage access anytime."
text = "Extend access duration or revoke access",
```
**Suggested String Resources:**
- `control_center_add_person_description`
- `control_center_extend_or_revoke_access`

---

### 10. **RewardTransactionScreen.kt** (Lines 154, 234)
**Priority: MEDIUM**
```kotlin
text = "Transaction History",
text = "No transactions found",
```
**Suggested String Resources:**
- `transaction_history`
- `no_transactions_found`

---

### 11. **ExpiringPointsBanner.kt** (Line 56)
**Priority: HIGH**
```kotlin
labelText = "$expiringPoints coins will expire soon, please use as soon as possible."
```
**Suggested String Resource:**
- `expiring_coins_warning` (with placeholder)

---

### 12. **RewardTransactionItem.kt** (Line 81)
**Priority: MEDIUM**
```kotlin
text = "-${transaction.coinPrice} coins",
```
**Suggested String Resource:**
- `coins_spent_format` (with placeholder)

---

### 13. **ProfileInformationSection.kt** (Lines 47-48)
**Priority: MEDIUM**
```kotlin
descriptionText = "View and manage your two-factor authentication code",
descriptionTitle = "Security",
```
**Suggested String Resources:**
- `two_factor_auth_description`
- `security`

---

### 14. **Challenge Variant Cards** (Multiple files)
**Priority: LOW** (These appear to be preview/demo data)
```kotlin
title = "FAT BURNING HIIT",
title = "FULL BODY WORKOUT",
title = "Movies",
description = "From timeless classics to the latest releases...",
```
**Note:** These seem to be preview/demo data and may not need localization.

---

## Content Descriptions (Accessibility - Lower Priority)
These are for accessibility and could also be localized:
- "Back", "Close", "Share", "Edit", "View details", "Challenge icon", "Participants", etc.

---

## Recommendation Priority:
1. **HIGH Priority** - User-facing text that affects core functionality
2. **MEDIUM Priority** - User-facing text in secondary features
3. **LOW Priority** - Demo/preview data, content descriptions

## Total Hardcoded Strings Found: ~146 instances
## Critical User-Facing Strings: ~50+ instances
