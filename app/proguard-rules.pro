# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
##-------------------------RXBUS -------------------------------
-keepnames class com.hwangjr.rxbus.thread.**{*;}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.hwangjr.rxbus.annotation.Subscribe public *;
    @com.hwangjr.rxbus.annotation.Produce public *;
}
-dontwarn com.hwangir.rxbus.**
##---------------------------------------------------



##---------------Begin: proguard configuration for realm----------
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**
##---------------end proguard configuration for realm----------

##---------------Begin: proguard configuration for butterknife----------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
##---------------end proguard configuration for butterknife----------


##-----------------------Rx java--------------------------------------------------
-dontwarn sun.misc.**
-keep class rx.internal.util.unsafe.** { *; }

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

##---------------Begin:proguard configuration for lombok --------------
-dontwarn javax.**
-dontwarn lombok.**
-dontwarn org.apache.**
-dontwarn com.squareup.**
-dontwarn com.sun.**
-dontwarn **retrofit**
##---------------End: proguard confiuration for lombok ----------------


#========================gson================================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

#=================== umeng ========================
-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class [您的应用包名].R$*{
public static final int *;
}