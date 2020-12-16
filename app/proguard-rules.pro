# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wangxiaokun/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#Tencent map sdk
-keep class com.tencent.mapsdk.**{*;}
-keep class com.tencent.tencentmap.**{*;}

#Tencent locate sdk
-keepattributes *Annotation*
-keepclassmembers class ** {
    public void on*Event(...);
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-dontwarn  org.eclipse.jdt.annotation.**


-dontnote ct.**


# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

-dontwarn org.eclipse.jdt.annotation.**

-dontwarn android.location.Location

-dontwarn com.tencent.**
-dontnote ct.**
