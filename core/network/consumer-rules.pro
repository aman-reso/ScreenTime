# Native RegisterNatives looks up this class and method names at load time.
-keep class com.app.screentime.core.network.security.NativeSecurityVault {
    native <methods>;
}
