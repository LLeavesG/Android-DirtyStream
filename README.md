# Android-DirtyStream

Android-DirtyStream Vuln Exp repo

## DirtyStream
BlackHat2023: https://www.blackhat.com/asia-23/briefings/schedule/index.html#dirty-stream-attack-turning-android-share-targets-into-attack-vectors-30234

The Android operating system uses intents as its main means of exchanging information between applications. Besides messaging, file exchange is also possible by simply constructing an intent of action ACTION_SEND and using it to forward the desired file as an associated stream to another application. On the other end, the receiving app can define a filter in its manifest to inform the intent resolver to route the forwarded stream to a specific component.

While the sender application can construct an implicit intent and delegate the decision of choosing the target to the user, it is also possible to categorematically define a component of another package and by the time that this is exported, to trigger it by using an explicit intent. The latter eliminates the need for user interaction and can be initiated at any time while the sender application maintains a foreground state.

In this session, we will describe an attack that exploits the case where the receiving application blindly trusts an incoming stream and proceeds with processing it without validation. The concept is similar to a file upload vulnerability of a web application. More specifically, a malicious app uses a specially crafted content provider to bear a payload that it sends to the target application. As the sender controls the content but also the name of the stream, the receiver may overwrite critical files with malicious content in case it doesn't perform some necessary security checks. Additionally, when certain conditions apply, the receiver may also be forced to copy protected files to a public directory, setting the user's private data at risk.

## Demo

The folder DirtyStream is the AttackApp Source.
The folder DirtyStreamVuln is the VulnApp Source.

**Use Android Studio and Gradle to complie them.**

## Attack Feature

An attack method for file overwriting and file reading in the sandbox was implemented in AttackApp.

```java
// ......

// overwrite the file in vuln App
Uri uri = Uri.parse("content://com.test.android.fileprovider/file.txt?name=file.txt&_size=11&path=" + getFilesDir() + "/file.txt");

// read file from vuln App and write to sdcard
Uri uri = Uri.parse("content://com.test.vulnapp.fileprovider/root/data/user/0/com.test.dirtystreamvuln/shared_prefs/shared_pref.xml?displayName=../../../../../../../sdcard/test.xml");

// ......

```
## More info

Blog: https://blog.lleavesg.top/
Android-DirtyStream: https://blog.lleavesg.top/article/Android-DirtyStream 
