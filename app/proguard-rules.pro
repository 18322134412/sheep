# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in Z:\Users\nEdAy\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

    -keepattributes InnerClasses,Deprecated,SourceFile,LineNumberTable,EnclosingMethod
    #指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*

    #忽略警告
    -ignorewarning

    -keepattributes Signature
    -keepattributes Exceptions

    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment
    #如果引用了v4或者v7包
    -keep class android.support.** { *; }
    -dontwarn android.support.**

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService

    ##记录生成的日志数据,gradle build时在本项目根目录输出##

    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    ########记录生成的日志数据，gradle build时 在本项目根目录输出-end######


    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }
    -keep class **.R$* {*;}
    -keep class **.R{*;}
    -dontwarn **.R$*
    -keepclassmembers class * {
       public <init> (org.json.JSONObject);
    }
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
################混淆保护自己项目的部分代码以及引用的第三方jar包################

    -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }

    -keep class c.b.** { *; }

    -dontwarn bolts.**
    -keep class bolts.** { *; }

    -dontwarn com.facebook.**
    -keep class com.facebook.** { *; }

    -dontwarn com.google.gson.**
    -keep class com.google.gson.** { *; }

    -dontwarn org.eclipse.mat.**
    -keep class org.eclipse.mat.** { *; }

    -dontwarn com.squareup.**
    -keep class com.squareup.** { *;}
    -keep interface com.squareup.** { *; }

    -dontwarn com.nineoldandroids.**
    -keep class com.nineoldandroids.** { *; }

    -dontwarn okhttp3.**
    -keep class okhttp3.** { *; }

    -dontwarn okio.**
    -keep class okio.** { *; }

    -dontwarn rx.**
    -keep class rx.** { *; }

    -dontwarn cn.smssdk.**
    -keep class cn.smssdk.** { *; }

    -dontwarn com.bigkoo.svprogresshud.**
    -keep class com.bigkoo.svprogresshud.** { *; }

    -dontwarn com.readystatesoftware.systembartint.**
    -keep class com.readystatesoftware.systembartint.** { *; }

    -dontwarn in.srain.cube.views.ptr.**
    -keep class in.srain.cube.views.ptr.** { *; }

    -keep class com.xpple.sheep.view.**{*;}
    -keep class com.xpple.sheep.bean.**{*;}

    -keep class cn.sharesdk.**{*;}
    -dontwarn cn.sharesdk.**
    -keep class com.sina.**{*;}

    -keep class sun.misc.Unsafe { *; }
    -keep class com.taobao.** {*;}
    -keep class com.alibaba.** {*;}
    -keepclassmembers class com.alibaba.** {
        *;
    }
    -keepclassmembers class com.taobao.** {
        *;
    }
    -keep class com.alipay.** {*;}
    -dontwarn com.alipay.**
    -keep class com.ut.** {*;}
    -dontwarn com.ut.**
    -keep class com.ta.** {*;}
    -dontwarn com.ta.**

    -keep class anet.**{*;}
    -keep class org.android.spdy.**{*;}
    -keep class org.android.agoo.**{*;}
    -dontwarn anet.**
    -dontwarn org.android.spdy.**
    -dontwarn org.android.agoo.**

    -keep public class com.android.vending.licensing.ILicensingService
    -keep public class com.kepler.jd.login.AuthSuccessActivity {public *;}
    -keep public class com.kepler.jd.sdk.WebViewActivity {public *;}
    -keep public class com.kepler.jd.sdk.JdView {public *;}
    -keep public class com.kepler.jd.login.KeplerAuth {public *;}
    -keep public class com.kepler.jd.login.KeplerApiManager {public *;}
    -keep class com.kepler.jd.sdk.JdView$InJavaScriptLocalObj{
        public <fields>;
        public <methods>;
        public *;
    }
     #必须留存
     -keep class com.kepler.jd.sdk.JdView$**{
        public <fields>;
        public <methods>;
        public *;
    }
    -dontwarn com.tencent.smtt.**

    -keep class com.lenovo.lps.sus.** { *;}
    -dontwarn com.lenovo.lps.sus.**
################混淆保护自己项目的部分代码以及引用的第三方jar包################