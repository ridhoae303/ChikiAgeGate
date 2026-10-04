-dontoptimize
-dontpreverify
-dontusemixedcaseclassnames

-keepattributes Signature,InnerClasses,EnclosingMethod,*Annotation*

-keep class com.ridhoae303.app.main.MainActivity {
    *;
}

-keep class com.chiki.makigate.ChikiVerification {
    *;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn com.chiki.makigate.ChikiVerification
-dontwarn com.google.vending.licensing.ILicensingService
-dontwarn com.android.vending.licensing.ILicensingService
-dontwarn android.support.annotation.Keep
-dontwarn androidx.annotation.Keep

-renamesourcefileattribute SourceFile