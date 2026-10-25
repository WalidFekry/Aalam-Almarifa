# ProGuard / R8 Optimization Rules for Aalam-Almarifa

# Preserve line numbers and source files for crash reports
-keepattributes SourceFile,LineNumberTable,Signature,InnerClasses,EnclosingMethod,*Annotation*
-renamesourcefileattribute SourceFile

# Keep Activity classes so dynamic FCM intent Class.forName() works
-keep public class com.walid.knowledgeworld.** extends android.app.Activity

# Keep JavaScript Interface methods used in VideoEnabledWebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep Lottie Animation Views
-keep class com.airbnb.lottie.LottieAnimationView { *; }

# Keep PrettyDialog
-keep class libs.mjn.prettydialog.PrettyDialog { *; }

# Keep Material Icon Lib
-keep class net.steamcrafted.materialiconlib.MaterialIconView { *; }

# Suppress OkHttp build warnings
-dontwarn okhttp3.**
-dontwarn okio.**
