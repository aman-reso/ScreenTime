# Architecture and Design System Rules

## 1. Telekom ODS UI Components (Mandatory)
- **100% ODS Components**: All user interface elements MUST use Telekom ODS components (`com.telekom.odsystem.*`):
  - Containers: `ODSBox`, `ODSColumn`, `ODSRow`
  - Lists: `ODSLazyColumn` (Never use `Modifier.verticalScroll()` or Jetpack Compose `LazyColumn`)
  - Typography: `ODSText` with `ODSTextStyles` (Maximum text size is 16sp: `bodyMBold`, `bodyMRegular`, `bodySBold`, `bodySRegular`, `microcopyBold`, `microcopyRegular`). Never use `title*`, `subtitle*`, `bodyL*`, or `display*`.
  - Media & Controls: `ODSImage`, `ODSIcon`, `ODSButton`, `ODSLoadingSpinner`, `ODSDialog`
  - **Strict prohibition**: Never use raw Jetpack Compose `Box`, `Column`, `Row`, `Text`, `Icon`, `Button`, or `LazyColumn`.
- **Color Tokens**:
  - Always derive colors from the composable `scheme: ODSTheme` parameter (e.g. `scheme.basicBackground`, `scheme.basicAccent`, `scheme.basicTextDominant`, `scheme.basicTextRecessive`, `scheme.basicStroke`).
  - Never use hardcoded hex values in composables when `scheme` is available.
  - Wrap background/border colors with `ODSColorModel(hexColor = scheme.xxx)`. For `ODSIcon` tint use `scheme.xxx.getColor()`.
- **Spacing & Radii**:
  - Always use `ODSVariables` tokens for spacing, padding, and corner radii (e.g. `ODSVariables.spacingComponent4`, `ODSVariables.spacingLayout1`, `ODSVariables.radiusMedium`, `ODSVariables.strokes1`). Never hardcode raw dp values for standard spacing.

## 2. Clean MVVM Architecture: ViewModel, UseCase, Repository (Mandatory)
- **Layered Structure**:
  1. **UI Layer (`Composable`)**: Only consumes `StateFlow<UiState>` from `ViewModel` and dispatches UI events.
  2. **ViewModel Layer (`ViewModel`)**: Manages UI state, handles user interaction, and invokes domain UseCases. Never interacts with data sources or network APIs directly.
  3. **Domain Layer (`UseCase` & `Repository Interface`)**:
     - Single-responsibility UseCases (e.g., `FetchUserProfileUseCase`, `UpdateProfileUseCase`, `DeleteAccountUseCase`).
     - Repository interfaces define domain contracts (`ProfileRepository`).
     - UseCases call Repositories; UseCases do not directly call network clients or databases.
  4. **Data Layer (`Repository Implementation` & `Data Sources`)**:
     - `ProfileRepositoryImpl` implements `ProfileRepository`.
     - Coordinates remote API (`WinterApi`, `DiscoveryApi`) and local storage (`PreferencesManager`, `SessionManager`).
- **Dependency Injection**:
  - Use Dagger Hilt.
  - Bind repository interfaces to implementations via `@Binds` in Hilt modules (`@InstallIn(SingletonComponent::class)` or `@InstallIn(ViewModelComponent::class)`).

## 3. Product & Design Fidelity
- Strictly follow provided UI mockups and specifications.
- Match existing design patterns, typography hierarchy, and navigation flows.
