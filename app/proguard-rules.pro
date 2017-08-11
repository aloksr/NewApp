-dontwarn org.apache.commons.**
-dontwarn com.google.**
-dontwarn com.j256.ormlite**
-dontwarn org.apache.http**
-dontwarn com.amazonaws.**
-dontwarn com.fasterxml.**
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.okhttp.**

-keep class com.amazonaws.** { *; }
-keep class cn.pedant.** { *; }
-keepnames class com.amazonaws.** { *; }

-keep class android.support.v7.widget.** { *; }
-keep interface android.support.v7.widget.** { *; }
-dontwarn org.xmlpull.v1.**
-dontwarn com.squareup.**
-keep class com.squareup.** { *; }

-keepattributes SourceFile,LineNumberTable
-keepattributes Signature,InnerClasses
-keep class com.parse.*{ *; }
-dontwarn com.parse.**

-keepclasseswithmembernames class * {
    native <methods>;
}


-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-keepattributes Signature
# GSON Library
# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Google Map
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }
-keep class com.google.android.gms.** { *; }
