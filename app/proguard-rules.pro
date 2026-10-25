# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep JavaScript Interface methods used in VideoEnabledWebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Preserve LineNumberTable for crash reporting
-keepattributes SourceFile,LineNumberTable
