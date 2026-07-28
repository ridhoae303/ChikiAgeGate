-dontoptimize
-dontpreverify
-dontusemixedcaseclassnames
-keepattributes Signature,InnerClasses,EnclosingMethod,*Annotation*
-keep class com.ridhoae303.app.main.MainActivity {
    public <init>();
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
-renamesourcefileattribute SourceFile
